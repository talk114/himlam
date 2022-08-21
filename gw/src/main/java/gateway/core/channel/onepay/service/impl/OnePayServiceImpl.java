package gateway.core.channel.onepay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.onepay.SecureHash;
import gateway.core.channel.onepay.dto.BankInfoObject;
import gateway.core.channel.onepay.dto.OnePayConstants;
import gateway.core.channel.onepay.dto.res.*;
import gateway.core.channel.onepay.service.OnePayService;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.RequestUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OnePayServiceImpl extends PaymentGate implements OnePayService {

    private static final Logger logger = LogManager.getLogger(OnePayServiceImpl.class);

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonPGResponseService commonPGResponseService;

    @Override
    public PGResponse verifyCard(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {

        // WriteInfoLog("OnePay VerifyCard");
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_VERIFY_CARD, true));

        Map<String, String> req = objectMapper.readValue(inputStr, new TypeReference<Map<String, String>>() {
        });
        req.put("vpc_Merchant", paymentAccount.getMerchantId());
        req.put("vpc_AccessCode", paymentAccount.getEncryptKey());

        if (OnePayConstants.map.containsKey(req.get("vpc_BankId"))) {
            BankInfoObject bankObj = OnePayConstants.map.get(req.get("vpc_BankId"));

            if (StringUtils.isNotBlank(bankObj.getAuthMethod()))
                req.put("vpc_AuthMethod", bankObj.getAuthMethod());
            if (StringUtils.isNotBlank(bankObj.getCardType()))
                req.put("vpc_CardType", bankObj.getCardType());
            if (StringUtils.isNotBlank(bankObj.getCardName()))
                req.put("vpc_CardName", bankObj.getCardName());
        }

        String hash = getHash(req, paymentAccount.getAuthKey());
        req.put("vpc_SecureHash", hash);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(req.get("vpc_MerchTxnRef"));
        String[] paramsLog;
        if (paymentToCheckExist != null) {
            paramsLog = new String[]{"Transaction id (trace id) already exist ("
                    + req.get("vpc_MerchTxnRef") + ")"};
            logger.info(commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    OnePayConstants.FUNCTION_CODE_VERIFY_CARD, false, false, true, paramsLog));

            logger.info(
                    commonLogService.createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE,
                            OnePayConstants.SERVICE_NAME,
                            OnePayConstants.FUNCTION_CODE_VERIFY_CARD, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, req);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        String strReturn = callAPI(req, endpointUrl, OnePayConstants.FUNCTION_CODE_VERIFY_CARD, req.get("vpc_MerchTxnRef"));
        VerifyCardRes verifyCardRes = objectMapper.readValue(strReturn, VerifyCardRes.class);

        PGResponse pgResponse = buildResponse(verifyCardRes);
        updatePaymentAfterCreated(paymentDTO, pgResponse);
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_VERIFY_CARD, false));
        return  pgResponse;
    }

    @Override
    public PGResponse verifyAuthen(ChannelFunction channelFunction, PaymentAccount paymentAccount, String reqStr) throws
            Exception {
        // WriteInfoLog("OnePay VerifyAuthen");
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_VERIFY_AUTHEN, true));

        Map<String, String> req = objectMapper.readValue(reqStr, new TypeReference<Map<String, String>>() {
        });
        TreeMap<String, String> mapping = (TreeMap<String, String>) convertLinkedHashMapToTreeMap(req);

        Map<String, String> urlValuePair = RequestUtil.splitQuery(mapping.get("vpc_AuthURL"));
        String trans_id = urlValuePair.get("vpc_MerchTxnRef");
        if (StringUtils.isEmpty(trans_id)) {
            trans_id = urlValuePair.get("transaction_id");
        }
        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        String strReturn = callAPI(mapping, endpointUrl,
                OnePayConstants.FUNCTION_CODE_VERIFY_AUTHEN, trans_id);
        VerifyAuthenRes verifyAuthenRes = objectMapper.readValue(strReturn, VerifyAuthenRes.class);

        if (verifyAuthenRes.getVpcTxnResponseCode().equalsIgnoreCase("0")) {
            String vpcSecureHash = verifyAuthenRes.getVpcSecureHash();
            Map<String, String> map = objectMapper.readValue(strReturn, new TypeReference<Map<String, String>>() {
            });
            map.remove("vpc_SecureHash");
            if (vpcSecureHash.equalsIgnoreCase(getHash(map, paymentAccount.getAuthKey()))) {
                verifyAuthenRes.setVpcSecureHashCompare("0");
            } else {
                verifyAuthenRes.setVpcSecureHashCompare("1");
            }
        }

        PGResponse pgResponse =  buildResponse(verifyAuthenRes);
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_VERIFY_AUTHEN, false));
        return pgResponse;
    }

    @Override
    public PGResponse query(ChannelFunction channelFunction, PaymentAccount paymentAccount, String reqStr) throws
            Exception {
        // WriteInfoLog("OnePay Query");
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_QUERY_ORDER, true));

        Map<String, String> req = objectMapper.readValue(reqStr, new TypeReference<Map<String, String>>() {
        });
        req.put("vpc_Merchant", paymentAccount.getMerchantId());
        req.put("vpc_AccessCode", paymentAccount.getEncryptKey());
        req.put("vpc_User", paymentAccount.getUsername());
        req.put("vpc_Password", paymentAccount.getPassword());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId(req.get("vpc_MerchTxnRef"));

        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        String[] paramsLog = null;

        if (paymentToCheckExist == null) {

            paramsLog = new String[] { "MerchTxnRef (trace id) not exist ("
                    + paymentDTO.getMerchantTransactionId() + ")" };
            logger.info(commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    OnePayConstants.FUNCTION_CODE_QUERY_ORDER, false, false, true, paramsLog));

            logger.info(commonLogService.createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    OnePayConstants.FUNCTION_CODE_QUERY_ORDER, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        }
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + objectMapper.writeValueAsString(req));

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        String strReturn = callAPI(req, endpointUrl, OnePayConstants.FUNCTION_CODE_QUERY_ORDER,
                req.get("vpc_MerchTxnRef"));
        QueryRes queryRes = objectMapper.readValue(strReturn, QueryRes.class);

        PGResponse pgResponse = buildResponse(queryRes);

        updatePaymentStatus(paymentDTO, pgResponse);
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_QUERY_ORDER, false));
        return pgResponse;
    }

    // API refund
    public PGResponse refund(ChannelFunction channelFunction, PaymentAccount paymentAccount, String input) throws
            Exception {
        // WriteInfoLog("ONE PAY REFUND");
        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_REFUND, true));

        Map<String, String> req = objectMapper.readValue(input, new TypeReference<Map<String, String>>() {
        });
        req.put("vpc_Command", "refund");
        req.put("vpc_Merchant", paymentAccount.getMerchantId());
        req.put("vpc_AccessCode", paymentAccount.getEncryptKey());

        String hash = getHash(req, paymentAccount.getAuthKey());
        req.put("vpc_SecureHash", hash);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId(req.get("vpc_MerchTxnRef"));

        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        String[] paramsLog = null;

        if (paymentToCheckExist == null) {
            paramsLog = new String[] { "MerchTxnRef (trace id) not exist ("
                    + paymentDTO.getMerchantTransactionId() + ")" };
            logger.info(commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    OnePayConstants.FUNCTION_CODE_REFUND, false, false, true, paramsLog));

            logger.info(commonLogService.createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    OnePayConstants.FUNCTION_CODE_REFUND, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        }
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + objectMapper.writeValueAsString(req));

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        String response = callAPI(req, endpointUrl, OnePayConstants.FUNCTION_CODE_REFUND, req.get("vpc_MerchTxnRef"));
        RefundResponse refundResponse = objectMapper.readValue(response, RefundResponse.class);
        if (refundResponse.getVpcTxnResponseCode().equals("0")) { // Refund chỉ thành công khi VpcTxnResponseCode = 0
            Map<String, String> hashVerifyResponse = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {
            });
            hashVerifyResponse.remove("vpc_SecureHash");
            if (refundResponse.getVpcSecureHash().equalsIgnoreCase(getHashRefund(hashVerifyResponse, paymentAccount.getAuthKey()))) {
                refundResponse.setVpcSecureHashCompare("VERIFY HASH VALUE SUCCESS");
                // TODO Update payment status
                paymentDTO.setChannelRevertStatus(PaymentConst.EnumPaymentRevertStatus.REVERTED.code());
            } else {
                refundResponse.setVpcSecureHashCompare("VERIFY HASH VALUE FALSE");
                refundResponse.setVpcMessage("CANNOT VERIFY HASH VALUE ONE PAY");
            }
        }
        PGResponse pgResponse = buildResponse(refundResponse);

        refundPaymentStatus(paymentDTO, pgResponse);

        logger.info(commonLogService
                .createContentLogStartEndFunction(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                        OnePayConstants.FUNCTION_CODE_REFUND, false));
        return pgResponse;
    }

    /**
     * ################################################################################################################################
     * ################################################################################################################################
     * ################################################################################################################################
     */

    private Map<String, String> convertLinkedHashMapToTreeMap(Map<String, String> map) {
        Map<String, String> mapping = new TreeMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mapping.put(entry.getKey(), entry.getValue());
        }
        return mapping;
    }


    private PGResponse buildResponse(BaseResponse opRes) throws JsonProcessingException {
        PGResponse res = new PGResponse();
        res.setStatus(true);
        res.setErrorCode(opRes.getVpcTxnResponseCode());
        res.setMessage(opRes.getVpcMessage());
        res.setData(objectMapper.writeValueAsString(opRes));
        return  res;
    }

    private String getHash(Map<String, String> map, String secureSecret) throws Exception {
        SecureHash.SECURE_SECRET = secureSecret;
        return SecureHash.hashAllFields(map);
    }

    private String getHashRefund(Map<String, String> map, String secureSecret) throws Exception {
        SecureHash.SECURE_SECRET = secureSecret;
        return SecureHash.hashAllFieldsRefund(map);
    }

    private String callAPI(Map<String, String> req, String functionURL, String pgFunctionCode, String... transId) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();

        URL url = new URL(functionURL);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);

        String paramsStr = "";
        for (Map.Entry<String, String> entry : req.entrySet()) {
            if (entry.getKey().equals("vpc_CustomerUserAgent")) {
                String valueAfterReplace = entry.getValue().replace("+", "");
                paramsStr += entry.getKey() + "=" + valueAfterReplace + "&";
            } else {
                paramsStr += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        paramsStr = paramsStr.substring(0, paramsStr.length() - 1);

        // WriteInfoLog("2. ONEPAY REQUEST", paramsStr);
        String[] paramsLog = new String[]{paramsStr};

        if (ArrayUtils.isEmpty(transId)) {
            logger.info(commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    pgFunctionCode, true, false, true, paramsLog));
        } else {
            commonLogService.logInfoWithTransId(logger, transId[0],
                    commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                            pgFunctionCode, true, true, false, paramsLog));
        }

        DataOutputStream wr = new DataOutputStream((con.getOutputStream()));

        wr.writeBytes(paramsStr);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // WriteInfoLog("3. ONEPAY RESPONSE", response.toString());
        paramsLog = new String[]{response.toString()};
        if (ArrayUtils.isEmpty(transId)) {
            logger.info(commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    pgFunctionCode, true, false, true, paramsLog));
        } else {
            commonLogService.logInfoWithTransId(logger, transId[0],
                    commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                            pgFunctionCode, true, false, true, paramsLog));
        }


        if (response.length() > 0) {
            String[] array = response.toString().split("&");
            for (String p : array) {
                String[] kv = p.split("=");
                if (kv.length > 1) {
                    resp.put(kv[0], kv[1]);
                }
            }
        }
        return objectMapper.writeValueAsString(resp);
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     *
     * @param channelFunction
     * @param req
     */
    private PaymentDTO createPaymentData(ChannelFunction channelFunction, Map<String, String> req) throws
            JsonProcessingException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        // paymentDTO.setChannelTransactionType(req.getPaymentType());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(req));
        paymentDTO.setCardNo(req.get("vpc_CardNo"));
        paymentDTO.setMerchantCode(req.get("vpc_Merchant"));
        // setBankCode unsetted
        paymentDTO.setMerchantTransactionId(req.get("vpc_MerchTxnRef"));
        paymentDTO.setAmount(req.get("vpc_Amount"));
        paymentDTO.setDescription(req.get("Title"));

        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }

    private void updatePaymentAfterCreated(PaymentDTO paymentDTO, PGResponse pgResponse) throws
            JsonProcessingException {
        String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

        // Update status payment
        if (StringUtils.equals(pgResponse.getErrorCode(),
                OnePayConstants.API_RESPONSE_STATUS_CODE_SUCCESS)) {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else if (ArrayUtils.contains(OnePayConstants.API_RESPONSE_STATUS_CODE_PENDING, pgResponse.getErrorCode())) {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }

        paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
    }

    private void updatePaymentStatus(PaymentDTO paymentDTO, PGResponse pgResponse) throws
            JsonProcessingException {
        String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + responseStr);

        // Update status payment
        if (StringUtils.equals(pgResponse.getErrorCode(),
                OnePayConstants.API_RESPONSE_STATUS_CODE_SUCCESS)) {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else if (ArrayUtils.contains(OnePayConstants.API_RESPONSE_STATUS_CODE_PENDING, pgResponse.getErrorCode())) {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        } else {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }
        paymentService.updateChannelTransactionStatusPayment(paymentDTO);
    }

    private void refundPaymentStatus(PaymentDTO paymentDTO, PGResponse pgResponse) throws
            JsonProcessingException {
        String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + responseStr);

        paymentService.updateChannelTransactionStatusPayment(paymentDTO);
    }
}
