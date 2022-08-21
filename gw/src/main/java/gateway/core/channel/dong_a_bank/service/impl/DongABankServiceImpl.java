package gateway.core.channel.dong_a_bank.service.impl;

import com.fasterxml.jackson.databind.JsonNode;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.cybersouce.response.Response;
import gateway.core.channel.dong_a_bank.ClientHandler;
import gateway.core.channel.dong_a_bank.DABSecurity;
import gateway.core.channel.dong_a_bank.dto.DABConstants;
import gateway.core.channel.dong_a_bank.dto.req.*;
import gateway.core.channel.dong_a_bank.dto.res.CreateOrderRes;
import gateway.core.channel.dong_a_bank.dto.res.DABNotifyEcomRes;
import gateway.core.channel.dong_a_bank.dto.res.GetBillInfoRes;
import gateway.core.channel.dong_a_bank.service.DongABankService;
import gateway.core.channel.dong_a_bank.wsclient.WSBean;
import gateway.core.channel.dong_a_bank.wsclient.WSBeanServiceLocator;
import gateway.core.channel.dong_a_bank.wsclient.WSBeanSoapBindingStub;
import gateway.core.dto.PGResponse;
import gateway.core.util.FilePathUtil;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.entities.PgUser;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DongABankServiceImpl extends PaymentGate implements DongABankService {

    private static final Logger logger = LogManager.getLogger(DongABankServiceImpl.class);
    private static final String SERVICE_NAME = "IB ON";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonPGResponseService commonPGResponseService;

    @Autowired
    private CommonLogService commonLogService;

    private static final String private_keyNL = "DAB/private_key.pem";

    private static final String public_keyDAB = "DAB/public_keyDAB.pem";

    // http://sandbox.dongabank.com.vn/egate-new-dl-khcn/WSBean

    // private static final String PARTNER_CODE = "NLTT";
    // private static final String ACCESS_KEY = "NLTThDYPCH923NYv6wFx";
    // private static final String SECRET_KEY =
    // "SXgPh0zDxGJ3iYZwgLb6y62xUZ{9eXtkrTt)IVee";

    /**
     * Gửi thông tin Order, nếu thành công build url redirect gửi về Vimo/Ngân lượng
     *
     * @param channelFunction
     * @param paymentAccount
     * @param inputStr
     * @return
     */
    @Override
    public PGResponse createOrder(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr)
            throws IOException, ParseException, ServiceException, SignatureException {

        CreateOrderReq req = objectMapper.readValue(inputStr, CreateOrderReq.class);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(req.getOrderID());
        String[] paramsLog;
        if (paymentToCheckExist != null) {

            paramsLog = new String[] { "Merchant transaction id (trace id) already exist ("
                    + req.getOrderID() + ")" };
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                    DABConstants.FUNCTION_CODE_CREATE_ORDER, false, false, true, paramsLog));

            logger.info(commonLogService.createContentLogStartEndFunction(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                    DABConstants.FUNCTION_CODE_CREATE_ORDER, false));
            PGResponse pgRes = (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
            return pgRes;
            //return objectMapper.writeValueAsString(pgRes);
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, inputStr, req);

        req.setPartnerSub(paymentAccount.getMerchantId());

        Object[] params = {req.getOrderID(), req.getCustName(), req.getCustAddress(),
                DABConstants.dfDateOrder.format(DABConstants.dfTimestamp.parse(req.getDateOrder())), req.getPhone(), req.getDateDelivery(),
                req.getAddressDelivery(), req.getAmount(), req.getPartnerSub()};

        //String timeStamp = req.getDateOrder();
        String timeStamp = getTimeStamp();

        // Write log before request
        paramsLog = new String[] { inputStr };
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_CREATE_ORDER, true, true, false, paramsLog));

        WSBean proxy = getProxy(channelFunction, paymentAccount, DABConstants.CREATE_ORDER_ACTION, timeStamp);

        Object[] res = proxy.callExecution(DABConstants.CREATE_ORDER_ACTION, 5, params);

        // WriteInfoLog("NGANLUONG CREATE ORDER RES", objectMapper.writeValueAsString(res));


        PGResponse pgRes = new PGResponse();
        pgRes.setStatus(true);
        pgRes.setChannelErrorCode(res[0].toString());
        pgRes.setMessage(res[0].toString()); // Vì gateway 1 đang set kết quả trả về là kết quả của DongA

        String responseStr = objectMapper.writeValueAsString(res);
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);
        paramsLog = new String[] { responseStr };


        if (res[0].toString().equals(DABConstants.CREATE_ORDER_RESPONSE_CODE_SUCCESS)) {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            String url = buildUrlRedirect(channelFunction, paymentAccount, timeStamp, req.getOrderID());
            CreateOrderRes data = new CreateOrderRes();
            data.setUrlRedirect(url);
            pgRes.setData(objectMapper.writeValueAsString(data));

            PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
            // pgRes.setMessage(prefixResult.getMessage());
            pgRes.setErrorCode(prefixResult.getErrorCode());

            // Write log after request, response success
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                    DABConstants.FUNCTION_CODE_CREATE_ORDER, true, false, true, paramsLog));
        }
        else {
            PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
            pgRes.setMessage(prefixResult.getMessage());
            pgRes.setErrorCode(prefixResult.getErrorCode());

            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());

            // Write log after request, response fail
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                    DABConstants.FUNCTION_CODE_CREATE_ORDER, false, false, true, paramsLog));
        }

        paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

        // Write log end function
        paramsLog = new String[]{"END " + objectMapper.writeValueAsString(pgRes)};
        logger.info(paramsLog);

        //return objectMapper.writeValueAsString(pgRes);
        return pgRes;
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     * @param channelFunction
     * @param inputStr
     * @param req
     */
    private PaymentDTO createPaymentData(ChannelFunction channelFunction, String inputStr, CreateOrderReq req) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setAmount(String.valueOf(req.getAmount()));
        paymentDTO.setMerchantTransactionId(req.getOrderID());
        // paymentDTO.setAccountNo(transactionInfo.getAccount_no());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + inputStr);
        // paymentDTO.setDescription(transactionInfo.getPayment_description());
        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }


    @Override
    public PGResponse updateOrderStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr)
            throws IOException, ServiceException, SignatureException {

        UpdateOrderStatusReq req = objectMapper.readValue(inputStr, UpdateOrderStatusReq.class);
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId(req.getOrderID());
        // WriteInfoLog("NGANLUONG UPDATE ORDER STATUS REQ", objectMapper.writeValueAsString(req));
        // Write log before request
        String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_UPDATE_ORDER, true, true, false, paramsLog));
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + StringUtils.join(paramsLog));

        Object[] params = {req.getOrderID(), req.getStatus(), req.getChange()};

        String timeStamp = getTimeStamp();
        WSBean proxy = getProxy(channelFunction, paymentAccount, DABConstants.UPDATE_ORDER_ACTION, timeStamp);
        Object[] res = proxy.callExecution(DABConstants.UPDATE_ORDER_ACTION, 5, params);

        // WriteInfoLog("NGANLUONG UPDATE ORDER STATUS RES", objectMapper.writeValueAsString(res));
        // Write log after request, response success
        paramsLog = new String[] { objectMapper.writeValueAsString(res) };
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_UPDATE_ORDER, true, false, true, paramsLog));
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + StringUtils.join(paramsLog));

        if (res[0].toString().equals(DABConstants.CREATE_ORDER_RESPONSE_CODE_SUCCESS)) {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        }
        else {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        }

        paymentService.updateChannelTransactionStatusPayment(paymentDTO);
        // Write log end function
        logger.info(commonLogService.createContentLogStartEndFunction(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_UPDATE_ORDER, false));

        PGResponse pgRes = new PGResponse();
        pgRes.setStatus(true);
        pgRes.setErrorCode(res[0].toString());
        pgRes.setData(objectMapper.writeValueAsString(res[1]));
        paramsLog =  new String[]{"END " + objectMapper.writeValueAsString(pgRes)};
        logger.info(paramsLog);
        //return objectMapper.writeValueAsString(pgRes);
        return pgRes;
    }

    @Override
    public String notifyEcom( PaymentAccount paymentAccount, PgUser pgUser, String input)
            throws IOException, GeneralSecurityException {
        String[] paramsLog = new String[] { objectMapper.writeValueAsString(input) };
        logger.info(paramsLog);

        DABNotifyEcomReq req = objectMapper.readValue(input, DABNotifyEcomReq.class);

        // verify signature DAB
        String data = req.getPartnerCode() + "|" + req.getOrderId() + "|" + req.getState();

        boolean verify = false;
        try {
           // verify = DABSecurity.verify(data, req.getDataSign(), paymentAccount.getPartnerKeyPath());
            verify = DABSecurity.verify(data,req.getDataSign(),DongABankServiceImpl.public_keyDAB);
        } catch (IOException | GeneralSecurityException e) {
            // TODO Write log:
            WriteErrorLog("Verify signature DAB error :: " + e.getMessage());
        }

        if (verify) {
            // TODO Write log:
            WriteInfoLog("VERIFY SIGNATURE TRUE");
        } else {
            // TODO Write log:
            WriteErrorLog("VERIFY SIGNATURE FALSE");
            JSONObject res = new JSONObject();
            res.put("ErrorCode", "-6");
            res.put("Url", "");
            return res.toString();
        }

        // call Ngan luong
        req.setDataSign(null);
        Map<String, Object> map = new HashMap<>();
        map.put("params", objectMapper.writeValueAsString(req));
        String nganluongRes = HttpUtil.send(paymentAccount.getUrlApi(), map, null);
        // TODO Write log:
        WriteInfoLog("2. " + pgUser.getCode() + " response", nganluongRes);

        // Parse Response to DAB
        DABNotifyEcomRes notifyRes = objectMapper.readValue(nganluongRes, DABNotifyEcomRes.class);
        String rawData = notifyRes.getErrorCode() + "|" + notifyRes.getUrl();
      //  notifyRes.setSign(DABSecurity.sign(rawData, paymentAccount.getPrivateKeyPath()));
        String path = FilePathUtil.getAbsolutePath(DongABankServiceImpl.class, DongABankServiceImpl.private_keyNL);
        PrivateKey privateKey = PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File(path));
        notifyRes.setSign(PGSecurity.signatureRSASHA256(rawData, privateKey));

        String notifyResStr = objectMapper.writeValueAsString(notifyRes);
        // TODO Write log:
        WriteInfoLog("3. NGAN LUONG RES DAB", notifyResStr);
        paramsLog = new String[] { objectMapper.writeValueAsString(notifyRes) };
        logger.info(paramsLog);

        return notifyResStr;
    }

    @Override
    public PGResponse checkOderStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException, ServiceException, SignatureException {
        CheckOderReq req = objectMapper.readValue(inputStr,CheckOderReq.class);
        String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };
        Object[] params = {req.getOrderID()};

        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_CHECK_ORDER, true, true, false, paramsLog));
       String timeStamp = getTimeStamp();

        WSBean proxy = getProxy(channelFunction,paymentAccount,DABConstants.CHECK_ORDER_ACTION,timeStamp);
        Object[] res = proxy.callExecution(DABConstants.CHECK_ORDER_ACTION, 5, params);
        System.out.println(res[0].toString());
        paramsLog = new String[] { objectMapper.writeValueAsString(res) };
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME, DABConstants.FUNCTION_CODE_CHECK_ORDER, true, false, true, paramsLog));

        JSONObject jsonObject = XML.toJSONObject(res[1].toString());
        String value = jsonObject.toString();
        System.out.println(value);
        PGResponse pgRes = new PGResponse();
        pgRes.setStatus(true);
        pgRes.setErrorCode(res[0].toString());
        pgRes.setData(value);

        paramsLog =  new String[]{"END " + objectMapper.writeValueAsString(pgRes)};
        logger.info(paramsLog);

        return pgRes;

    }

    @Override
    public PGResponse getBillInfo(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException, ServiceException, SignatureException {
        GetBillInfoReq req = objectMapper.readValue(inputStr,GetBillInfoReq.class);
        String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };
        Object[] params = {req.getPos(),req.getMethod()};
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_GET_BILL_INFO, true, true, false, paramsLog));
        String timeStamp = getTimeStamp();
        WSBean proxy = getProxy(channelFunction,paymentAccount,DABConstants.GET_BILL_INFO_ACTION,timeStamp);
        Object[] res = proxy.callExecution(DABConstants.GET_BILL_INFO_ACTION, 5, params);
        paramsLog = new String[] { objectMapper.writeValueAsString(res) };
        PGResponse pgResponse = new PGResponse();
        if(res[0].toString().equals(DABConstants.CREATE_ORDER_RESPONSE_CODE_SUCCESS)){
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(res[0].toString());
            pgResponse.setData(res[1]);
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME, DABConstants.FUNCTION_CODE_GET_BILL_INFO, true, false, true, paramsLog));
        }else {
            PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
            pgResponse.setStatus(false);
            pgResponse.setMessage(prefixResult.getMessage());
            pgResponse.setErrorCode(res[0].toString());
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME, DABConstants.FUNCTION_CODE_GET_BILL_INFO, true, false, true, paramsLog));
        }
        return pgResponse;
    }

    @Override
    public PGResponse getBillInfoNL(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException, ServiceException, SignatureException {
        GetBillInfoNLReq req = objectMapper.readValue(inputStr,GetBillInfoNLReq.class);
        String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };
        Object[] params = {req.getOfi()};
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_GET_BILL_NL_INFO, true, true, false, paramsLog));
        Payment payment = paymentService.findByMerchantTransactionId(req.getOfi());
        PGResponse pgRes = new PGResponse();
        if(payment !=null) {
            String timeStamp = getTimeStamp();
            WSBean proxy = getProxy(channelFunction, paymentAccount, DABConstants.GET_BILL_INFO_NL_ACTION, timeStamp);
            Object[] res = proxy.callExecution(DABConstants.GET_BILL_INFO_NL_ACTION, 5, params);
            paramsLog = new String[] { objectMapper.writeValueAsString(res) };
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                    DABConstants.FUNCTION_CODE_GET_BILL_NL_INFO, false, false, true, paramsLog));
            pgRes.setStatus(true);
            pgRes.setErrorCode(res[0].toString());
            pgRes.setData(res[1]);
        }else{
            paramsLog = new String[] { "Merchant transaction id (trace id) already not exist ("
                    + req.getOfi() + ")" };
            logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                    DABConstants.FUNCTION_CODE_GET_BILL_NL_INFO, false, false, true, paramsLog));
            pgRes = (PGResponse) commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();

        }

        return pgRes;
    }

    @Override
    public PGResponse getListBillingSize(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException, ServiceException, SignatureException {
        GetListBillSize req = objectMapper.readValue(inputStr,GetListBillSize.class);
        String[] paramsLog = new String[] { objectMapper.writeValueAsString(req) };
        Object[] params = {req.getMethod()};
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_GET_LIST_BILLING_SIZE, true, true, false, paramsLog));
        String timeStamp = getTimeStamp();

        WSBean proxy = getProxy(channelFunction, paymentAccount, DABConstants.GET_LIST_BILLING_SIZE_ACTION, timeStamp);
        Object[] res = proxy.callExecution(DABConstants.GET_LIST_BILLING_SIZE_ACTION, 5, params);
        paramsLog = new String[] { objectMapper.writeValueAsString(res) };
        logger.info(commonLogService.createContentLog(DABConstants.CHANNEL_CODE, SERVICE_NAME,
                DABConstants.FUNCTION_CODE_GET_BILL_NL_INFO, false, false, true, paramsLog));

        PGResponse pgRes = new PGResponse();
        pgRes.setStatus(true);
        pgRes.setErrorCode(res[0].toString());
        pgRes.setData(res[1]);

        paramsLog =  new String[]{"END " + objectMapper.writeValueAsString(pgRes)};
        logger.info(paramsLog);

        return  pgRes;

    }


    public WSBean getProxy(ChannelFunction channelFunction, PaymentAccount paymentAccount, String command, String timeStamp)
            throws MalformedURLException, ServiceException, SignatureException {
        URL portAddress = new URL(channelFunction.getUrl());
        WSBeanServiceLocator locator = new WSBeanServiceLocator();

        QName portName = new QName("https://webservice.dongabank.com.vn/egate-new/WSBean?WSDL", "WSBean");
        HandlerRegistry registry = locator.getHandlerRegistry();
        List<HandlerInfo> handlerList = new ArrayList<>();

        Map<String, Object> handlerConfig = getHandlerConfig(paymentAccount, command, timeStamp);
        handlerList.add(new HandlerInfo(ClientHandler.class, handlerConfig, null));
        registry.setHandlerChain(portName, handlerList);

        WSBeanSoapBindingStub client = (WSBeanSoapBindingStub) locator.getWSBean(portAddress);

        return client;
    }

    private Map<String, Object> getHandlerConfig(PaymentAccount paymentAccount, String command, String timeStamp) throws SignatureException {
        Map<String, Object> handlerConfig = new HashMap<>();
        handlerConfig.put(DABConstants.ACCESS_KEY_HEADER, paymentAccount.getAuthKey());
        handlerConfig.put(DABConstants.TIME_STAMP_HEADER, timeStamp);
        String data = command + handlerConfig.get(DABConstants.TIME_STAMP_HEADER).toString();
        handlerConfig.put(DABConstants.SIGNATURE_HEADER,
                DABSecurity.calculateRFC2104HMAC(data, paymentAccount.getEncryptKey()));
        return handlerConfig;
    }

    private String buildUrlRedirect(ChannelFunction channelFunction, PaymentAccount paymentAccount, String timeStamp, String orderId)
            throws SignatureException, ParseException, UnsupportedEncodingException {
        SimpleDateFormat dfUrl = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStampUrl = dfUrl.format(DABConstants.dfTimestamp.parse(timeStamp));

        String signature = DABSecurity.calculateRFC2104HMAC(
                paymentAccount.getMerchantId() + timeStampUrl + orderId, paymentAccount.getEncryptKey());

        // http://uat-apps.dongabank.com.vn/ebankinternet-khcn/partner?partnerSub=NLTT&orderid=VM8250476521&signature=w4Isr%2Bs6UTwv3nlHtQXLLbXBnlE=&timestamp=20190306143405&partnerCode=NLTT#payonline_partner_login:
        String url = channelFunction.getHost() + "partnerSub="
                + paymentAccount.getMerchantId() + "&orderid=" + orderId + "&signature="
                + URLEncoder.encode(signature, "UTF-8") + "&timestamp=" + timeStampUrl + "&partnerCode="
                + paymentAccount.getMerchantId() + "#payonline_partner_login:";

        return url;
    }

    private static String getTimeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return DABConstants.dfTimestamp.format(new Date());
    }
}
