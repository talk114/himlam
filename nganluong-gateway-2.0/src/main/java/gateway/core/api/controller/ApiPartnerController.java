package gateway.core.api.controller;
import gateway.core.channel.bidv_ecom.dto.BidvCallbackRes;
import gateway.core.channel.bidv_ecom.dto.BidvEcomConstants;
import gateway.core.channel.bidv_ecom.service.BIDVEcomService;
import gateway.core.channel.dong_a_bank.service.DongABankService;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.msb_offus.dto.MSBOFFUSConstant;
import gateway.core.channel.msb_offus.request.NotificationOrderRequest;
import gateway.core.channel.msb_offus.service.MSBOFFUSService;
import gateway.core.channel.msb_qr.dto.MsbConstants;
import gateway.core.channel.msb_qr.service.MSBQRCodeService;
import gateway.core.channel.msb_va.dto.MSBVAContants;
import gateway.core.channel.msb_va.service.MSBVAService;
import gateway.core.channel.tcb_qrcode.dto.TCB_QrcodeConstants;
import gateway.core.channel.tcb_qrcode.service.TCB_QrcodeService;
import gateway.core.channel.vcb_ib.dto.VCBIbConstants;
import gateway.core.channel.vcb_ib.service.VCBIbService;
import gateway.core.channel.vccb_va.service.VCCBVirtualAccountService;
import gateway.core.channel.vib.service.VIBVAService;
import gateway.core.util.PGUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.*;
import vn.nganluong.naba.service.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/restful/api/partner")
public class ApiPartnerController {

    private static final Logger logger = LogManager.getLogger(ApiPartnerController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String COMMENT = "############################";

    @Autowired
    private PgEndpointService pgEndpointService;
    @Autowired
    private PgUserService pgUserService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelFunctionService channelFunctionService;
    @Autowired
    private PgFunctionService pgFunctionService;
    @Autowired
    private PaymentAccountService paymentAccountService;
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private MSBQRCodeService msbqrCodeService;
    @Autowired
    private VCBIbService vcbIbService;
    @Autowired
    private MSBOFFUSService mSBOFFUSService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private VIBVAService vibvaService;
    @Autowired
    private BIDVEcomService bidvEcomService;
    @Autowired
    private MSBVAService msbvaService;
    @Autowired
    private VCCBVirtualAccountService vccbVirtualAccountService;
    @Autowired
    private DongABankService dongABankService;

    @Autowired
    private TCB_QrcodeService tcb_qrcodeService;

    @PostMapping(path = "/dongabankCallbackNganluong", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public  String dongabankCallbackNganluong(@RequestBody String req) {

        try {
            logger.info(COMMENT + "1. PARAM FROM DAB_NGANLUONG" + COMMENT + "\n" + req);
   //         processPartnerRequest("DONG_A_BANK_NGL", "NGL_DONG_A_BANK", "NotifyEcom", req);
             String userCode = "NL";
             PgUser pgUser = pgUserService.findByCode(userCode);
             int channel_id = 4;
             PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserIdAndChannelId(pgUser.getId(),channel_id);

             return dongABankService.notifyEcom(paymentAccount,pgUser,req);

        } catch (Exception ex) {
//            PGUtil.sendEmail("DONG A BANK CALLBACK NGANLUONG FAIL: " + req);
            logger.error(ex.getMessage(), ex);
            JSONObject res = new JSONObject();
            res.put("ErrorCode", "-99");
            res.put("Url", "");
            return res.toString();

        }

    }

    @PostMapping(path = "/msbCompleteNLQrPayment", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void msbCompleteNLQrPayment(@RequestBody String request) {

        try {

            logger.info(COMMENT + "1. PARAM FROM MSB_QR_NGL" + COMMENT + "\n" + request);
            logger.info(
                    commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE,
                            MsbConstants.SERVICE_NAME_COMPLETE_CALLBACK,
                            MsbConstants.FUNCTION_CODE_COMPLETE_QR_PAYMENT, true));

            String[] paramsLog = new String[]{request};
            logger.info(commonLogService.createContentLog(MsbConstants.CHANNEL_CODE, MsbConstants.SERVICE_NAME_COMPLETE_CALLBACK,
                    MsbConstants.FUNCTION_CODE_COMPLETE_QR_PAYMENT, true, true, false, paramsLog));

            String userCode = "NGANLUONG_QRMSB";
            String channelCode = "MSB_QRCODE";
            processPartnerRequest("MSB_QR_NGL", userCode, MsbConstants.FUNCTION_CODE_COMPLETE_QR_PAYMENT, request);

            PgUser pgUser = pgUserService.findByCode(userCode);
            PgFunction pgFunction = pgFunctionService
                    .findByCodeAndChannelCode(MsbConstants.FUNCTION_CODE_COMPLETE_QR_PAYMENT, channelCode);
            Channel channel = channelService.findById(pgFunction.getChannelId());
            PaymentAccount paymentAccount = paymentAccountService
                    .getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channel.getId());

            msbqrCodeService.CompleteQrPayment(paymentAccount, request);

        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }

        logger.info(
                commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE,
                        MsbConstants.SERVICE_NAME_COMPLETE_CALLBACK,
                        MsbConstants.FUNCTION_CODE_COMPLETE_QR_PAYMENT, false));
    }

    @PostMapping(path = "/vcbCallback/{data}", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void vcbCallback(@PathVariable("data") String data) {

        try {
            logger.info(commonLogService
                    .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                            VCBIbConstants.FUNCTION_CODE_CALLBACK, true));
            String userCode = "NL";
            String channelCode = "VCB_IB";
            PgUser pgUser = pgUserService.findByCode(userCode);
            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(VCBIbConstants.FUNCTION_CODE_CALLBACK, channelCode);
            PaymentAccount paymentAccount = paymentAccountService
                    .getPaymentAccountByUserIdAndChannelId(pgUser.getId(), pgFunction.getChannelId());

            vcbIbService.vcbCallback(paymentAccount, data);

        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }

        logger.info(
                commonLogService.createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE,
                        VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_CALLBACK, false));
    }
    @PostMapping(path = "/msb/va/notify", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public void msbVACallback(@RequestBody String bodyRequest,
                              HttpServletRequest request) {
        try {
            logger.info("####################################");
            logger.info("-----------MSB-CALLBACK------------");
            logger.info("####################################");
            logger.info("RemoteAddr: "+request.getRemoteAddr());
            logger.info(MSBVAContants.CHANNEL_CODE + "- NOTIFY : " + bodyRequest);
            msbvaService.MSBVANotify(bodyRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MSBVAContants.CHANNEL_CODE + " -cause :" + e.getMessage());
        }
    }

    @PostMapping(path = "/vccb-bto/notify", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> vccbVACallback(@RequestHeader("signature") String signature,
                                            @RequestBody String requestBody,
                                            HttpServletRequest request) {
        logger.info("####################################");
        logger.info("-----------VCCB-CALLBACK------------");
        logger.info("####################################");
        logger.info("RemoteAddr: "+request.getRemoteAddr());
        String filterRequest = requestBody.replaceAll("[*%;]", "");
        //TODO implement callback
        logger.info("VCCB VA: " + signature);
        logger.info("VCCB VA: " + requestBody);
        return vccbVirtualAccountService.notifyVAVCCB(signature, filterRequest);
    }

    @PostMapping(path = "/vib/va/notify", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> vibCallback(@RequestHeader("signature") String signature,
                                         @RequestBody String requestBody,
                                         HttpServletRequest request) {
        logger.info("####################################");
        logger.info("-----------VIB-CALLBACK------------");
        logger.info("####################################");
        logger.info("RemoteAddr: "+request.getRemoteAddr());
        String filterRequest = requestBody.replaceAll("[*%;]", "");
        System.out.println("signature: " + signature);
        //TODO implement callback
        logger.info(requestBody);
        return vibvaService.notifyListener(signature, filterRequest);
    }

    @PostMapping(path = "/tcb_qrcode/notify")
    public String TCBQRCODE_CALLBACK(@RequestBody String req) {
        try{
            logger.info("####################################");
            logger.info("-----------TCBQRCODE-CALLBACK------------");
            logger.info("####################################");
            logger.info("Request: "+req);

            return tcb_qrcodeService.NotifyTCB_QRCODE(req);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(TCB_QrcodeConstants.CHANNEL_CODE + " -cause :" + e.getMessage());
            return null;
        }

    }

    /**
     * ********************* PROCESS PARTNER REQUEST *********************
     * *******************************************************************
     */
    private String processPartnerRequest(String channelCode, String userCode, String func, String request)
            throws Exception {

        // processPartnerRequest("MSB_QR_NGL", "NGANLUONG_QRMSB", "CompleteNLQrPayment", request);

        PgUser pgUser = pgUserService.findByCode(userCode);
        PgEndpoint pgEndpoint = pgEndpointService.findById(pgUser.getEndpointId());
        PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(func, channelCode);
        Channel channel = channelService.findById(pgFunction.getChannelId());

        ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByName(func);
        PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channel.getId());
        // TODO


//        Map<String, String> mapPgConfigs = apiService.getPGConfigs();
//        PGUser user = apiService.getUserByCode(userCode);
//        PaymentChannel paymentChannel = apiService.getPaymentChannelByName(channelCode);
//
//        validate(user, paymentChannel);
//        PaymentAccount paymentAccount = apiService.getPaymentAccount(user.getId(), paymentChannel.getId());
//        Object paymentGate = PaymentFactory.loadDriverClass(paymentChannel);
//
//        Method method = paymentGate.getClass().getDeclaredMethod(func, String.class);
//        if (method == null) {
//            throw new Exception("There is no such method");
//        }
//        BeanUtils.setProperty(paymentGate, "paymentChannelStr", mapper.writeValueAsString(paymentChannel));
//        BeanUtils.setProperty(paymentGate, "paymentAccountStr", mapper.writeValueAsString(paymentAccount));
//        BeanUtils.setProperty(paymentGate, "mapPGConfigsStr", mapper.writeValueAsString(mapPgConfigs));
//        BeanUtils.setProperty(paymentGate, "paymentUserStr", mapper.writeValueAsString(user));
//
//        String res = (String) method.invoke(paymentGate, request);
//
//        try {
//            PGLog log = new PGLog();
//            log.setUserId(user.getId());
//            log.setFunction(func);
//            log.setChannelId(paymentChannel.getId());
//            log.setRequest(request);
//            log.setResponse(res);
//            log.setTimeCreated(new Date());
//            log.setType(PGConstant.TYPE_TRX_PARTNER);
//            apiService.saveTrxLog(log);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            logger.info(ex.getMessage());
//        }
//
//        return res;
        return null;
    }

    @PostMapping(path = "/msb/offus/callback", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String msbOFFUSCallback(@RequestBody String request) {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START NOTIFICATION ORDER MSB OFFUS (" + request + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_NOTIFICATION_ORDER, false, false, true, paramsLog));

            String userCode = "NL";
            String channelCode = "MSB_ECOM_OFFUS";


            PgUser pgUser = pgUserService.findByCode(userCode);

            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(MSBOFFUSConstant.FUNCTION_NOTIFICATION_ORDER, channelCode);


            PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserIdAndChannelId(pgUser.getId(), pgFunction.getChannelId());

            // data convert
//            NotificationOrderRequest dataConvert = new NotificationOrderRequest();

            NotificationOrderRequest response = mSBOFFUSService.msbOffUsCallbackNL(paymentAccount, request);

            if (response.getStatus().equals("1")) {
                Payment payment = paymentService.findByMerchantTransactionId(response.getBillNumber());
                payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentService.updatePayment(payment);
            }
            // Write log end function
            paramsLog = new String[]{"END NOTIFICATION ORDER MSB OFFUS (" + request + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_NOTIFICATION_ORDER, false, false, true, paramsLog));
            return new String("{\"error_code \" : \"00\"}");
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }
}
