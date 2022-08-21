package gateway.core.channel.anbinhbank.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import gateway.core.channel.ChannelCommonConfig;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.anbinhbank.ABBankSecurity;
import gateway.core.channel.anbinhbank.ClientRequest;
import gateway.core.channel.anbinhbank.dto.ABBankConstants;
import gateway.core.channel.anbinhbank.dto.req.GetAccessTokenReq;
import gateway.core.channel.anbinhbank.dto.req.HeaderRequest;
import gateway.core.channel.anbinhbank.dto.req.RootRequest;
import gateway.core.channel.anbinhbank.dto.req.body.*;
import gateway.core.channel.anbinhbank.dto.res.GetAccessTokenRes;
import gateway.core.channel.anbinhbank.dto.res.RootResponse;
import gateway.core.channel.anbinhbank.dto.res.body.BaseResponse;
import gateway.core.channel.anbinhbank.dto.res.body.CheckBalanceRes;
import gateway.core.channel.anbinhbank.dto.res.body.CheckTransRes;
import gateway.core.channel.anbinhbank.dto.res.body.VerifyOtpRes;
import gateway.core.channel.anbinhbank.report.CreateReportNglAbb;
import gateway.core.channel.anbinhbank.service.ABBEcomService;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;

import javax.ws.rs.core.MediaType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

@Service
public class ABBEcomServiceImpl extends PaymentGate implements ABBEcomService {

    private static final Logger logger = LogManager.getLogger(ABBEcomServiceImpl.class);
    private static final String SERVICE_NAME = "IB ON";

    // https://10.2.9.50:9443/abb/sandbox
    // https://apigw-abbdev.abbank.vn:9443/abb/sandbox
    // public https://apigw-abbdev.abbank.vn/api/sandbox

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonLogService commonLogService;

    private static String SESSION_FILE_PATH = "/var/lib/payment_gateway_test/key/an_binh_bank/";
    private static File fileSession = null;
    private int MAX_TIME_LOGIN = 3;

    //	String PARTNER_ID = "VIMO";
    //Key Doi Soat: ABBVIMO2019ABBVIMO2019ABBVIMO2019
    String CLIENT_ID = "99291dbc79c363fe28b69b48273396bf";//98301196eb3cbfc1234c2347c8f7c7e1
    String CLIENT_SECRET = "535f95eaf71ae61f965f0cba0dc48313";//98a32d476b629aed538fe8ead8bd0ec0
    String SCOPE = "wallets payments verifications";
    String GRANT_TYPE = "client_credentials";

    // http status 200, 201 có out body -> can xac thuc signature
    // con lai ko can

    @Override
    public PGResponse PaymentWithBankAcc(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException, KeyManagementException, NoSuchAlgorithmException, ParseException {
        PayWithBankAccReq body = objectMapper.readValue(inputStr, PayWithBankAccReq.class);
        RootRequest rootReq = buildRootReq(paymentAccount, body);
        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, inputStr, body);
        // Write log before request
        String[] paramsLog = new String[]{inputStr};
        logger.info(commonLogService.createContentLog(ABBankConstants.CHANNEL_CODE, SERVICE_NAME,
                ABBankConstants.FUNCTION_CODE_PAYMENT_WITH_BANK_ACC, true, true, false, paramsLog));
        return callApi(rootReq, ABBankConstants.PAY_WITH_BANK_ACC, new TypeReference<BaseResponse>() {
        }, paymentDTO);
    }

    @Override
    public PGResponse VerifyOtp(PaymentAccount paymentAccount, String inputStr) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        VerifyOtpReq body = objectMapper.readValue(inputStr, VerifyOtpReq.class);

        RootRequest rootReq = buildRootReq(paymentAccount, body);
        return callApi(rootReq, ABBankConstants.VERIFY_OTP_URL_SUFFIX, new TypeReference<VerifyOtpRes>() {
        }, null);
    }

    @Override
    public PGResponse GetBalance(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {
        CheckBalanceReq body = objectMapper.readValue(inputStr, CheckBalanceReq.class);
        RootRequest rootReq = buildRootReq(paymentAccount, body);
        return callApi(rootReq, ABBankConstants.GET_BALANCE_PAY_URL_SUFFIX, new TypeReference<CheckBalanceRes>() {
        }, null);
    }

    @Override
    public PGResponse CheckTransStatus(PaymentAccount paymentAccount, String inputStr) throws JsonParseException, JsonMappingException, IOException,
            KeyManagementException, NoSuchAlgorithmException {

        CheckTransReq body = objectMapper.readValue(inputStr, CheckTransReq.class);
        PaymentDTO paymentDTO = new PaymentDTO();
        // TODO
        paymentDTO.setMerchantTransactionId(body.getMerchantId());
        // Write log before request
        String[] paramsLog = new String[]{objectMapper.writeValueAsString(body)};
        logger.info(commonLogService.createContentLog(ABBankConstants.CHANNEL_CODE, SERVICE_NAME,
                ABBankConstants.FUNCTION_CODE_CHECK_TRANS_STATUS, true, true, false, paramsLog));
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + StringUtils.join(paramsLog));

        RootRequest rootReq = buildRootReq(paymentAccount, body);

        return callApi(rootReq, ABBankConstants.CHECK_TRANS_PAY_URL_SUFFIX, new TypeReference<CheckTransRes>() {
        }, paymentDTO);
    }

    //Test DoiSoat

    public String DoiSoat() throws IOException {
        CreateReportNglAbb.doiSoat();
        return "ThanhCong";
    }

    public String DoiSoatGW(String req) throws IOException {
        CreateReportNglAbb.doiSoatGW(req);
        return "ThanhCong";
    }

    protected <T extends BaseResponse> PGResponse callApi(RootRequest rootReq, String action, TypeReference<T> objRes, PaymentDTO paymentDTO)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String[] paramsLog;

        String functionCode = "";
        if (action == ABBankConstants.PAY_WITH_BANK_ACC) {
            functionCode = ABBankConstants.FUNCTION_CODE_PAYMENT_WITH_BANK_ACC;
        } else if (action == ABBankConstants.CHECK_TRANS_PAY_URL_SUFFIX) {
            functionCode = ABBankConstants.FUNCTION_CODE_CHECK_TRANS_STATUS;
        }
        // TODO
        WriteInfoLog("2. ABB ROOT REQ", objectMapper.writeValueAsString(rootReq));

        RootResponse rootRes = ClientRequest.sendRequest(ChannelCommonConfig.ABB_URL_API + action, rootReq,
                MediaType.APPLICATION_JSON);

        // TODO
        WriteInfoLog("3. ABB ROOT RES", objectMapper.writeValueAsString(rootRes));
        T res = null;
        PGResponse pgRes = new PGResponse();
        String resCode = "";

        int htppStatus = rootRes.getHttpStatus();
        if (htppStatus == HttpURLConnection.HTTP_OK || htppStatus == HttpURLConnection.HTTP_CREATED) {
            res = objectMapper.readValue(rootRes.getBodyRes(), objRes);
            res.setTransId(rootReq.getBodyReq().getTransId());

            // verify signature
            String signatureRes = rootRes.getSignatureHeaderRes();
            if (!ABBankSecurity.verifyP12(signatureRes, res.rawData(), ChannelCommonConfig.PUBLIC_KEY_ABB_PATH)) {
                // TODO
                WriteErrorLog("ABB VERIFY SIGNATURE ERROR, RES DATA: " + res.rawData() + "\t RES SIGN: " + signatureRes);

                pgRes.setStatus(true);
                pgRes.setData(objectMapper.writeValueAsString(res));
                pgRes.setErrorCode("05");
                pgRes.setMessage("Verify Signature ABB FAIL");
                //return objectMapper.writeValueAsString(pgRes);
                return pgRes;
            }
            resCode = ABBankConstants.MAP_HTTP_STT.get(htppStatus);
        } else if (htppStatus == HttpURLConnection.HTTP_BAD_REQUEST) {
            res = objectMapper.readValue(rootRes.getBodyRes(), objRes);
            resCode = res.getCode();
            WriteErrorLog(HttpURLConnection.HTTP_BAD_REQUEST + "\t" + resCode);
        } else if (htppStatus == HttpURLConnection.HTTP_UNAUTHORIZED) {
            String accessToken = getTokenApi(0);
            rootReq.getHeaderReq().setAccessToken(accessToken);
            if (StringUtils.isNotBlank(accessToken))
                return callApi(rootReq, action, objRes, paymentDTO);
            else
                resCode = "ZZ";
        } else {
            // loi ko xac dinh
            resCode = "99";
        }

        pgRes.setStatus(true);
        pgRes.setData(objectMapper.writeValueAsString(res));
        pgRes.setErrorCode(resCode);
        pgRes.setMessage(ABBankConstants.getErrorMessage(resCode));
        //return objectMapper.writeValueAsString(pgRes);

        String responseStr = objectMapper.writeValueAsString(res);
        paramsLog = new String[]{responseStr};

        if (action == ABBankConstants.PAY_WITH_BANK_ACC) {
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);
            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
        } else if (action == ABBankConstants.CHECK_TRANS_PAY_URL_SUFFIX) {
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + responseStr);
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);
        }

        if (resCode == "0") {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            // Write log after request, response success
            logger.info(commonLogService.createContentLog(ABBankConstants.CHANNEL_CODE, SERVICE_NAME,
                    functionCode, true, false, true, paramsLog));
        } else {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            // Write log after request, response fail
            logger.info(commonLogService.createContentLog(ABBankConstants.CHANNEL_CODE, SERVICE_NAME,
                    functionCode, false, false, true, paramsLog));
        }
        // Write log end function
        logger.info(commonLogService.createContentLogStartEndFunction(ABBankConstants.CHANNEL_CODE, SERVICE_NAME,
                functionCode, false));

        return pgRes;
    }

    protected RootRequest buildRootReq(PaymentAccount paymentAccount, BaseRequest body) {
        body.setPartnerId(paymentAccount.getProviderId());
        body.setMerchantId(paymentAccount.getMerchantId());
        body.setMerchantName(paymentAccount.getMerchantName());

        HeaderRequest header = new HeaderRequest();
        header.setAccessToken(readFileSession());
        header.setSignature(ABBankSecurity.signP12(ChannelCommonConfig.PRIVATE_KEY_PATH,
                ChannelCommonConfig.PRIVATE_KEY_PASSWORD, body.rawData()));
        header.setVimoTransId(body.getTransId());

        RootRequest rootReq = new RootRequest();
        rootReq.setBodyReq(body);
        rootReq.setHeaderReq(header);
        return rootReq;
    }

    protected synchronized String getTokenApi(int timeLogin)
            throws JsonProcessingException, IOException, KeyManagementException, NoSuchAlgorithmException {
        GetAccessTokenReq req = new GetAccessTokenReq();
        req.setScope(SCOPE);
        CLIENT_ID = ChannelCommonConfig.AUTH_KEY_ABB.split(":")[0];
        CLIENT_SECRET = ChannelCommonConfig.AUTH_KEY_ABB.split(":")[1];
        req.setClientId(CLIENT_ID);
        req.setClientSecret(CLIENT_SECRET);
        req.setGrantType(GRANT_TYPE);

        RootRequest rootReq = new RootRequest();
        rootReq.setBodyReq(req);

        // TODO
        WriteInfoLog("Get Token Req: " + timeLogin, objectMapper.writeValueAsString(rootReq));
        RootResponse rootRes = ClientRequest.sendRequest(
                ChannelCommonConfig.ABB_URL_API + ABBankConstants.GET_TOKEN_URL_SUFFIX, rootReq,
                MediaType.APPLICATION_FORM_URLENCODED);

        // TODO
        WriteInfoLog("Get Token Res Time: " + timeLogin, objectMapper.writeValueAsString(rootRes));
        GetAccessTokenRes res = objectMapper.readValue(rootRes.getBodyRes(), GetAccessTokenRes.class);

        if (StringUtils.isNotBlank(res.getAccessToken())) {
            writeFileSession(res.getAccessToken());
            return res.getAccessToken();
        }
        timeLogin += 1;
        if (timeLogin < MAX_TIME_LOGIN)
            return getTokenApi(timeLogin);
        else
            return "";
    }

    private String readFileSession() {
        String accessToken = "";
        try {
            ClassLoader classLoader = new ABBankSecurity().getClass().getClassLoader();
            fileSession = new File(classLoader.getResource(ChannelCommonConfig.ABB_SESSION_FILE_TOKEN_PATH).getFile());
            if (!fileSession.exists()) {
                fileSession.createNewFile();
            }
            byte[] contentByte = Files.readAllBytes(fileSession.toPath());
            accessToken = new String(contentByte);
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return accessToken.replaceAll("\\n", "").replaceAll("\\t", "").trim();
    }

    private void writeFileSession(String newSession) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        StringBuilder sb = new StringBuilder(newSession);
        try {
            if (fileSession == null) {
                ClassLoader classLoader = new ABBankSecurity().getClass().getClassLoader();
                fileSession = new File(classLoader.getResource(ChannelCommonConfig.ABB_SESSION_FILE_TOKEN_PATH).getFile());
                if (!fileSession.exists()) {
                    fileSession.createNewFile();
                }
            }

            fw = new FileWriter(fileSession.getAbsolutePath());
            bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            bw.close();
            fw.close();
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     *
     * @param channelFunction
     * @param inputStr
     * @param req
     */
    private PaymentDTO createPaymentData(ChannelFunction channelFunction, String inputStr, PayWithBankAccReq req) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setAmount(String.valueOf(req.getAmount()));
        paymentDTO.setMerchantTransactionId(req.getBillId());
        // paymentDTO.setAccountNo(transactionInfo.getAccount_no());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + inputStr);
        // paymentDTO.setDescription(transactionInfo.getPayment_description());
        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }
}