package gateway.core.channel.bidv_ecom.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.bidv_ecom.dto.*;
import gateway.core.channel.bidv_ecom.safety.BidvEcomSecurity;
import gateway.core.channel.bidv_ecom.service.BIDVEcomService;
import gateway.core.channel.bidv_ecom.ws.bidvws.NCCPortType;
import gateway.core.channel.bidv_ecom.ws.bidvws.NCCWSDL;
import gateway.core.channel.bidv_ecom.ws.nccinput_schema.RootNccInput;
import gateway.core.channel.bidv_ecom.ws.nccoutput_schema.RootNccOutput;
import gateway.core.dto.PGResponse;
import gateway.core.util.HttpUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sonln@nganluong.vn
 */

@Service
public class BIDVEcomServiceImpl implements BIDVEcomService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(BIDVEcomServiceImpl.class);
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public PGResponse inittrans(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, String req) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        RootNccOutput rootOutput = new RootNccOutput();
        try {
            BidvEcomRequest bidvEcomRequest = objectMapper.readValue(req, BidvEcomRequest.class);
            //create payment
            PaymentDTO payment = new PaymentDTO();
            payment.setChannelId(channelFunction.getChannel().getId());
            payment.setPgFunctionId(channelFunction.getId());
            payment.setMerchantCode(paymentAccount.getMerchantId());
            payment.setMerchantName(paymentAccount.getMerchantName());
            payment.setMerchantTransactionId(bidvEcomRequest.getTransId());
            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setAmount(bidvEcomRequest.getAmount());
            paymentService.createPayment(payment);


            RootNccInput reqBidv = new RootNccInput();
            reqBidv.setServiceId(BidvEcomConstants.SERVICE_ID);
            reqBidv.setMerchantId(BidvEcomConstants.MERCHANT_ID);
            reqBidv.setMerchantName(BidvEcomConstants.MERCHANT_NAME);
            reqBidv.setTransId(bidvEcomRequest.getTransId());
            reqBidv.setTransDesc(bidvEcomRequest.getTransDesc());
            reqBidv.setAmount(bidvEcomRequest.getAmount());
            reqBidv.setCurr(BidvEcomConstants.CURR);
            String[] arraySecureCode = {reqBidv.getServiceId(), reqBidv.getMerchantId(), reqBidv.getMerchantName(),
                    reqBidv.getTrandate(), reqBidv.getTransId(), reqBidv.getTransDesc(), reqBidv.getAmount(),
                    reqBidv.getCurr(), reqBidv.getPayerId(), reqBidv.getPayerName(), reqBidv.getPayerAddr(),
                    reqBidv.getType(), reqBidv.getCustmerId(), reqBidv.getCustomerName(), reqBidv.getIssueDate()};
            reqBidv.setSecureCode(BidvEcomSecurity.md5(arraySecureCode, BidvEcomConstants.KEY_ENCRYPT));
            // Call BIDV
            log = new String[]{"CALL BIDV " + objectMapper.writeValueAsString(reqBidv)};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_INIT_TRANS, true, true, false, log));
            URL url = new ClassPathResource("bidv/BIDVPaygateWS.wsdl").getURL();
            NCCWSDL service = new NCCWSDL(url);
            NCCPortType port = service.getNCCPortTypeEndpoint0();
            rootOutput = port.inittrans(reqBidv);

            //update rawRequets Bidv
            payment.setRawRequest(objectMapper.writeValueAsString(reqBidv));
            paymentService.updatePayment(payment);

            log = new String[]{"RESPONSE BIDV: " + objectMapper.writeValueAsString(rootOutput)};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_INIT_TRANS, true, true, false, log));
            pgResponse = PGResponse.getInstanceFullValue(true, objectMapper.readValue(objectMapper.writeValueAsString(rootOutput), BidvEcomResponse.class),
                    rootOutput.getResponseCode(), BidvEcomConstants.getErrorMessage(rootOutput.getResponseCode()), PGResponse.SUCCESS);
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            pgResponse = PGResponse.getExceptionMsg(e, null, rootOutput.getResponseCode(), BidvEcomConstants.getErrorMessage(rootOutput.getResponseCode()), null);
        }
        return pgResponse;
    }

    @Override
    public PGResponse verify(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, String req) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        RootNccOutput rootOutput = new RootNccOutput();
        PaymentDTO payment = new PaymentDTO();
        try {
            log = new String[]{"REQUEST " + req.replaceAll("[\n\r]", "")};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_VERIFY, true, true, false, log));
            BidvEcomRequest bidvEcomRequest = objectMapper.readValue(req, BidvEcomRequest.class);

            //create payment
            payment.setChannelId(channelFunction.getChannel().getId());
            payment.setPgFunctionId(channelFunction.getId());
            payment.setMerchantCode(paymentAccount.getMerchantId());
            payment.setMerchantName(paymentAccount.getMerchantName());
            payment.setMerchantTransactionId(bidvEcomRequest.getTransId());
            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setAmount(bidvEcomRequest.getAmount());
            paymentService.createPayment(payment);

            RootNccInput reqBidv = new RootNccInput();
            reqBidv.setServiceId(BidvEcomConstants.SERVICE_ID);
            reqBidv.setMerchantId(BidvEcomConstants.MERCHANT_ID);
            reqBidv.setMerchantName(BidvEcomConstants.MERCHANT_NAME);
            reqBidv.setTransId(bidvEcomRequest.getTransId());
            reqBidv.setTransDesc(bidvEcomRequest.getTransDesc());
            reqBidv.setAmount(bidvEcomRequest.getAmount());
            reqBidv.setCurr(BidvEcomConstants.CURR);
            reqBidv.setPayerId(bidvEcomRequest.getPayerId());
            reqBidv.setPayerName(bidvEcomRequest.getPayerName());
            reqBidv.setPayerAddr(bidvEcomRequest.getPayerAddr());
            reqBidv.setType(bidvEcomRequest.getType());
            reqBidv.setCustmerId(bidvEcomRequest.getCustomerId());
            reqBidv.setCustomerName(bidvEcomRequest.getCustomerName());
            reqBidv.setIssueDate(bidvEcomRequest.getIssuedate());
            String[] arraySecureCode = {reqBidv.getServiceId(), reqBidv.getMerchantId(), reqBidv.getMerchantName(),
                    reqBidv.getTrandate(), reqBidv.getTransId(), reqBidv.getTransDesc(), reqBidv.getAmount(),
                    reqBidv.getCurr(), reqBidv.getPayerId(), reqBidv.getPayerName(), reqBidv.getPayerAddr(),
                    reqBidv.getType(), reqBidv.getCustmerId(), reqBidv.getCustomerName(), reqBidv.getIssueDate()};
            reqBidv.setSecureCode(BidvEcomSecurity.md5(arraySecureCode, BidvEcomConstants.KEY_ENCRYPT));
            // Call BIDV
            log = new String[]{"CALL BIDV " + objectMapper.writeValueAsString(reqBidv)};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_VERIFY, true, true, false, log));
            URL url = new ClassPathResource("bidv/BIDVPaygateWS.wsdl").getURL();
            NCCWSDL service = new NCCWSDL(url);
            NCCPortType port = service.getNCCPortTypeEndpoint0();
            rootOutput = port.verify(reqBidv);
            //update rawRequets Bidv
            payment.setRawRequest(objectMapper.writeValueAsString(reqBidv));

            log = new String[]{"RESPONSE BIDV: " + objectMapper.writeValueAsString(rootOutput)};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_VERIFY, true, true, false, log));
            pgResponse = PGResponse.getInstanceFullValue(true, objectMapper.readValue(objectMapper.writeValueAsString(rootOutput), BidvEcomResponse.class),
                    rootOutput.getResponseCode(), BidvEcomConstants.getErrorMessage(rootOutput.getResponseCode()), PGResponse.SUCCESS);

        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            pgResponse = PGResponse.getExceptionMsg(e, null, rootOutput.getResponseCode(), BidvEcomConstants.getErrorMessage(rootOutput.getResponseCode()), null);
        }
        payment.setRawResponse(objectMapper.writeValueAsString(pgResponse));
        paymentService.updatePayment(payment);
        return pgResponse;
    }

    @Override
    public PGResponse inquiry(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, String req) throws JsonProcessingException {
        String[] log;
        PGResponse pgResponse = new PGResponse();
        RootNccOutput rootOutput = new RootNccOutput();
        try {
            log = new String[]{"REQUEST " + req.replaceAll("[\n\r]", "")};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_INQUIRY, true, true, false, log));
            BidvEcomRequest bidvEcomRequest = objectMapper.readValue(req, BidvEcomRequest.class);

            RootNccInput reqBidv = new RootNccInput();
            reqBidv.setServiceId(BidvEcomConstants.SERVICE_ID);
            reqBidv.setMerchantId(BidvEcomConstants.MERCHANT_ID);
            reqBidv.setMerchantName(BidvEcomConstants.MERCHANT_NAME);
            reqBidv.setTransId(bidvEcomRequest.getTransId());
            reqBidv.setTrandate(bidvEcomRequest.getTrandate());
            String[] arraySecureCode = {reqBidv.getServiceId(), reqBidv.getMerchantId(), reqBidv.getMerchantName(),
                    reqBidv.getTrandate(), reqBidv.getTransId(), reqBidv.getTransDesc(), reqBidv.getAmount(),
                    reqBidv.getCurr(), reqBidv.getPayerId(), reqBidv.getPayerName(), reqBidv.getPayerAddr(),
                    reqBidv.getType(), reqBidv.getCustmerId(), reqBidv.getCustomerName(), reqBidv.getIssueDate()};
            reqBidv.setSecureCode(BidvEcomSecurity.md5(arraySecureCode, BidvEcomConstants.KEY_ENCRYPT));
            // Call BIDV
            log = new String[]{"CALL BIDV " + objectMapper.writeValueAsString(reqBidv)};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_INQUIRY, true, true, false, log));
            URL url = new ClassPathResource("bidv/BIDVPaygateWS.wsdl").getURL();
            NCCWSDL service = new NCCWSDL(url);
            NCCPortType port = service.getNCCPortTypeEndpoint0();
            rootOutput = port.inquiry(reqBidv);
            log = new String[]{"RESPONSE BIDV: " + objectMapper.writeValueAsString(rootOutput)};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_INQUIRY, true, true, false, log));
            pgResponse = PGResponse.getInstanceFullValue(true, objectMapper.readValue(objectMapper.writeValueAsString(rootOutput), BidvEcomResponse.class),
                    rootOutput.getResponseCode(), BidvEcomConstants.getErrorMessage(rootOutput.getResponseCode()), PGResponse.SUCCESS);
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            pgResponse = PGResponse.getExceptionMsg(e, null, rootOutput.getResponseCode(), BidvEcomConstants.getErrorMessage(rootOutput.getResponseCode()), null);
        }
        return pgResponse;
    }

    @Override
    public BidvCallbackRes BidvNotify(String req) throws Exception {
        String[] log;
        BidvCallbackRes bidvCallbackRes = new BidvCallbackRes();
        String responseDesc;
        String responseCode;
        String redirectUrl;
        String secureCode;
        try {
            log = new String[]{"REQUEST BIDV NOTIFY" + req.replaceAll("[\n\r]", "")};
            logger.info(commonLogService.createContentLog(BidvEcomConstants.CHANNEL_CODE, BidvEcomConstants.SERVICE,
                    BidvEcomConstants.FNC_BIDV_NOTIFY, true, true, false, log));

            BidvCallbackReq bidvCallbackReq = objectMapper.readValue(req, BidvCallbackReq.class);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("fnc", "confirm");
            paramsMap.put("service_id", bidvCallbackReq.getServiceId());
            paramsMap.put("merchant_id", bidvCallbackReq.getMerchantId());
            paramsMap.put("amount", bidvCallbackReq.getAmount());
            paramsMap.put("trans_id", bidvCallbackReq.getTransId());
            paramsMap.put("result_code", bidvCallbackReq.getResultCode());
            paramsMap.put("bank_trans_id", bidvCallbackReq.getTxnCode());
            paramsMap.put("trace_number", bidvCallbackReq.getTraceNumber());
            paramsMap.put("more_info", bidvCallbackReq.getMoreInfo());
            String responseApp = HttpUtil.send(BidvEcomConstants.NL_URL_REDIRECT, paramsMap);
            logger.info("RESPONE APP BIDV NOTIFY" + responseApp);
            JSONObject json = new JSONObject(responseApp);
            responseCode = json.has("response_code") ? json.getString("response_code") : "096";
            responseDesc = json.has("response_desc") ? json.getString("response_desc") : "";
            redirectUrl = json.has("redirect_url") ? json.getString("redirect_url") : "https://www.nganluong.vn/";
            String[] arraySecureCode = {responseCode, responseDesc,
                    redirectUrl, bidvCallbackRes.getMoreInfo()};
            secureCode = BidvEcomSecurity.md5(arraySecureCode, BidvEcomConstants.KEY_ENCRYPT);
            //update payment
            Payment paymentCheck = paymentService.findByMerchantTransactionId(bidvCallbackReq.getTransId());
            if (paymentCheck != null) {
                paymentCheck.setRawResponse(req);
                if (responseCode.equals("000")) {
                    paymentCheck.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                }
                if (bidvCallbackReq.getResultCode().equals("000")) {
                    paymentCheck.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                    paymentCheck.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                }
                paymentService.updatePayment(paymentCheck);
            }
            bidvCallbackRes.setResponseCode(responseCode);
            bidvCallbackRes.setResponseDesc(responseDesc);
            bidvCallbackRes.setRedirectUrl(redirectUrl);
            bidvCallbackRes.setSecureCode(secureCode);
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            bidvCallbackRes.setResponseCode("096");
            bidvCallbackRes.setResponseDesc("Thất bại");
            bidvCallbackRes.setRedirectUrl("https://www.nganluong.vn/");
            String[] arraySecureCode = {bidvCallbackRes.getResponseCode(), bidvCallbackRes.getResponseDesc(),
                    bidvCallbackRes.getRedirectUrl(), bidvCallbackRes.getMoreInfo()};
            bidvCallbackRes.setSecureCode(BidvEcomSecurity.md5(arraySecureCode, BidvEcomConstants.KEY_ENCRYPT));
        }
        return bidvCallbackRes;
    }
}
