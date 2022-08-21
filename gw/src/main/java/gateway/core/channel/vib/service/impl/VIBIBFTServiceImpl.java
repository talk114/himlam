package gateway.core.channel.vib.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.vib.service.VIBIBFTService;
import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import org.apache.commons.codec.Charsets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Node;
import vn.nganluong.naba.channel.vib.controller.VIBSOAPUtil;
import vn.nganluong.naba.channel.vib.controller.VIBSignature;
import vn.nganluong.naba.channel.vib.dto.*;
import vn.nganluong.naba.channel.vib.request.*;
import vn.nganluong.naba.channel.vib.response.*;
import vn.nganluong.naba.dto.AddTransactionGWResponse;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.entities.PgUser;
import vn.nganluong.naba.service.*;
import vn.nganluong.naba.utils.MyDateUtil;
import vn.nganluong.naba.utils.RequestUtil;

import javax.net.ssl.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VIBIBFTServiceImpl extends PaymentGate implements VIBIBFTService {

    private static final Logger logger = LogManager.getLogger(VIBIBFTServiceImpl.class);

    @Autowired
    private PgLogChannelFunctionService pgLogChannelFunctionService;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private ChannelFunctionService channelFunctionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private CommonPGResponseService commonResponseService;

    @Autowired
    private PgUserService pgUserService;

    @Override
    public String getAccessToken() {
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, true));

        RestTemplate restTemplate = null;
        try {
            restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);

            String[] paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
        }

        ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, VIBConst.CHANNEL_CODE);

        if (channelFunction == null) {
            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);

            String[] paramsLog = new String[]{VIBConst.CHANNEL_NOT_CONFIG_MSG};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", channelFunction.getAuthKey());

        MultiValueMap<String, String> mapBody = new LinkedMultiValueMap<String, String>();
        mapBody.add("grant_type", "password");
        mapBody.add("username", channelFunction.getUser());
        mapBody.add("password", channelFunction.getPassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(mapBody,
                headers);

        // post request
        ResponseEntity<GetTokenResponse> response = restTemplate
                .exchange(RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                        channelFunction.getUrl()), HttpMethod.POST, entity, GetTokenResponse.class);

        GetTokenResponse getTokenResponse = response.getBody();

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, true);

            String[] paramsLog = new String[]{getTokenResponse.getAccess_token()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, true, false, true, paramsLog));

        } else {
            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ACCESS_TOKEN_CODE, false);

            String[] paramsLog = new String[]{"Resonse httpstatus: " + response.getStatusCode()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
        }

        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ACCESS_TOKEN, false));
        return getTokenResponse.getAccess_token();
    }

    @Override
    public ResponseEntity<?> checkInvalidAccount(PGRequest pgRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, true));
        String accessToken = getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
            return commonResponseService.returnBadGateway();
        }
        try {
            ValidAccountVIBRequets accountRequest = objectMapper.readValue(pgRequest.getData(), ValidAccountVIBRequets.class);
            String channelFunctionId = VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_VIB_CODE;

            if (StringUtils.equals(accountRequest.getAccount_type(), "NAPAS")) {
                channelFunctionId = VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE;
            }
            ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(channelFunctionId, VIBConst.CHANNEL_CODE);

            PgUser pgUser = pgUserService.findByCode(pgRequest.getPgUserCode());
            PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channelFunction.getChannel().getId());

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String reqId = RandomStringUtils.randomAlphanumeric(6);
            String dataToSign = paymentAccount.getCif() + "|" + accountRequest.getAccount_no() + "|" + reqId;

            if (StringUtils.equals(channelFunctionId, VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE)) {
                dataToSign = paymentAccount.getCif() + "|" + accountRequest.getAccount_no() + "|" + paymentAccount.getSourceAccountNo() + "|" + accountRequest.getBank_id() + "|" + reqId;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("signeddata", VIBSignature.signSHA1withRSA(dataToSign));

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();

            if (StringUtils.equals(channelFunctionId, VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE)) {
                mapParams.add("acctno", accountRequest.getAccount_no());
                mapParams.add("fromacctno", paymentAccount.getSourceAccountNo());
                mapParams.add("bankid", accountRequest.getBank_id());
            }
            mapParams.add("cif", paymentAccount.getCif());
            mapParams.add("reqid", reqId);

            if (channelFunction == null) {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                String[] paramsLog = new String[]{VIBConst.CHANNEL_NOT_CONFIG_MSG};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false, true, paramsLog));
                return commonResponseService.returnBadGateway();
            }
            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl).pathSegment(accountRequest.getAccount_no()).queryParams(mapParams);

            ValidAccountVIBResponse validAccountVIBResponse = null;
            String acctName = StringUtils.EMPTY;
            String acctStatus = StringUtils.EMPTY;

            ObjectMapper mapperObj = new ObjectMapper();
            String[] paramsLog = new String[]{mapperObj.writeValueAsString(mapParams)};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, true, true, false, paramsLog));
            try {
                ResponseEntity<ValidAccountVIBResponse> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity, ValidAccountVIBResponse.class);
                validAccountVIBResponse = response.getBody();
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    if (StringUtils.equals(validAccountVIBResponse.getResult().getStatus_code(), "000000")) {
                        acctStatus = validAccountVIBResponse.getResult().getData().getStatus();
                        if (StringUtils.equals(channelFunctionId, VIBConst.CHANNEL_FUNCTION_INVALID_ACCOUNT_NAPAS_CODE)) {
                            acctName = validAccountVIBResponse.getResult().getData().getAcct_name();
                        } else {
                            acctName = validAccountVIBResponse.getResult().getData().getAcct_desc();
                        }
                    }
                    pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, true);
                }
                paramsLog = new String[]{mapperObj.writeValueAsString(validAccountVIBResponse)};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, true, false, true, paramsLog));
            } catch (HttpClientErrorException e) {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[]{e.getMessage()};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false, true, paramsLog));
                return commonResponseService.returnBadRequets_WithCause(e.getMessage());
            } catch (Exception e) {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[]{e.getMessage()};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false, true, paramsLog));
                return commonResponseService.returnBadGatewayWithCause(e.getMessage());
            }
            if (validAccountVIBResponse == null) {
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, channelFunctionId, false);
                paramsLog = new String[]{"Response is empty"};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false,true, paramsLog));
                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
                return commonResponseService.returnChannelBadRequest("");
            }
            PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);
            jsonObject.setErrorCode(prefixResult.getErrorCode());
            jsonObject.setMessage(prefixResult.getMessage());
            jsonObject.setChannelErrorCode(validAccountVIBResponse.getResult().getStatus_code());

            ValidAccountGWResponse dataObject = new ValidAccountGWResponse();
            dataObject.setAccount_name(acctName);
            dataObject.setAccount_status(acctStatus);
            jsonObject.setData(dataObject);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            String[] paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false, false,
                    true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_VALID_ACCOUNT, false));
            return commonResponseService.returnBadGateway();
        }

    }

    @Override
    public ResponseEntity<?> getAccountBalance(PGRequest pgRequest) {

        String[] paramsLog = null;
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, true));

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, VIBConst.CHANNEL_CODE);

            // PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE, channelFunction.getChannel().getId());
            PgUser pgUser = pgUserService.findByCode(pgRequest.getPgUserCode());
            PaymentAccount paymentAccount = paymentAccountService
                    .getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channelFunction.getChannel().getId());
            GetAccountBalanceVIBRequets accountBalanceRequets = objectMapper.readValue(pgRequest.getData(), GetAccountBalanceVIBRequets.class);
            String accountNo = accountBalanceRequets.getAccount_no();
            if (StringUtils.isBlank(accountNo)) {
                accountNo = paymentAccount.getSourceAccountNo();
            }

            RqAccountBalanceObject rqObject = new RqAccountBalanceObject();
            rqObject.CIF = paymentAccount.getCif();
            rqObject.accountNumber = accountNo;
            rqObject.username = paymentAccount.getUsername();

            String dataToSign = VIBSignature.SHA1AccountBalance(rqObject);
            String signature = VIBSignature.genSignature(dataToSign);

            String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
                    + "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + "    <Body>\r\n"
                    + "        <getAccountBalance xmlns=\"http://vn.vib.fastpay.erp\">\r\n"
                    + "            <!-- Optional -->\r\n" + "            <rqObject>\r\n"
                    + "                <CIF xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">"
                    + paymentAccount.getCif() + "</CIF>\r\n"
                    + "                <accountNumber xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">"
                    + accountNo + "</accountNumber>\r\n"
                    + "                <signData xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">" + signature
                    + "</signData>\r\n" + "                <username xmlns=\"http://object.vn.vib.fastpay.erp/xsd\">"
                    + paymentAccount.getUsername() + "</username>\r\n" + "            </rqObject>\r\n"
                    + "        </getAccountBalance>\r\n" + "    </Body>\r\n" + "</Envelope>";
            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
                    new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));
            soapMessage.saveChanges();

            String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                    channelFunction.getUrl());

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            paramsLog = new String[]{body};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, true, true,
                    false, paramsLog));

            disableSslVerification();
            SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
            // soapResponse.writeTo(System.out); // Print response

//			SOAPBody soapBody = soapResponse.getSOAPBody();
//			Node getBalanceRes = soapBody.getFirstChild();
//			String curCode = VIBSOAPUtil.getValue(getBalanceRes, "return.acctRec.curCode");
//			String ballType = VIBSOAPUtil.getValue(getBalanceRes, "return.acctRec.listAcct.ballType");
//			String curAmt = VIBSOAPUtil.getValue(getBalanceRes, "return.acctRec.listAcct.curAmt");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            String strMsg = new String(out.toByteArray());
            XMLInputFactory xif = XMLInputFactory.newFactory();
            InputStream is = new ByteArrayInputStream(strMsg.getBytes(Charsets.UTF_8));
            InputStreamReader reader = new InputStreamReader(is);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
            xsr.nextTag(); //getAccountBalanceResponse
            xsr.nextTag(); //return
//        	xsr.nextTag(); //acctRec
            // System.out.println(xsr.getLocalName());

            JAXBContext jc = JAXBContext.newInstance(GetAccountBalanceResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            GetAccountBalanceResponse accountBalanceResponse = unmarshaller.unmarshal(xsr, GetAccountBalanceResponse.class).getValue(); // JAXBElement

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);
            GetAccountBalanceGWResponse dataObject = new GetAccountBalanceGWResponse();
            jsonObject.setChannelErrorCode(accountBalanceResponse.getErrorObject().getErrorCode());
            jsonObject.setChannelMessage(accountBalanceResponse.getErrorObject().getMessage());

            if (accountBalanceResponse.getErrorObject().getErrorCode().equals("000000")) {
                // GetAccountBalanceGWResponse jsonObject = new GetAccountBalanceGWResponse();

                PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());
                dataObject.setListBalance(accountBalanceResponse.getAcctRec().getListAcct());
                jsonObject.setData(dataObject);

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, true);

                paramsLog = new String[]{strMsg};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, true, false,
                        true, paramsLog));
            } else {
                paramsLog = new String[]{strMsg};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false, false,
                        true, paramsLog));

                PGResponse prefixResult = commonResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, false);
                // return returnChannelBadRequest("Response channel code is not success");
            }

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false));
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);

        } catch (ConnectException e) {
            doCatchConnectionExceptionGetAccountBalance(e.getMessage());
            return commonResponseService.returnChannelTimeout();

        } catch (SOAPException e) {
            doCatchConnectionExceptionGetAccountBalance(e.getMessage());

            return commonResponseService.returnChannelBadRequest(e.getMessage());
        } catch (Exception e) {

            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false, false,
                    true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false));

            return commonResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private void doCatchConnectionExceptionGetAccountBalance(String message) {
        String[] paramsLog;
        pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_BALANCE_ACCOUNT_CODE, false);

        paramsLog = new String[]{message};
        logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false, false,
                true, paramsLog));
        logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT, false));
    }

    @Override
    public ResponseEntity<?> addTransaction(PGRequest pgRequest) {
        String[] paramsLog = null;
        PaymentDTO paymentDTO = new PaymentDTO();
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, true));

            AddTransactionVIBRequets transactionInfo = objectMapper.readValue(pgRequest.getData(), AddTransactionVIBRequets.class);

            if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
                paramsLog = new String[]{"Client transaction is empty"};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
                        true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
                return commonResponseService.returnBadRequets_TransactionEmpty();
            } else {
                Payment paymentToCheckExist = paymentService.findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
                if (paymentToCheckExist != null) {

                    paramsLog = new String[]{"Client transaction id (trace id) already exist (" + transactionInfo.getClient_transaction_id() + ")"};
                    logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
                            true, paramsLog));

                    logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE,
                            VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
                    return commonResponseService.returnBadRequets_TransactionExist();
                }
            }

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, VIBConst.CHANNEL_CODE);

            PgUser pgUser = pgUserService.findByCode(pgRequest.getPgUserCode());
            PaymentAccount paymentAccount = paymentAccountService
                    .getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channelFunction.getChannel().getId());

            if (StringUtils.isBlank(transactionInfo.getFrom_account())) {
                transactionInfo.setFrom_account(paymentAccount.getSourceAccountNo());
            }

            RqInputTransactionExtObject rqObject = new RqInputTransactionExtObject();
            rqObject.Amount = transactionInfo.getAmount();
            rqObject.Ccy = VIBConst.CCY;
            rqObject.Channel_type = VIBConst.CHANNEL_TYPE;
            rqObject.Client_no = paymentAccount.getCif();
            rqObject.Narrative = transactionInfo.getNarrative();
            rqObject.ServiceID = transactionInfo.getService_id();
            rqObject.Trans_type = VIBConst.TRANSACTION_TYPE;
            rqObject.User_id = paymentAccount.getUsername();
            List<TransactionExtObj> transactionExtObjs = new ArrayList<TransactionExtObj>();
            TransactionExtObj tranObj = new TransactionExtObj();

            tranObj.amount = transactionInfo.getAmount();
            tranObj.ccy = VIBConst.CCY;
            tranObj.ben_bankid = transactionInfo.getBen_bank_id();
            tranObj.toacct = transactionInfo.getTo_account();
            transactionExtObjs.add(tranObj);
            rqObject.TransList = transactionExtObjs;

            String dataToSign = VIBSignature.SHA1AddTransaction(rqObject);
            String signature = VIBSignature.genSignature(dataToSign);

            String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
                    + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vn=\"http://vn.vib.fastpay.erp\" xmlns:xsd=\"http://object.vn.vib.fastpay.erp/xsd\">\r\n"
                    + "   <soapenv:Header />\r\n"
                    + "   <soapenv:Body>\r\n"
                    + "      <vn:makeTransaction>\r\n"
                    + "         <vn:rqObject>\r\n"
                    + "            <xsd:amount>" + transactionInfo.getAmount() + "</xsd:amount>\r\n"
                    + "            <xsd:ccy>" + VIBConst.CCY + "</xsd:ccy>\r\n"
                    + "            <xsd:chanel_type>" + rqObject.Channel_type + "</xsd:chanel_type>\r\n"
                    + "            <xsd:checkSum />\r\n"
                    + "            <xsd:client_no>" + rqObject.Client_no + "</xsd:client_no>\r\n"
                    + "            <xsd:narrative>" + rqObject.Narrative + "</xsd:narrative>\r\n"
                    + "            <xsd:serviceID>" + rqObject.ServiceID + "</xsd:serviceID>\r\n"
                    + "            <xsd:signData>" + signature + "</xsd:signData>\r\n"
                    + "            <xsd:transList>\r\n"
                    + "               <xsd:acctname>" + StringUtils.trimToEmpty(transactionInfo.getAccount_name()) + "</xsd:acctname>\r\n"
                    + "               <xsd:amount>" + transactionInfo.getAmount() + "</xsd:amount>\r\n"
                    + "               <xsd:ben_bankcity />\r\n"
                    + "               <xsd:ben_bankcountry />\r\n"
                    + "               <xsd:ben_bankid>" + StringUtils.trimToEmpty(transactionInfo.getBen_bank_id()) + "</xsd:ben_bankid>\r\n"
                    + "               <xsd:ben_bankname>" + StringUtils.trimToEmpty(transactionInfo.getBen_bank_name()) + "</xsd:ben_bankname>\r\n"
                    + "               <xsd:ben_branchcode />\r\n"
                    + "               <xsd:ben_branchname />\r\n"
                    + "               <xsd:benadd />\r\n"
                    + "               <xsd:bencity />\r\n"
                    + "               <xsd:bencountry />\r\n"
                    + "               <xsd:benname>" + StringUtils.trimToEmpty(transactionInfo.getBen_name()) + "</xsd:benname>\r\n"
                    + "               <xsd:billno />\r\n"
                    + "               <xsd:ccy>" + VIBConst.CCY + "</xsd:ccy>\r\n"
                    + "               <xsd:clientTransID>" + transactionInfo.getClient_transaction_id() + "</xsd:clientTransID>\r\n"
                    + "               <xsd:fee_amount />\r\n"
                    + "               <xsd:feeside>" + transactionInfo.getFeeside() + "</xsd:feeside>\r\n"
                    + "               <xsd:fromacct>" + transactionInfo.getFrom_account() + "</xsd:fromacct>\r\n"
                    + "               <xsd:inter_bankcity />\r\n"
                    + "               <xsd:inter_bankcountry />\r\n"
                    + "               <xsd:inter_bankname />\r\n"
                    + "               <xsd:inter_branchname />\r\n"
                    + "               <xsd:interbankid />\r\n"
                    + "               <xsd:narrative>" + rqObject.Narrative + "</xsd:narrative>\r\n"
                    + "               <xsd:partnerid />\r\n"
                    + "               <xsd:response_code>000000</xsd:response_code>\r\n"
                    + "               <xsd:toacct>" + tranObj.toacct + "</xsd:toacct>\r\n"
                    + "               <xsd:transcode />\r\n"
                    + "               <xsd:transnumber />\r\n"
                    + "               <xsd:transtatus />\r\n"
                    + "               <xsd:transtatus_desc />\r\n"
                    + "            </xsd:transList>\r\n"
                    + "            <xsd:trans_type>" + rqObject.Trans_type + "</xsd:trans_type>\r\n"
                    + "            <xsd:user_id>" + rqObject.User_id + "</xsd:user_id>\r\n"
                    + "         </vn:rqObject>\r\n"
                    + "      </vn:makeTransaction>\r\n"
                    + "   </soapenv:Body>\r\n"
                    + "</soapenv:Envelope>";
            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
                    new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));
            soapMessage.saveChanges();

            // String endpointUrl = "https://103.10.212.98/erp/services/AddTransactionWS?wsdl";
            String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                    channelFunction.getUrl());

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            paramsLog = new String[]{body};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, true, true,
                    false, paramsLog));

            // Create payment before call api into channel
            // Add payment to database:
            paymentDTO.setChannelId(channelFunction.getChannel().getId());
            paymentDTO.setAmount(transactionInfo.getAmount());
            paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
            paymentDTO.setAccountNo(transactionInfo.getTo_account());
            paymentDTO.setRawRequest(body);
            paymentDTO.setDescription(transactionInfo.getNarrative());

            if (StringUtils.contains(transactionInfo.getService_id(), VIBConst.EnumTransactionType.SMLACCT.name())) {
                paymentDTO.setChannelTransactionType(VIBConst.EnumTransactionType.SMLACCT.typeName());
            } else if (StringUtils.contains(transactionInfo.getService_id(), VIBConst.EnumTransactionType.VIBA.name())) {
                paymentDTO.setChannelTransactionType(VIBConst.EnumTransactionType.VIBA.typeName());
            }

            paymentDTO.setSourceAccount(transactionInfo.getFrom_account());

            paymentService.createPayment(paymentDTO);

            // Call api
            disableSslVerification();
            SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);
            // soapResponse.writeTo(System.out); // Print response
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            soapResponse.writeTo(output);
            String soapMessageContent = new String(output.toByteArray());

            SOAPBody soapBody = soapResponse.getSOAPBody();
            Node addTransRes = soapBody.getFirstChild();
            String curCode = VIBSOAPUtil.getValue(addTransRes, "return.errorObject.errorCode");
            String message = VIBSOAPUtil.getValue(addTransRes, "return.errorObject.message");
            String transactionId = VIBSOAPUtil.getValue(addTransRes, "return.o_transid");

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);
            AddTransactionGWResponse dataObject = new AddTransactionGWResponse();
            jsonObject.setChannelMessage(message);
            jsonObject.setChannelErrorCode(curCode);
            dataObject.setTransaction_id(transactionId);
            jsonObject.setData(dataObject);

            paymentDTO.setChannelTransactionId(transactionId);
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + soapMessageContent);

            if (StringUtils.equals(curCode, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION_RESPONSE_STATUS_CODE_SUCCESS)) {

                PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());
                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, true);
                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());

                paramsLog = new String[]{soapMessageContent};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, true, false,
                        true, paramsLog));
            } else {

                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                PGResponse prefixResult = commonResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, false);

                paramsLog = new String[]{soapMessageContent};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
                        true, paramsLog));
            }

            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));

            return new ResponseEntity<>(jsonObject, HttpStatus.OK);

        } catch (SOAPException e) {

            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
                    true, paramsLog));

            pgLogChannelFunctionService.writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_ADD_TRANSACTION, false);

            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));

            return commonResponseService.returnChannelBadRequest(e.getMessage());

        } catch (Exception e) {
            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false,
                    true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_ADD_TRANSACTION, false));

            return commonResponseService.returnBadGateway();
        }
    }

    @Override
    public ResponseEntity<?> getTransactionStatus(PGRequest pgRequest) {
        String[] paramsLog = null;
        try {
            logger.info(commonLogService
                    .createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                            VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION,
                            VIBConst.CHANNEL_CODE);

            PgUser pgUser = pgUserService.findByCode(pgRequest.getPgUserCode());
            PaymentAccount paymentAccount = paymentAccountService
                    .getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channelFunction.getChannel().getId());

            RqERPLinkGetListTransObject rqObject = new RqERPLinkGetListTransObject();
            GetTransactionStatusVIBRequets transactionStatusRequets = objectMapper
                    .readValue(pgRequest.getData(), GetTransactionStatusVIBRequets.class);

            if (StringUtils
                    .isAllBlank(transactionStatusRequets.getFrom_date(), transactionStatusRequets.getTo_date())) {
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                String tomorrow = new SimpleDateFormat("dd/MM/yyyy").format(MyDateUtil.getTomorow());
                transactionStatusRequets.setFrom_date(date);
                transactionStatusRequets.setTo_date(tomorrow);
            }

            rqObject.i_client_no = paymentAccount.getCif();
            rqObject.i_from_date = transactionStatusRequets.getFrom_date();
            rqObject.i_page_num = "1";
            rqObject.i_page_size = "500";
            rqObject.i_service_type = transactionStatusRequets.getService_type();
            rqObject.i_to_date = transactionStatusRequets.getTo_date();
            rqObject.i_trans_id = StringUtils.trimToEmpty(transactionStatusRequets.getClient_transaction_id());
            rqObject.i_trans_type = StringUtils.trimToEmpty(transactionStatusRequets.getTransaction_type());
            rqObject.i_userid = paymentAccount.getUsername();

            String dataToSign = VIBSignature.SHA1TransactionStatus(rqObject);
            String signature = VIBSignature.genSignature(dataToSign);

            String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vn=\"http://vn.vib.fastpay.erp\" xmlns:xsd=\"http://object.vn.vib.fastpay.erp/xsd\">\r\n"
                    + "   <soapenv:Header/>\r\n"
                    + "   <soapenv:Body>\r\n"
                    + "      <vn:queryTransactionStatus>\r\n"
                    + "         <vn:rqObject>\r\n"
                    + "            <xsd:checkSum></xsd:checkSum>\r\n"
                    + "            <xsd:i_client_no>" + rqObject.i_client_no + "</xsd:i_client_no>\r\n"
                    + "            <xsd:i_from_date>" + rqObject.i_from_date + "</xsd:i_from_date>\r\n"
                    + "            <xsd:i_page_num>" + rqObject.i_page_num + "</xsd:i_page_num>\r\n"
                    + "            <xsd:i_page_size>" + rqObject.i_page_size + "</xsd:i_page_size>\r\n"
                    + "            <xsd:i_service_type>" + rqObject.i_service_type + "</xsd:i_service_type>\r\n"
                    + "            <xsd:i_to_date>" + rqObject.i_to_date + "</xsd:i_to_date>\r\n"
                    + "            <xsd:i_trans_id>" + rqObject.i_trans_id + "</xsd:i_trans_id>\r\n"
                    + "            <xsd:i_trans_type>" + rqObject.i_trans_type + "</xsd:i_trans_type>\r\n"
                    + "            <xsd:i_userid>" + rqObject.i_userid + "</xsd:i_userid>\r\n"
                    + "            <xsd:signData>" + signature + "</xsd:signData>\r\n"
                    + "         </vn:rqObject>\r\n"
                    + "      </vn:queryTransactionStatus>\r\n"
                    + "   </soapenv:Body>\r\n"
                    + "</soapenv:Envelope>";

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
                    new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));

            soapMessage.saveChanges();

            // String endpointUrl = "https://103.10.212.98/erp/services/QueryTransactionStatusWS?wsdl";
            String endpointUrl = RequestUtil
                    .createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                            channelFunction.getUrl());

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            paramsLog = new String[]{body};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                    VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, true,
                    false, paramsLog));

            disableSslVerification();
            SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);

            String soapMessageContent = new String(out.toByteArray());

            String strMsg = new String(out.toByteArray());
            XMLInputFactory xif = XMLInputFactory.newFactory();
            InputStream is = new ByteArrayInputStream(strMsg.getBytes(Charsets.UTF_8));
            InputStreamReader reader = new InputStreamReader(is);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
            xsr.nextTag(); //getAccountBalanceResponse
            xsr.nextTag(); //return
//        	xsr.nextTag(); //acctRec

            JAXBContext jc = JAXBContext.newInstance(GetTransactionStatusResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            GetTransactionStatusResponse transactionStatusResponse = unmarshaller
                    .unmarshal(xsr, GetTransactionStatusResponse.class).getValue(); // JAXBElement

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);
            GetTransactionStatusGWResponse dataObject = new GetTransactionStatusGWResponse();
            jsonObject.setChannelErrorCode(transactionStatusResponse.getErrorObject().getErrorCode());
            jsonObject.setChannelMessage(transactionStatusResponse.getErrorObject().getMessage());
            dataObject.setListTrans(transactionStatusResponse.getListTrans());
            jsonObject.setData(dataObject);

            pgLogChannelFunctionService
                    .writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, true);
            if (StringUtils.equals(transactionStatusResponse.getErrorObject().getErrorCode(),
                    VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION_RESPONSE_STATUS_CODE_SUCCESS)) {
                PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                if (dataObject.getListTrans() != null && dataObject.getListTrans().size() > 0) {
                    for (ListTransactionStatusResponse transactionStatus : dataObject.getListTrans()) {

                        PaymentDTO paymentDTO = new PaymentDTO();
                        paymentDTO.setChannelId(channelFunction.getChannel().getId());
                        paymentDTO.setAmount(transactionStatus.getAmount());

                        if (StringUtils.isNotBlank(transactionStatus.getBank_status())) {
                            paymentDTO.setChannelTransactionStatus(
                                    PaymentConst.EnumBankStatus.valueOf(transactionStatus.getBank_status()).code());
                        } else {
                            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.EMPTY.code());
                        }

                        paymentDTO.setMerchantTransactionId(transactionStatus.getClient_transaction_id());
                        paymentDTO.setChannelTransactionId(transactionStatus.getTransaction_id());

                        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + soapMessageContent);

                        paymentService.updateChannelTransactionStatusPayment(paymentDTO);
                    }
                }

                paramsLog = new String[]{strMsg};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                        VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, false,
                        true, paramsLog));
            } else {
                PGResponse prefixResult = commonResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                paramsLog = new String[]{strMsg};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                        VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false,
                        true, paramsLog));
            }

            logger.info(commonLogService
                    .createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                            VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));

            return new ResponseEntity<>(jsonObject, HttpStatus.OK);

        } catch (SOAPException e) {
            pgLogChannelFunctionService
                    .writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION,
                            false);

            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                    VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false,
                    true, paramsLog));
            logger.info(commonLogService
                    .createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                            VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));

            return commonResponseService.returnChannelBadRequest(e.getMessage());
        } catch (Exception e) {

            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                    VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false,
                    true, paramsLog));
            logger.info(commonLogService
                    .createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                            VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));

            return commonResponseService.returnBadGateway();
        }
    }

    @Override
    public ResponseEntity<?> getHistoryTransaction(PGRequest pgRequest) {

        String[] paramsLog = null;
        try {
            logger.info(
                    commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                            VIBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));

            ChannelFunction channelFunction = channelFunctionService
                    .findChannelFunctionByCodeAndChannelCode(VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION,
                            VIBConst.CHANNEL_CODE);

            PaymentAccount paymentAccount = paymentAccountService
                    .getPaymentAccountByUserCodeAndChannelId(VIBConst.PG_USER_CODE,
                            channelFunction.getChannel().getId());

            GetTransactionHistoryVIBRequets transactionHistoryRequets = objectMapper
                    .readValue(pgRequest.getData(), GetTransactionHistoryVIBRequets.class);

            if (StringUtils
                    .isAllBlank(transactionHistoryRequets.getFrom_date(), transactionHistoryRequets.getTo_date())) {
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                String tomorrow = new SimpleDateFormat("dd/MM/yyyy").format(MyDateUtil.getTomorow());
                transactionHistoryRequets.setFrom_date(date);
                transactionHistoryRequets.setTo_date(tomorrow);
            }

            if (StringUtils.isBlank(transactionHistoryRequets.getC_d())) {
                transactionHistoryRequets.setC_d(StringUtils.EMPTY);
            }

            RqTransactionHistoryInqObject rqObject = new RqTransactionHistoryInqObject();
            rqObject.client_no = paymentAccount.getCif();
            rqObject.i_from_date = transactionHistoryRequets.getFrom_date();
            rqObject.i_account_no = transactionHistoryRequets.getAccount_no();
            rqObject.i_page_num = transactionHistoryRequets.getPage_num();
            rqObject.i_page_size = transactionHistoryRequets.getPage_size();
            rqObject.i_cr_dr = transactionHistoryRequets.getC_d(); // D, C or empty
            rqObject.RequestId = StringUtils.EMPTY;
            rqObject.i_to_date = transactionHistoryRequets.getTo_date();
            rqObject.client_id = paymentAccount.getUsername();

            String dataToSign = VIBSignature.SHA1TransactionHistory(rqObject);
            String signature = VIBSignature.genSignature(dataToSign);

            String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vn=\"http://vn.vib.fastpay.erp\" xmlns:xsd=\"http://object.vn.vib.fastpay.erp/xsd\">\r\n"
                    + "   <soapenv:Header/>\r\n"
                    + "   <soapenv:Body>\r\n"
                    + "      <vn:getTransHis>\r\n"
                    + "         <vn:rqObj>\r\n"
                    + "            <xsd:checkSum></xsd:checkSum>\r\n"
                    + "            <xsd:client_id>" + rqObject.client_id + "</xsd:client_id>\r\n"
                    + "            <xsd:client_no>" + rqObject.client_no + "</xsd:client_no>\r\n"
                    + "            <xsd:i_account_no>" + rqObject.i_account_no + "</xsd:i_account_no>\r\n"
                    + "            <xsd:i_cr_dr>" + rqObject.i_cr_dr + "</xsd:i_cr_dr>\r\n"
                    + "            <xsd:i_from_date>" + rqObject.i_from_date + "</xsd:i_from_date>\r\n"
                    + "            <xsd:i_page_num>" + rqObject.i_page_num + "</xsd:i_page_num>\r\n"
                    + "            <xsd:i_page_size>" + rqObject.i_page_size + "</xsd:i_page_size>\r\n"
                    + "            <xsd:i_to_date>" + rqObject.i_to_date + "</xsd:i_to_date>\r\n"
                    + "            <xsd:requestID></xsd:requestID>\r\n"
                    + "            <xsd:signData>" + signature + "</xsd:signData>\r\n"
                    + "         </vn:rqObj>\r\n"
                    + "      </vn:getTransHis>\r\n"
                    + "   </soapenv:Body>\r\n"
                    + "</soapenv:Envelope>";

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(),
                    new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8"))));

            soapMessage.saveChanges();

            String endpointUrl = RequestUtil
                    .createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                            channelFunction.getUrl());

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            paramsLog = new String[]{body};
            logger.info(commonLogService
                    .createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                            VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION,
                            true, true,
                            false, paramsLog));

            disableSslVerification();
            SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);

            soapResponse.writeTo(System.out); // Print response

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            String strMsg = new String(out.toByteArray());
            XMLInputFactory xif = XMLInputFactory.newFactory();
            InputStream is = new ByteArrayInputStream(strMsg.getBytes(Charsets.UTF_8));
            InputStreamReader reader = new InputStreamReader(is);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
            xsr.nextTag(); //getAccountBalanceResponse
            xsr.nextTag(); //return
//        	xsr.nextTag(); //acctRec

            JAXBContext jc = JAXBContext.newInstance(GetTransactionHistoryResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            GetTransactionHistoryResponse transactionStatusResponse = unmarshaller
                    .unmarshal(xsr, GetTransactionHistoryResponse.class).getValue(); // JAXBElement

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);
            GetTransactionHistoryGWResponse dataObject = new GetTransactionHistoryGWResponse();
            jsonObject.setChannelErrorCode(transactionStatusResponse.getErrorObject().getErrorCode());
            jsonObject.setChannelMessage(transactionStatusResponse.getErrorObject().getMessage());
            dataObject.setListTrans(transactionStatusResponse.getListTrans());
            dataObject.setTotal_record(CollectionUtils.size(transactionStatusResponse.getListTrans()));
            jsonObject.setData(dataObject);

            pgLogChannelFunctionService
                    .writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION,
                            true);
            if (StringUtils.equals(transactionStatusResponse.getErrorObject().getErrorCode(),
                    VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION_RESPONSE_STATUS_CODE_SUCCESS)) {
                PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                paramsLog = new String[]{strMsg};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                        VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, true, false,
                        true, paramsLog));

            } else {
                PGResponse prefixResult = commonResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                paramsLog = new String[]{strMsg};
                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                        VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false, false,
                        true, paramsLog));
            }

            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                    VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false));

            return new ResponseEntity<>(jsonObject, HttpStatus.OK);

        } catch (SOAPException e) {
            pgLogChannelFunctionService
                    .writeLogChannelFunction(VIBConst.CHANNEL_CODE, VIBConst.CHANNEL_FUNCTION_HISTORY_TRANSACTION,
                            false);

            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService
                    .createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION,
                            false, false,
                            true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                    VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false));

            return commonResponseService.returnChannelBadRequest(e.getMessage());
        } catch (Exception e) {

            paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService
                    .createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT, VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION,
                            false, false,
                            true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
                    VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION, false));

            return commonResponseService.returnBadGateway();
        }

    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (KeyManagementException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
    }
}
