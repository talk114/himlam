package gateway.core.channel.vib.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import gateway.core.channel.PaymentGate;
import gateway.core.dto.request.NLVARequest;
import gateway.core.channel.vib.dto.VIBConstant;
import gateway.core.channel.vib.dto.VIBNotifyRequest;
import gateway.core.channel.vib.dto.VIBNotifyResponse;
import gateway.core.channel.vib.service.VIBIBFTService;
import gateway.core.channel.vib.service.VIBVAService;
import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import gateway.core.util.FilePathUtil;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.nganluong.naba.channel.vib.controller.VIBSignature;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.channel.vib.request.*;
import vn.nganluong.naba.channel.vib.response.*;
import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.*;
import vn.nganluong.naba.service.*;
import vn.nganluong.naba.utils.MyDateUtil;
import vn.nganluong.naba.utils.RequestUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class VIBVAServiceImpl extends PaymentGate implements VIBVAService {

    private static final Logger logger = LogManager.getLogger(VIBVAServiceImpl.class);

    private static final String SERVICE_NAME = "VA";

    @Autowired
    private PgLogChannelFunctionService pgLogChannelFunctionService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private ChannelFunctionService channelFunctionService;

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private VirtualAccountService virtualAccountService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonPGResponseService commonResponseService;

    @Autowired
    private VIBIBFTService vibibftService;

    @Autowired
    private PgUserService pgUserService;

    @Override
    public ResponseEntity<?> createVirtualAccount(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_CREATE_VA;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_CREATE_VA, true));
        try {

            CreateVirtualAccountVIBRequets accountRequets = objectMapper.readValue(pgRequest.getData(), CreateVirtualAccountVIBRequets.class);

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_CREATE_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);
            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            String vaCode = paymentAccount.getVirtualAccountCode();
            String virtualAccountNo = accountRequets.getMerchantCode() + accountRequets.getAccountNumber();
            //TODO - Khôi phục lại code cũ
//            int i = 0;
//            while (true) {
//                virtualAccountNo = createVirtualAccountNo(accountRequets.getMerchant_code(),
//                        accountRequets.getPhone_number(), vaCode);
//                if (!virtualAccountService.isExistVirtualAccount(virtualAccountNo)) {
//                    break;
//                }
//                i++;
//                if (i>2) {
//                    break;
//                }
//            }

            if (StringUtils.isEmpty(virtualAccountNo)) {

                paramsLog = new String[] { "Merchant code or phone number is invalid" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_CREATE_VA, false, false, true, paramsLog));
                return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(6);

            String dataToSign = paymentAccount.getCif() + "|" + virtualAccountNo + "|" + reqId + "|"
                    + vaCode + "|" + accountRequets.getAccountName();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("reqid", reqId);
            bodyRequest.put("code", vaCode);
            bodyRequest.put("name", accountRequets.getAccountName());

            HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // /egatepublic/1.0.0/virtualaccount/{cif}/account/{acctno}/
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "account", virtualAccountNo);

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + bodyRequest.toString() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_CREATE_VA, true, true, false, paramsLog));
            String channelStatusCode = StringUtils.EMPTY;

            ResponseEntity<String> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.POST, entity,
                    String.class);

            String responseBody = response.getBody();
            JsonNode root = objectMapper.readTree(responseBody);
            channelStatusCode = root.findPath("STATUSCODE").asText();

            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            VirtualAccountCreateGWResponse dataObject = new VirtualAccountCreateGWResponse();

            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Create account success
                    VirtualAccountDto virtualAccountDto = new VirtualAccountDto();
                    virtualAccountDto.setChannelId(channelFunction.getChannel().getId());
                    virtualAccountDto.setVirtualAccountNo(virtualAccountNo);
                    virtualAccountDto.setVirtualAccountName(accountRequets.getAccountName());
                    virtualAccountDto.setPhoneNumber(accountRequets.getAccountNumber());
                    virtualAccountDto.setMerchantCode(accountRequets.getMerchantCode());
                    virtualAccountService.createVirtualAccount(virtualAccountDto);
                    dataObject.setVirtual_account_code(virtualAccountNo);
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_CREATE_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_CREATE_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { responseBody };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_CREATE_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);
            pgResponse.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_CREATE_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_CREATE_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {
            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_CREATE_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_CREATE_VA, false));
            return commonResponseService.returnBadGateway();
        }

    }

    @Override
    public ResponseEntity<?> deleteVirtualAccount(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_DELETE_VA;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_DELETE_VA, true));
        try {

            DeleteVirtualAccountVIBRequest accountRequest = objectMapper.readValue(pgRequest.getData(), DeleteVirtualAccountVIBRequest.class);

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_DELETE_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);


            if (channelFunction == null) {
                paramsLog = new String[] { VIBConst.CHANNEL_NOT_CONFIG_MSG };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_DELETE_VA, false, false, true, paramsLog));
                return commonResponseService.returnBadGatewayWithCause(paramsLog[0]);
            }

            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(6);

            String dataToSign = paymentAccount.getCif() + "|" + accountRequest.getVirtual_account_no() + "|" + reqId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);

            headers.add("reqid", reqId);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("reqid", reqId);

            HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // /egatepublic/1.0.0/virtualaccount/{cif}/account/{acctno}/
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "account", accountRequest.getVirtual_account_no());

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + bodyRequest.toString() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_DELETE_VA, true, true, false, paramsLog));

            ResponseEntity<String> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.DELETE, entity,
                    String.class);

            String responseBody = response.getBody();
            JsonNode root = objectMapper.readTree(responseBody);
            String channelStatusCode = root.findPath("STATUSCODE").asText();

            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);

            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Delete virtual account
                    virtualAccountService.deleteVirtualAccount(accountRequest.getVirtual_account_no());
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_DELETE_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_DELETE_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { responseBody };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_DELETE_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_DELETE_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_DELETE_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {
            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_DELETE_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_DELETE_VA, false));
            return commonResponseService.returnBadGateway();
        }
    }

    @Override
    public ResponseEntity<?> enableVirtualAccount(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_ENABLE_VA;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_ENABLE_VA, true));
        try {

            EnableVirtualAccountVIBRequest accountRequest = objectMapper.readValue(pgRequest.getData(), EnableVirtualAccountVIBRequest.class);

            if (StringUtils.equalsIgnoreCase(accountRequest.getStatus(), "ENABLE")){
                accountRequest.setStatus("ENABLE");
            }
            else {
                accountRequest.setStatus("DISABLE");
            }

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_ENABLE_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);

            if (channelFunction == null) {
                paramsLog = new String[] { VIBConst.CHANNEL_NOT_CONFIG_MSG };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_ENABLE_VA, false, false, true, paramsLog));
                return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
            }

            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(6);

            String dataToSign = paymentAccount.getCif() + "|" + accountRequest.getVirtual_account_no() + "|" + reqId + "|" + accountRequest.getStatus();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("reqid", reqId);
            bodyRequest.put("action", accountRequest.getStatus());

            HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // /egatepublic/1.0.0/virtualaccount/{cif}/account/{acctno}/
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "account", accountRequest.getVirtual_account_no(), "status");

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + bodyRequest.toString() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_ENABLE_VA, true, true, false, paramsLog));

            ResponseEntity<String> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.PUT, entity,
                    String.class);

            String responseBody = response.getBody();
            JsonNode root = objectMapper.readTree(responseBody);
            String channelStatusCode = root.findPath("STATUSCODE").asText();

            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);

            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Delete virtual account
                    virtualAccountService.updateStatusVirtualAccount(accountRequest.getVirtual_account_no(), accountRequest.getStatus().equals("ENABLE") );
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_DELETE_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_DELETE_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { responseBody };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_ENABLE_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_ENABLE_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_ENABLE_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {
            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_ENABLE_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_ENABLE_VA, false));
            return commonResponseService.returnBadGateway();
        }
    }

    /**
     * 3. Get Trans Hist
     * Get informations about transaction history of Real Account.
     * @param pgRequest
     * @return
     */
    @Override
    public ResponseEntity<?> getHistoryTransactionOfRealAccount(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, true));
        try {

            VAVIBGetTransactionHistoryOfRealAccountVIBRequest historyRequest = objectMapper.readValue(pgRequest.getData(), VAVIBGetTransactionHistoryOfRealAccountVIBRequest.class);

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);


            if (channelFunction == null) {
                paramsLog = new String[] { VIBConst.CHANNEL_NOT_CONFIG_MSG };

                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false, false, true, paramsLog));
                return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
            }

            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String contractno = paymentAccount.getProviderId();// "20201222141052";

            if (StringUtils.isAllBlank(historyRequest.getFrom_date(), historyRequest.getTo_date())) {
                // dd/MM/yyyy
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(MyDateUtil.getTomorow());
                historyRequest.setFrom_date(date);
                historyRequest.setTo_date(tomorrow);
            }
            else {
                historyRequest.setFrom_date(MyDateUtil.convertDateFormat("dd/MM/yyyy","yyyy-MM-dd", historyRequest.getFrom_date()));
                historyRequest.setTo_date(MyDateUtil.convertDateFormat("dd/MM/yyyy","yyyy-MM-dd", historyRequest.getTo_date()));
            }

            String dataToSign = paymentAccount.getCif() + "|" + contractno
                    + "|" + historyRequest.getActual_acct()
                    + "|" + historyRequest.getFrom_date() + "|" + historyRequest.getTo_date()
                    + "|" + historyRequest.getFrom_amt() + "|" + historyRequest.getTo_amt()
                    + "|" + historyRequest.getC_d()
                    + "|" + historyRequest.getPage_num() + "|" + historyRequest.getPage_size();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("contractno", contractno);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));



            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();

            // mapParams.add("reqid", reqId);
            mapParams.add("fromdate", historyRequest.getFrom_date());
            mapParams.add("todate", historyRequest.getTo_date());
            mapParams.add("fromamount", historyRequest.getFrom_amt());
            mapParams.add("toamount", historyRequest.getTo_amt());
            mapParams.add("crdr", historyRequest.getC_d());
            mapParams.add("pagenum", historyRequest.getPage_num());
            mapParams.add("pagesize", historyRequest.getPage_size());

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://IP_VIB/egatepublic/1.0.0/virtualaccount/{cif}/code/{code}/tranhist
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "accounts", "currentaccount", historyRequest.getActual_acct(), "transhist").queryParams(mapParams);

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + objectMapper.writeValueAsString(mapParams) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, true, true, false, paramsLog));

            ResponseEntity<VAVIBTransactionHistoryOfVAVIBResponse> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity,
                    VAVIBTransactionHistoryOfVAVIBResponse.class);
            VAVIBTransactionHistoryOfVAVIBResponse responseData = response.getBody();
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            VAVIBTransactionHistoryOfVAGWResponse dataObject = new VAVIBTransactionHistoryOfVAGWResponse();
            String channelStatusCode = responseData.getResult().getStatus_code();
            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Get history success
                    dataObject.setSync_time(responseData.getResult().getSync_time());
                    dataObject.setData(responseData.getResult().getData());
                    dataObject.setTotal_record(responseData.getResult().getTotal_record());
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { objectMapper.writeValueAsString(responseData) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();


            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);
            pgResponse.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT, false));
            return commonResponseService.returnBadGateway();
        }
    }

    /**
     * 4. Get Trans Hist of VA
     * Get information about transaction history of Virtual Account. (VA CODE)
     * @param pgRequest
     * @return
     */
    @Override
    public ResponseEntity<?> getHistoryTransactionVA(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_VA_HISTORY_TRANSACTION_VA;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, true));
        try {

            VAVIBGetTransactionHistoryOfVAVIBRequets historyRequest = objectMapper.readValue(pgRequest.getData(), VAVIBGetTransactionHistoryOfVAVIBRequets.class);

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);
            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            String vaCode = paymentAccount.getVirtualAccountCode();

            if (StringUtils.isEmpty(vaCode)) {
                paramsLog = new String[] { "VA Code has not config" };

                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
                return commonResponseService.returnBadGatewayWithCause(paramsLog[0]);
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(8);

            if (StringUtils.isAllBlank(historyRequest.getFrom_date(), historyRequest.getTo_date())) {
                // dd/MM/yyyy
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(MyDateUtil.getTomorow());
                historyRequest.setFrom_date(date);
                historyRequest.setTo_date(tomorrow);
            }
            else {
                historyRequest.setFrom_date(MyDateUtil.convertDateFormat("dd/MM/yyyy","dd-MMM-yyyy", historyRequest.getFrom_date()));
                historyRequest.setTo_date(MyDateUtil.convertDateFormat("dd/MM/yyyy","dd-MMM-yyyy", historyRequest.getTo_date()));
            }

            String dataToSign = paymentAccount.getCif() + "|" + vaCode + "|" + reqId
                    + "|" + historyRequest.getFrom_date() + "|" + historyRequest.getTo_date()
                    + "|" + historyRequest.getPage_num() + "|" + historyRequest.getPage_size()
                    + "|" + historyRequest.getVa_result();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));



            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();

            mapParams.add("reqid", reqId);
            mapParams.add("fromdate", historyRequest.getFrom_date());
            mapParams.add("todate", historyRequest.getTo_date());
            mapParams.add("varesult", historyRequest.getVa_result());
            mapParams.add("pagenum", historyRequest.getPage_num());
            mapParams.add("pagesize", historyRequest.getPage_size());

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://IP_VIB/egatepublic/1.0.0/virtualaccount/{cif}/code/{code}/tranhist
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "code", vaCode, "tranhist").queryParams(mapParams);

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + objectMapper.writeValueAsString(mapParams) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, true, true, false, paramsLog));
            String channelStatusCode = StringUtils.EMPTY;

            ResponseEntity<VAVIBTransactionHistoryOfVAVIBResponse> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity,
                    VAVIBTransactionHistoryOfVAVIBResponse.class);
            VAVIBTransactionHistoryOfVAVIBResponse responseData = response.getBody();
//			JsonNode root = mapperObj.readTree(response.getBody()).findPath("Result");
//			VAVIBTransactionHistoryOfVAVIBResponse objCnvert  = mapperObj.convertValue(root, VAVIBTransactionHistoryOfVAVIBResponse.class);
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            VAVIBTransactionHistoryOfVAGWResponse dataObject = new VAVIBTransactionHistoryOfVAGWResponse();
            channelStatusCode = responseData.getResult().getStatus_code();
            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Get history success
                    dataObject.setSync_time(responseData.getResult().getSync_time());
                    dataObject.setData(responseData.getResult().getData());
                    dataObject.setTotal_record(responseData.getResult().getTotal_record());
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { objectMapper.writeValueAsString(responseData) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();


            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);
            pgResponse.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA, false));
            return commonResponseService.returnBadGateway();
        }
    }

    /**
     * 5. Get Trans Hist Error of VA CODE
     * Get transaction history error of Virtual Account: transaction success but virtual account not exist
     *
     * @param pgRequest
     * @return
     */
    @Override
    public ResponseEntity<?> getErrorHistoryTransactionOfVACode(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_VA_ERROR_HISTORY_TRANSACTION_VA;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, true));
        try {

            VAVIBGetTransactionHistoryOfVAVIBRequets historyRequest = objectMapper.readValue(pgRequest.getData(), VAVIBGetTransactionHistoryOfVAVIBRequets.class);

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);

            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);
            String vaCode = paymentAccount.getVirtualAccountCode();
            if (StringUtils.isEmpty(vaCode)) {
                paramsLog = new String[] { "VA Code has not config" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
                return commonResponseService.returnBadGatewayWithCause(paramsLog[0]);
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(8);

            if (StringUtils.isAllBlank(historyRequest.getFrom_date(), historyRequest.getTo_date())) {
                // dd/MM/yyyy
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(MyDateUtil.getTomorow());
                historyRequest.setFrom_date(date);
                historyRequest.setTo_date(tomorrow);
            }
            else {
                historyRequest.setFrom_date(MyDateUtil.convertDateFormat("dd/MM/yyyy","dd-MMM-yyyy", historyRequest.getFrom_date()));
                historyRequest.setTo_date(MyDateUtil.convertDateFormat("dd/MM/yyyy","dd-MMM-yyyy", historyRequest.getTo_date()));
            }

            String dataToSign = paymentAccount.getCif() + "|" + vaCode + "|" + reqId
                    + "|" + historyRequest.getFrom_date() + "|" + historyRequest.getTo_date()
                    + "|" + historyRequest.getPage_num() + "|" + historyRequest.getPage_size();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();

            mapParams.add("reqid", reqId);
            mapParams.add("fromdate", historyRequest.getFrom_date());
            mapParams.add("todate", historyRequest.getTo_date());
            mapParams.add("pagenum", historyRequest.getPage_num());
            mapParams.add("pagesize", historyRequest.getPage_size());

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://IP_VIB/egatepublic/1.0.0/virtualaccount/{cif}/code/{code}/tranhist
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "code", vaCode, "tranhisterror").queryParams(mapParams);

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + objectMapper.writeValueAsString(mapParams) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, true, true, false, paramsLog));

            ResponseEntity<VAVIBTransactionHistoryOfVAVIBResponse> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity,
                    VAVIBTransactionHistoryOfVAVIBResponse.class);
            VAVIBTransactionHistoryOfVAVIBResponse responseData = response.getBody();
//			JsonNode root = mapperObj.readTree(response.getBody()).findPath("Result");
//			VAVIBTransactionHistoryOfVAVIBResponse objCnvert  = mapperObj.convertValue(root, VAVIBTransactionHistoryOfVAVIBResponse.class);
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            VAVIBTransactionHistoryOfVAGWResponse dataObject = new VAVIBTransactionHistoryOfVAGWResponse();
            String channelStatusCode = responseData.getResult().getStatus_code();
            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Get history success
                    dataObject.setSync_time(responseData.getResult().getSync_time());
                    dataObject.setData(responseData.getResult().getData());
                    dataObject.setTotal_record(responseData.getResult().getTotal_record());
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { objectMapper.writeValueAsString(responseData) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();


            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);
            pgResponse.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA, false));
            return commonResponseService.returnBadGateway();
        }
    }

    private PaymentAccount getPaymentAccount(PGRequest pgRequest, ChannelFunction channelFunction) {
        PgUser pgUser = pgUserService.findByCode(pgRequest.getPgUserCode());
        return paymentAccountService.getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channelFunction.getChannel().getId());
    }

    /**
     * 6. Get list Virtual Account
     *
     * @param pgRequest
     * @return
     */
    @Override
    public ResponseEntity<?> getListVirtualAccount(PGRequest pgRequest) {

        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_VA_LIST_VA;
        String[] paramsLog = null;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_VA_LIST_VA, true));
        try {

            VAGetListVirtualAccountVIBRequest listVARequest = objectMapper.readValue(pgRequest.getData(), VAGetListVirtualAccountVIBRequest.class);

            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_LIST_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);

            if (channelFunction == null) {
                paramsLog = new String[] { VIBConst.CHANNEL_NOT_CONFIG_MSG };

                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_LIST_VA, false, false, true, paramsLog));
                return commonResponseService.returnBadRequets_WithCause(paramsLog[0]);
            }

            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(8);

            String dataToSign = paymentAccount.getCif() + "|" + reqId
                    + "|" + listVARequest.getPage_num() + "|" + listVARequest.getPage_size();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();

            mapParams.add("reqid", reqId);
            mapParams.add("pageindex", listVARequest.getPage_num());
            mapParams.add("pagesize", listVARequest.getPage_size());

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://IP_VIB/egatepublic/1.0.0/virtualaccount/{cif}/accounts
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "accounts").queryParams(mapParams);

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + objectMapper.writeValueAsString(mapParams) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_LIST_VA, true, true, false, paramsLog));

            ResponseEntity<VAVIBGetListVirtualAccountVIBResponse> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity,
                    VAVIBGetListVirtualAccountVIBResponse.class);
            VAVIBGetListVirtualAccountVIBResponse responseData = response.getBody();
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            VAVIBGetListVirtualAccountGWResponse dataObject = new VAVIBGetListVirtualAccountGWResponse();
            String channelStatusCode = responseData.getResult().getStatus_code();
            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Get list virtual account
                    dataObject.setData(responseData.getResult().getData());
                    dataObject.setTotal_record(responseData.getResult().getTotal_record());
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_LIST_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_LIST_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { objectMapper.writeValueAsString(responseData) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_LIST_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);
            pgResponse.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_LIST_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_LIST_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_LIST_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_LIST_VA, false));
            return commonResponseService.returnBadGateway();
        }

    }

    /**
     * 7. Get Detail Virtual Account
     *
     * @param pgRequest
     * @return
     */
    @Override
    public ResponseEntity<?> getDetailVirtualAccount(PGRequest pgRequest) {
        String channelFunctionId = VIBConst.CHANNEL_FUNCTION_VA_DETAIL_VA;
        String[] paramsLog;
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                VIBConst.FUNCTION_CODE_VA_DETAIL_VA, true));
        try {
            VAGetDetailVirtualAccountVIBRequest vaRequest = objectMapper.readValue(pgRequest.getData(), VAGetDetailVirtualAccountVIBRequest.class);
            String accessToken = vibibftService.getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false));
                return commonResponseService.returnBadGateway();
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);

            PaymentAccount paymentAccount = getPaymentAccount(pgRequest, channelFunction);

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(8);

            String dataToSign = paymentAccount.getCif()
                    + "|" + vaRequest.getVirtual_acct() + "|" + reqId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();

            mapParams.add("reqid", reqId);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://IP_VIB/egatepublic/1.0.0/virtualaccount/{cif}/account/{virtual account no}
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(paymentAccount.getCif(), "account", vaRequest.getVirtual_acct()).queryParams(mapParams);

            paramsLog = new String[] { "URL: " + builderUri.toUriString() + ", data: " + objectMapper.writeValueAsString(mapParams) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_DETAIL_VA, true, true, false, paramsLog));

            ResponseEntity<VAVIBGetDetailVirtualAccountVIBResponse> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity,
                    VAVIBGetDetailVirtualAccountVIBResponse.class);
            VAVIBGetDetailVirtualAccountVIBResponse responseData = response.getBody();
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            VAVIBGetDetailVirtualAccountGWResponse dataObject = new VAVIBGetDetailVirtualAccountGWResponse();
            String channelStatusCode = responseData.getResult().getStatus_code();
            if (response.getStatusCode().equals(HttpStatus.OK)) {

                if (StringUtils.equals(channelStatusCode, VIBConst.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                    // Get detail virtual account
                    dataObject.setData(responseData.getResult().getData());
                }

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[] { "Response is empty" };
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false, false, true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false));
                return commonResponseService.returnChannelBadRequest("");
            }

            paramsLog = new String[] { objectMapper.writeValueAsString(responseData) };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_DETAIL_VA, true, false, true, paramsLog));

            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

            pgResponse.setErrorCode(prefixResult.getErrorCode());
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setChannelErrorCode(channelStatusCode);
            pgResponse.setChannelMessage(StringUtils.EMPTY);
            pgResponse.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false));
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false, false, true, paramsLog));
            return commonResponseService.returnChannelBadRequest(e.getMessage());
        }

        catch (Exception e) {

            paramsLog = new String[] { e.getMessage() };
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_DETAIL_VA, false));
            return commonResponseService.returnBadGateway();
        }
    }

    private String createVirtualAccountNo(String merchantCode, String phoneNumber, String vaCode) {

        if (StringUtils.length(merchantCode) == 5 && StringUtils.length(phoneNumber) == 10) {
            if (NumberUtils.isDigits(merchantCode) && NumberUtils.isDigits(phoneNumber)) {
                return vaCode + merchantCode + phoneNumber
                        + StringUtils.upperCase(RandomStringUtils.randomAlphanumeric(7));
            }
        }

        return StringUtils.EMPTY;
    }

    @Override
    public ResponseEntity<?> notifyListener(String signature, String input) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VIBConst.CHANNEL_CODE,SERVICE_NAME,"notifyListener",true));
        String keyRootPath = FilePathUtil.getAbsolutePath(VIBConstant.PUBLIC_KEY_PATH);
        System.out.println(signature);
        PublicKey publicKey = PGSecurity.getPublicKey(keyRootPath);
        //TODO fix isSignatureValid later
        VIBNotifyResponse vibResponse = null;
        String errorCode = "00";
        boolean isSignatureValid = PGSecurity.verifySHA256withRSA(signature,input,publicKey);
        //todo uncomment when go live
//        if(!PGSecurity.verifySHA256withRSA(signature,input,publicKey)){
//            return new ResponseEntity(new VIBNotifyResponse("97","verify signature fail",""),HttpStatus.OK);
//        }
        System.out.println("Signature check status: " + isSignatureValid);
        try {
            VIBNotifyRequest request = objectMapper.readValue(input, VIBNotifyRequest.class);
            if("".equals(request.getSequenceNumber()) || request.getSequenceNumber() == 0){
                errorCode = "22";
                vibResponse =new VIBNotifyResponse("22", "Transaction id is empty",input);
            }
            System.out.println(request);
            Payment paymentSaved = paymentService.findByMerchantTransactionId(String.valueOf(request.getSequenceNumber()));
            if(paymentSaved != null){
                errorCode = "21";
                vibResponse = new VIBNotifyResponse("21", "Transaction id has exist",input);
            }
            //TODO save database
            NLVARequest newRequest = NLVARequest.parse(request, "VIB");
            PGResponse callback = PGResponse.getInstanceFullValue(true, newRequest,"00","",PGResponse.SUCCESS);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("bank_transaction_id", newRequest.getBankTransactionId());
            params.put("bank_account", newRequest.getBankAccount());
            params.put("transaction_amount", newRequest.getTransactionAmount());
            params.put("cashin_id",newRequest.getCashinId());
            params.put("bank_code", newRequest.getBankCode());
            params.put("bank_time", newRequest.getTransactionDate());

            String dataInput = new JSONObject(params).toString();

            Map<String, Object> paramsBig = new HashMap<>();
            paramsBig.put("status", true);
            paramsBig.put("error_code", errorCode);
            paramsBig.put("message", vibResponse);
            paramsBig.put("data", dataInput);
            paramsBig.put("checksum", PGSecurity.sha256(dataInput+PGSecurity.WITH_NGANLUONG_CALLBACK));

            String response = HttpUtil.send("https://sandbox.nganluong.vn:8088/nl35/api/Vibva/index", paramsBig);
            System.out.println(response);
            if(vibResponse==null) {
                PaymentDTO payment = PaymentDTO.builder()
                        .channelId(1)
                        .merchantTransactionId(String.valueOf(request.getSequenceNumber()))
                        .accountNo(request.getActualAccount())
                        .description(request.getTransactionDescription())
                        .amount(String.valueOf(request.getTransactionAmount()))
                        .rawRequest(input)
                        .build();
                paymentService.createPayment(payment);
                logger.info(commonLogService.createContentLogStartEndFunction(
                        VIBConst.CHANNEL_CODE,SERVICE_NAME,"notifyListener",false));
                return new ResponseEntity(new VIBNotifyResponse("00", "Request Success", "ACCT_NO" + request.getActualAccount()), HttpStatus.OK);
            }else {
                return new ResponseEntity(vibResponse, HttpStatus.OK);
            }

        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        } catch (NoSuchAlgorithmException e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return new ResponseEntity(new VIBNotifyResponse("99","Request Failed",null),HttpStatus.OK);
    }

    private Response requestApi(String url, String bodyRequest) throws IOException {
        System.out.println(bodyRequest);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyRequest);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.toString());
        return response;
    }
}
