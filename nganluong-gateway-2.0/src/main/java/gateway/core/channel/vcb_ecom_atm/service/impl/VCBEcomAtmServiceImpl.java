package gateway.core.channel.vcb_ecom_atm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.vcb_ecom_atm.VCBEcomAtmSecurity;
import gateway.core.channel.vcb_ecom_atm.dto.VCBEcomAtmConstants;
import gateway.core.channel.vcb_ecom_atm.dto.req.*;
import gateway.core.channel.vcb_ecom_atm.dto.res.*;
import gateway.core.channel.vcb_ecom_atm.service.VCBEcomAtmService;
import gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3Locator;
import gateway.core.channel.vcb_ecom_atm.ws_client.Payment_Service_Card_V3SoapStub;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class VCBEcomAtmServiceImpl extends PaymentGate implements VCBEcomAtmService {
    private static final Logger logger = LogManager.getLogger(VCBEcomAtmServiceImpl.class);

    @Override
    public PGResponse VerifyCard(String inputStr) throws Exception {
        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        VerifyPaymentReq data = new VerifyPaymentReq();
        buildCommonParam(data, input);
        data.setLanguage("VN");
        data.setCardNumber(input.getCardNumber());
        data.setCardHolderName(input.getCardHolerName().toUpperCase());
        data.setCardDate(input.getCardIssueDate());
        data.setAddInfo(StringUtils.defaultString(input.getDescription(), DEFAULT_VALUE));
        data.setClientIp(input.getClientIp());

        // CALCULATE TIME REQUEST - REPONSE
        long startTimeRequest = System.currentTimeMillis();
        Payment_Service_Card_V3SoapStub proxy = getProxy();

        String vcbRes = "";
        try {
            vcbRes = proxy.verifyCardAndSendOTP(buildRootRequest(data));
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3. VCB ECOM RESPONSE ", data.getTransactionId() + " | " + vcbRes + " | EXEC_TIME: " + execTime);
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3 VCB ECOM VERIFY CARD - EXCEPTION: ", data.getTransactionId() + " | EXEC_TIME: " + execTime);
            logger.info(ExceptionUtils.getStackTrace(e));
        }

        return buildResponse(data, vcbRes, VerifyPaymentRes.class);
    }

    @Override
    public PGResponse VerifyOtp(String inputStr) throws Exception {
        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        VerifyOtpReq data = new VerifyOtpReq();
        buildCommonParam(data, input);

        data.setCardNumber(input.getCardNumber());
        data.setCardHolderName(input.getCardHolerName().toUpperCase());
        data.setCardDate(input.getCardIssueDate());

        data.setTransactionId(input.getTransId());
        data.setOtp(input.getOtp());
        data.setHashCode(input.getHashCode());
        // CALCULATE TIME REQUEST - REPONSE
        long startTimeRequest = System.currentTimeMillis();

        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbRes = "";
        try {
            vcbRes = proxy.verifyOTPAndPayment(buildRootRequest(data));
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3. VCB ECOM RESPONSE ", data.getTransactionId() + " | " + vcbRes+ " | EXEC_TIME: " + execTime);
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3 VCB ECOM VERIFY OTP - EXCEPTION: ", data.getTransactionId() + " | EXEC_TIME: " + execTime);
            logger.info(ExceptionUtils.getStackTrace(e));
        }

        return buildResponse(data, vcbRes, VerifyOtpRes.class);
    }

    @Override
    public PGResponse Query(String inputStr) throws Exception {
        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        QueryTransReq data = new QueryTransReq();
        buildCommonParam(data, input);
        data.setQueryTranId(input.getInquiryTransId());

        // CALCULATE TIME REQUEST - REPONSE
        long startTimeRequest = System.currentTimeMillis();
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbRes = "";
        try {
            vcbRes = proxy.query(buildRootRequest(data));
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3. VCB ECOM RESPONSE ", data.getTransactionId() + " | " + vcbRes + " | EXEC_TIME: " + execTime);
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3 VCB ECOM QUERY - EXCEPTION: ", data.getTransactionId() + " | EXEC_TIME: " + execTime);
            logger.info(ExceptionUtils.getStackTrace(e));
        }

        return buildResponse(data, vcbRes, QueryTransRes.class);
    }

    @Override
    public PGResponse Refund(String inputStr) throws Exception {

        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        RefundReq data = new RefundReq();
        data.setRefundSources("ECOM_CARD");
        data.setRefundTranId(input.getRefTransId());	// Mã req refund
        data.setTransactionId(input.getTransId());		// Mã gd gốc
        data.setRefundAmount(df.format(input.getAmount()));

//		data.setMerchantID(VCBEcomAtmConstants.VCB_ECOM_MERCHANT_ID);
        data.setMerchantID(!Strings.isNullOrEmpty(input.getMerchantId()) ? input.getMerchantId() : VCBEcomAtmConstants.VCB_ECOM_MID);
//		data.setMid(VCBEcomAtmConstants.VCB_ECOM_MID);
        data.setMid(!Strings.isNullOrEmpty(input.getMerchantId()) ? input.getMerchantId() : VCBEcomAtmConstants.VCB_ECOM_MID);

        data.setCurrencyCode("VND");
        data.setPartnerID(VCBEcomAtmConstants.VCB_ECOM_PARTNER_ID);
        data.setPartnerPassword(VCBEcomAtmConstants.VCB_ECOM_PASSWORD);

        // CALCULATE TIME REQUEST - REPONSE
        long startTimeRequest = System.currentTimeMillis();
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbRes = "";
        try {
            vcbRes = proxy.refundOffline(buildRootRequest(data));
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3. VCB ECOM RESPONSE ", data.getTransactionId() + " | " + vcbRes + " | EXEC_TIME: " + execTime);
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3 VCB ECOM REFUND - EXCEPTION: ", data.getTransactionId() + " | EXEC_TIME: " + execTime);
            logger.info(ExceptionUtils.getStackTrace(e));
        }

        return buildResponse(data, vcbRes, RefundRes.class);
    }

    //---------------------------------------------------------------------------------------------------------

    @Override
    public PGResponse VerifyCard2(String inputStr) throws Exception {
        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        VerifyPaymentReq data = new VerifyPaymentReq();
        buildCommonParam2(data, input);
        data.setLanguage("VN");
        data.setCardNumber(input.getCardNumber());
        data.setCardHolderName(input.getCardHolerName().toUpperCase());
        data.setCardDate(input.getCardIssueDate());
        data.setAddInfo(StringUtils.defaultString(input.getDescription(), DEFAULT_VALUE));
        data.setClientIp(input.getClientIp());

        // CALCULATE TIME REQUEST - REPONSE
        long startTimeRequest = System.currentTimeMillis();
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbRes = "";
        try {
            vcbRes = proxy.verifyCardAndSendOTP(buildRootRequest2(data));
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3. VCB ECOM RESPONSE ", data.getTransactionId() + " | " + vcbRes + " | EXEC_TIME: " + execTime);
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3 VCB ECOM VERIFY CARD 2 - EXCEPTION: ", data.getTransactionId() + " | EXEC_TIME: " + execTime);
            logger.info(ExceptionUtils.getStackTrace(e));
        }

        return buildResponse(data, vcbRes, VerifyPaymentRes.class);
    }

    @Override
    public PGResponse VerifyOtp2(String inputStr) throws Exception {
        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        VerifyOtpReq data = new VerifyOtpReq();
        buildCommonParam2(data, input);

        data.setCardNumber(input.getCardNumber());
        data.setCardHolderName(input.getCardHolerName().toUpperCase());
        data.setCardDate(input.getCardIssueDate());

        data.setTransactionId(input.getTransId());
        data.setOtp(input.getOtp());
        data.setHashCode(input.getHashCode());

        // CALCULATE TIME REQUEST - REPONSE
        long startTimeRequest = System.currentTimeMillis();
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbRes = "";
        try {
            vcbRes = proxy.verifyOTPAndPayment(buildRootRequest2(data));
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3. VCB ECOM RESPONSE ", data.getTransactionId() + " | " + vcbRes + " | EXEC_TIME: " + execTime);
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTimeRequest;
            WriteInfoLog("3 VCB ECOM VERIFY OTP 2 - EXCEPTION: ", data.getTransactionId() + " | EXEC_TIME: " + execTime);
            logger.info(ExceptionUtils.getStackTrace(e));
        }

        return buildResponse(data, vcbRes, VerifyOtpRes.class);
    }

    private void buildCommonParam2(BaseRequest req, DataRequest input) {
        req.setTransactionId(input.getTransId());
        req.setAmount(df.format(input.getAmount()));

        req.setCurrencyCode("VND");
        req.setMerchantId(input.getMerchantId());
        req.setPartnerId(input.getPartnerId());
        req.setPartnerPassword(input.getPartnerPassword());
    }

    private String buildRootRequest2(BaseRequest data) throws JsonProcessingException, Exception {
        RootRequest rootReq = new RootRequest();
        rootReq.setPartnerId(data.getPartnerId());
        rootReq.setTransactionID(data.getTransactionId());
        rootReq.setData(VCBEcomAtmSecurity.encryptAES2(objectMapper.writeValueAsString(data)));

        String rawData = rootReq.getPartnerId() + rootReq.getTransactionID() + rootReq.getData();
        rootReq.setSignature(VCBEcomAtmSecurity.sign(rawData));
        WriteInfoLog("2. GATEWAY REQUEST ", data.getTransactionId() + " | " + objectMapper.writeValueAsString(data));
        WriteInfoLog("2. REQUEST TO VCB ECOM ATM ", data.getTransactionId() + " | " + objectMapper.writeValueAsString(rootReq));
        return objectMapper.writeValueAsString(rootReq);
    }

    //--------------------------------------------------------------------------------------------------------------------------

    private String buildRootRequest(BaseRequest data) throws JsonProcessingException, Exception {
        RootRequest rootReq = new RootRequest();
        rootReq.setPartnerId(VCBEcomAtmConstants.VCB_ECOM_PARTNER_ID);
        rootReq.setTransactionID(data.getTransactionId());
        rootReq.setData(VCBEcomAtmSecurity.encryptAES2(objectMapper.writeValueAsString(data)));

        String rawData = rootReq.getPartnerId() + rootReq.getTransactionID() + rootReq.getData();
        rootReq.setSignature(VCBEcomAtmSecurity.sign(rawData));
        WriteInfoLog("2. GATEWAY REQUEST ", data.getTransactionId() + " | " + objectMapper.writeValueAsString(data));
        WriteInfoLog("2. REQUEST TO VCB ECOM ATM ", data.getTransactionId() + " | " + objectMapper.writeValueAsString(rootReq));
        return objectMapper.writeValueAsString(rootReq);
    }


    /**
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    private void buildCommonParam(BaseRequest req, DataRequest input) {
        req.setTransactionId(input.getTransId());
        req.setAmount(df.format(input.getAmount()));

        req.setCurrencyCode("VND");
//		req.setMerchantId(VCBEcomAtmConstants.VCB_ECOM_MERCHANT_ID);
        req.setMerchantId(!Strings.isNullOrEmpty(input.getMerchantId()) ? input.getMerchantId() : VCBEcomAtmConstants.VCB_ECOM_MID);
        req.setPartnerId(VCBEcomAtmConstants.VCB_ECOM_PARTNER_ID);
        req.setPartnerPassword(VCBEcomAtmConstants.VCB_ECOM_PASSWORD);
    }

    private <T extends BaseResponse> PGResponse buildResponse(BaseRequest req, String res, Class<T> claszzRes)
            throws Exception {

        RootResponse rootRes = objectMapper.readValue(res, RootResponse.class);
        T vcbRes = objectMapper.readValue(VCBEcomAtmSecurity.decryptAES2(rootRes.getData()), claszzRes);
        vcbRes.setTransId(req.getTransactionId());

        PGResponse pgRes = new PGResponse();
        pgRes.setStatus(true);
        pgRes.setErrorCode(vcbRes.getErrCode());
        pgRes.setMessage(VCBEcomAtmConstants.getResMsg(vcbRes.getErrCode()));
        pgRes.setData(objectMapper.writeValueAsString(vcbRes));

        //return objectMapper.writeValueAsString(pgRes);
        return pgRes;
    }

    private Payment_Service_Card_V3SoapStub getProxy() throws MalformedURLException, ServiceException {
        Payment_Service_Card_V3Locator locator = new Payment_Service_Card_V3Locator();

        //URL url = new URL(getPaymentChannel().getUrl());
        URL url = new URL(VCBEcomAtmConstants.URL_API_CALL_VCB);
        Payment_Service_Card_V3SoapStub stub = (Payment_Service_Card_V3SoapStub) locator
                .getPayment_Service_Card_V3Soap(url);
        return stub;
    }
}
