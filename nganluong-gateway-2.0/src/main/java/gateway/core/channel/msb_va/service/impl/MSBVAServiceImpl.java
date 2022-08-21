package gateway.core.channel.msb_va.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.msb_offus.dto.MSBOFFUSConstant;
import gateway.core.channel.msb_va.dto.MSBVAContants;
import gateway.core.channel.msb_va.dto.obj.ReqAccountVA;
import gateway.core.channel.msb_va.dto.obj.ResAccountVA;
import gateway.core.channel.msb_va.dto.request.CreateUpdateVAReq;
import gateway.core.channel.msb_va.dto.request.HistoryTransReq;
import gateway.core.channel.msb_va.dto.request.LoginReq;
import gateway.core.channel.msb_va.dto.request.MSBNotifyReq;
import gateway.core.channel.msb_va.dto.response.CreateUpdateVARes;
import gateway.core.channel.msb_va.dto.response.HistoryTransRes;
import gateway.core.channel.msb_va.dto.response.LoginRes;
import gateway.core.channel.msb_va.service.MSBVAService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.NLVARequest;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.VirtualAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.service.VirtualAccountService;
import vn.nganluong.naba.utils.RequestUtil;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sonln
 */
@Service
public class MSBVAServiceImpl implements MSBVAService {
    private static final Logger logger = LogManager.getLogger(MSBVAServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final CommonLogService commonLogService;
    private final VirtualAccountService virtualAccountService;
    private final PaymentService paymentService;

    @Autowired
    public MSBVAServiceImpl(CommonLogService commonLogService,
                            VirtualAccountService virtualAccountService,
                            PaymentService paymentService){
        this.commonLogService = commonLogService;
        this.virtualAccountService = virtualAccountService;
        this.paymentService = paymentService;
    }

    @Override
    public PGResponse loginMSB(ChannelFunction channelFunction) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        log = new String[]{"START"};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_LOGIN, true, true, false, log));
        try {
            LoginReq loginReq = new LoginReq(MSBVAContants.MID, MSBVAContants.TID);
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String reqMSB = objectMapper.writeValueAsString(loginReq);
            log = new String[]{"REQUEST: " + url + "- BODY: " + reqMSB};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_LOGIN, true, true, false, log));
            String response = sendRequest(url, reqMSB);
            log = new String[]{"RESPONSE: " + response};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_LOGIN, true, true, false, log));
            LoginRes loginRes = objectMapper.readValue(response, LoginRes.class);
            pgResponse = PGResponse.getInstanceFullValue(
                    true,
                    loginRes.getRespDomain(),
                    loginRes.getRespMessage().getRespCode(),
                    MSBVAContants.getErrorMessage(loginRes.getRespMessage().getRespCode()),
                    PGResponse.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log = new String[]{"ERROR: " + e.getMessage()};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_LOGIN, true, true, false, log));
        }
        log = new String[]{"END: " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_LOGIN, true, true, false, log));
        return pgResponse;
    }

    @Override
    public PGResponse getTransactionHistory(ChannelFunction channelFunction, String req) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        log = new String[]{"START: " + req};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_TRANSACTION_HISTORY, true, true, false, log));
        try {
            HistoryTransReq historyTransReq = objectMapper.readValue(req, HistoryTransReq.class);
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String reqMSB = objectMapper.writeValueAsString(historyTransReq);
            log = new String[]{"REQUEST: " + url + " - BODY: " + reqMSB};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_TRANSACTION_HISTORY, true, true, false, log));
            String response = sendRequest(url, reqMSB);
            log = new String[]{"RESPONSE: " + response};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_TRANSACTION_HISTORY, true, true, false, log));
            HistoryTransRes historyTransRes = objectMapper.readValue(response, HistoryTransRes.class);
            pgResponse = PGResponse.getInstanceFullValue(
                    true,
                    historyTransRes.getRespDomain(),
                    historyTransRes.getRespMessage().getRespCode(),
                    MSBVAContants.getErrorMessage(historyTransRes.getRespMessage().getRespCode()),
                    PGResponse.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log = new String[]{"ERROR: " + e.getMessage()};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_TRANSACTION_HISTORY, true, true, false, log));
        }
        log = new String[]{"END: " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_TRANSACTION_HISTORY, true, true, false, log));
        return pgResponse;
    }

    @Override
    public PGResponse createMSBVA(ChannelFunction channelFunction, String req) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        log = new String[]{"START: " + req};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_CREATE_VA, true, true, false, log));
        try {
            CreateUpdateVAReq createUpdateVAReq = objectMapper.readValue(req, CreateUpdateVAReq.class);
            for (ReqAccountVA reqAccountVA : createUpdateVAReq.getRows()) {
                String checkValue = reqAccountVA.checkNullOrEmpty();
                if (!checkValue.isEmpty()) {
                    return PGResponse.getInstance(PGResponse.DATA_INVALID, null, null, checkValue);
                }
            }
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String reqMSB = objectMapper.writeValueAsString(createUpdateVAReq);
            log = new String[]{"REQUEST: " + url + "- BODY: " + reqMSB};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_CREATE_VA, true, true, false, log));
            String response = sendRequest(url, reqMSB);
            log = new String[]{"RESPONSE: " + response};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_CREATE_VA, true, true, false, log));
            CreateUpdateVARes createUpdateVARes = objectMapper.readValue(response, CreateUpdateVARes.class);
            if (createUpdateVARes.getRespDomain() != null) {
                createUpdateVARes.getRespDomain().getRows().forEach((account) -> {
                    account.setResponseDesc(MSBVAContants.getErrorMessage(account.getResponseCode()));
                    if (account.getResponseCode().equals("0")) GwCreateVA(account, channelFunction);
                });
            }
            pgResponse = PGResponse.getInstanceFullValue(
                    true,
                    createUpdateVARes.getRespDomain(),
                    createUpdateVARes.getRespMessage().getRespCode(),
                    MSBVAContants.getErrorMessage(createUpdateVARes.getRespMessage().getRespCode()),
                    PGResponse.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log = new String[]{"ERROR: " + e.getMessage()};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_CREATE_VA, true, true, false, log));
        }
        log = new String[]{"END: " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_CREATE_VA, true, true, false, log));
        return pgResponse;
    }

    @Override
    public PGResponse updateMSBVA(ChannelFunction channelFunction, String req) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        log = new String[]{"START: " + req};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_UPDATE_VA, true, true, false, log));
        try {
            CreateUpdateVAReq createUpdateVAReq = objectMapper.readValue(req, CreateUpdateVAReq.class);
            for (ReqAccountVA updateAcc : createUpdateVAReq.getRows()) {
                String checkValue = updateAcc.checkNullOrEmpty();
                if (!checkValue.isEmpty()) {
                    return PGResponse.getInstance(PGResponse.DATA_INVALID, null, null, checkValue);
                }
            }
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String reqMSB = objectMapper.writeValueAsString(createUpdateVAReq);
            log = new String[]{"REQUEST: " + url + "- BODY: " + reqMSB};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_UPDATE_VA, true, true, false, log));
            String response = sendRequest(url, reqMSB);
            log = new String[]{"RESPONSE: " + response};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_UPDATE_VA, true, true, false, log));
            CreateUpdateVARes createUpdateVARes = objectMapper.readValue(response, CreateUpdateVARes.class);
            if (createUpdateVARes.getRespDomain() != null) {
                createUpdateVARes.getRespDomain().getRows().forEach((account -> {
                    account.setResponseDesc(MSBVAContants.getErrorMessage(account.getResponseCode()));
                    if (account.getResponseCode().equals("0")) {
                        VirtualAccount virtualAccount = virtualAccountService.findVirtualAccount(account.getAccountNumber());
                        virtualAccount.setVirtualAccountNo(account.getAccountNumber());
                        virtualAccount.setVirtualAccountName(account.getName());
                        virtualAccount.setPhoneNumber(account.getPhone());
                        virtualAccount.setDescription(account.getDetail1());
                        if (account.getStatus().equals("1")) {
                            virtualAccount.setEnable(true);
                            virtualAccount.setStatus(true);
                        } else {
                            virtualAccount.setStatus(false);
                            virtualAccount.setEnable(false);
                        }
                        virtualAccountService.updateVirtualAccount(virtualAccount);
                    }
                }));
            }
            pgResponse = PGResponse.getInstanceFullValue(
                    true,
                    createUpdateVARes.getRespDomain(),
                    createUpdateVARes.getRespMessage().getRespCode(),
                    MSBVAContants.getErrorMessage(createUpdateVARes.getRespMessage().getRespCode()),
                    PGResponse.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log = new String[]{"ERROR: " + e.getMessage()};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_UPDATE_VA, true, true, false, log));
        }
        log = new String[]{"END: " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                MSBVAContants.FNC_UPDATE_VA, true, true, false, log));
        return pgResponse;
    }

    @Override
    public void MSBVANotify(String req) {
        String[] log;
        try {
            MSBNotifyReq msbNotifyReq = objectMapper.readValue(req, MSBNotifyReq.class);
            log = new String[]{"START: " + objectMapper.writeValueAsString(msbNotifyReq)};
            logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_MSB_VA_NOTIFY, true, true, false, log));
            String plainText = MSBOFFUSConstant.ACCESS_CODE +
                    msbNotifyReq.getTranSeq() + msbNotifyReq.getTranDate() + msbNotifyReq.getVaNumber() +
                    msbNotifyReq.getTranAmount() + msbNotifyReq.getFromAccountNumber() + msbNotifyReq.getToAccountNumber();
            String secureCode = PGSecurity.sha256(plainText);
            if (!secureCode.equals(msbNotifyReq.getSignature())) {
                log = new String[]{"VERIFY FAILED: " + msbNotifyReq.getTranSeq()};
                logger.error(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                        MSBVAContants.FNC_MSB_VA_NOTIFY, true, true, false, log));
                return;
            }
            //push to app
            NLVARequest newRequest = NLVARequest.parse(msbNotifyReq, "MSB");

            Map<String, Object> params = new HashMap<>();
            params.put("bank_transaction_id", newRequest.getBankTransactionId());
            params.put("bank_account", newRequest.getBankAccount());
            params.put("transaction_amount", newRequest.getTransactionAmount());
            params.put("cashin_id", newRequest.getCashinId());
            params.put("bank_code", newRequest.getBankCode());
            params.put("bank_time", newRequest.getTransactionDate());
            params.put("from_account_name", newRequest.getFromAccountName());
            params.put("from_account_number", newRequest.getFromAccountNumber());
            params.put("description", newRequest.getDescription());

            String dataInput = new JSONObject(params).toString();
            Map<String, Object> paramsBig = new HashMap<>();
            paramsBig.put("status", true);
            paramsBig.put("error_code", "00");
            paramsBig.put("message", req);
            paramsBig.put("data", dataInput);
            paramsBig.put("checksum", PGSecurity.sha256(dataInput + PGSecurity.WITH_NGANLUONG_CALLBACK));
            logger.info("REQUEST APP " + paramsBig);
            String url = NLVARequest.APP_NOTIFY_URL;
            if(newRequest.getDescription().contains("VA")){
                url = NLVARequest.APP_NOTIFY_URL_UAT;
            }
            String response = HttpUtil.send(url, paramsBig);
            logger.info(response);
            String vaNumber = msbNotifyReq.getVaNumber();
            VirtualAccount virtualAccount = virtualAccountService.findVirtualAccount(vaNumber);

            if(virtualAccount != null) {
                PaymentDTO payment = new PaymentDTO();
                payment.setChannelId(virtualAccount.getChannel().getId());
                payment.setMerchantTransactionId(msbNotifyReq.getTranSeq());
                payment.setVirtualAccountNo(msbNotifyReq.getVaNumber());
                payment.setAmount(msbNotifyReq.getTranAmount());
                payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                payment.setRawRequest(req);
                paymentService.createPayment(payment);
                log = new String[]{"SAVE PAYMENT : " + objectMapper.writeValueAsString(payment)};
                logger.info(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                        MSBVAContants.FNC_MSB_VA_NOTIFY, true, true, false, log));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log = new String[]{"ERROR : " + e.getMessage()};
            logger.error(commonLogService.createContentLog(MSBVAContants.CHANNEL_CODE, MSBVAContants.SERVICE,
                    MSBVAContants.FNC_MSB_VA_NOTIFY, true, true, false, log));
        }

    }

    private void GwCreateVA(ResAccountVA accountVA, ChannelFunction channelFunction) {
        VirtualAccountDto virtualAccountDto = new VirtualAccountDto();
        virtualAccountDto.setChannelId(channelFunction.getChannel().getId());
        virtualAccountDto.setVirtualAccountNo(accountVA.getAccountNumber());
        virtualAccountDto.setVirtualAccountName(accountVA.getName());
        virtualAccountDto.setMerchantCode(channelFunction.getCode());
        virtualAccountDto.setPhoneNumber(accountVA.getPhone());
        virtualAccountDto.setDescription(accountVA.getDetail1());
        virtualAccountService.createVirtualAccount(virtualAccountDto);
    }

    private String sendRequest(String url, String bodyRequest)
            throws KeyManagementException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(bodyRequest, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException exx) {
            response = new ResponseEntity<>(exx.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
            return response.getBody();
        }
    }
}
