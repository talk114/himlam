package gateway.core.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.channel.vib.request.VAVIBCallbackRequest;
import vn.nganluong.naba.channel.vib.response.VAVIBCallbackGWResponse;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.service.ChannelService;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.MyDateUtil;

@RestController
//@RequestMapping(value = "/restful/api/external")
public class ApiChannelExternalController {

    private static final Logger logger = LogManager.getLogger(ApiChannelExternalController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path = "/vib/va/callback", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<?> callBackVIB(VAVIBCallbackRequest callbackRequest) {

        try {

            logger.info(
                    commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                            VIBConst.FUNCTION_CODE_VA_CALL_BACK, true));
            ObjectMapper mapperObj = new ObjectMapper();
            String[] paramsLog = new String[]{mapperObj.writeValueAsString(callbackRequest)};

            if (StringUtils.isAnyEmpty(callbackRequest.getSeq_no(), callbackRequest.getActual_acct(),
                    // callbackRequest.getTran_type(),
                    callbackRequest.getTran_amt(), callbackRequest.getVirtual_acct(), callbackRequest.getNarrative(),
                    callbackRequest.getTran_date_time()) || !NumberUtils.isParsable(callbackRequest.getTran_amt())
                    || StringUtils.length(callbackRequest.getTran_amt()) > 12) {


                logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                        VIBConst.FUNCTION_CODE_VA_CALL_BACK, false, true, false, paramsLog));

                logger.info(commonLogService
                        .createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                                VIBConst.FUNCTION_CODE_VA_CALL_BACK, false));
                VAVIBCallbackGWResponse jsonObject = new VAVIBCallbackGWResponse();
                jsonObject.setStatus_code("999998");
                jsonObject.setMessage("Partner bad request");
                return new ResponseEntity<>(jsonObject, HttpStatus.OK);

            }

            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_CALL_BACK, true, true, false, paramsLog));

            Channel channel = channelService.findByCode(VIBConst.CHANNEL_CODE);

            // Insert payment to database:
            PaymentDTO paymentDTO = new PaymentDTO();

            paymentDTO.setChannelId(channel.getId());
            paymentDTO.setMerchantTransactionId(callbackRequest.getSeq_no() + "_" + StringUtils
                    .upperCase(RandomStringUtils.randomAlphanumeric(10)));
            paymentDTO.setChannelTransactionId(callbackRequest.getSeq_no());
            paymentDTO.setAccountNo(callbackRequest.getActual_acct());
            paymentDTO.setAmount(callbackRequest.getTran_amt());
            paymentDTO.setVirtualAccountNo(callbackRequest.getVirtual_acct());
            paymentDTO.setDescription(callbackRequest.getTran_date_time() + " " + callbackRequest.getNarrative());
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setPaymentType(PaymentConst.EnumPaymentType.VIRTUAL_ACCOUNT_NO.code());

            paymentDTO.setRawRequest(paramsLog[0]);

            paymentDTO.setTimeCreated(
                    MyDateUtil.parseDateFormat("yyyy-MM-dd HH:mm:ss", callbackRequest.getTran_date_time()));

            paymentService.createPaymentCallbackSuccess(paymentDTO);

            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_CALL_BACK, true, false, true, paramsLog));


            logger.info(
                    commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                            VIBConst.FUNCTION_CODE_VA_CALL_BACK, false));

        } catch (Exception e) {

            String[] paramsLog = new String[]{e.getMessage()};
            logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.VA_SERVICE_NAME,
                    VIBConst.FUNCTION_CODE_VA_CALL_BACK, false, false, true, paramsLog));
        }

        VAVIBCallbackGWResponse jsonObject = new VAVIBCallbackGWResponse();
        jsonObject.setStatus_code("000000");
        jsonObject.setMessage("Gateway request success");
        jsonObject.setSeq_no(callbackRequest.getSeq_no());
        jsonObject.setVirtual_acct(callbackRequest.getVirtual_acct());
        jsonObject.setActual_acct(callbackRequest.getActual_acct());
        //jsonObject.setTrace_id(callbackRequest.getTrace_id());
        //jsonObject.setTran_type(callbackRequest.getTran_type());
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }
}
