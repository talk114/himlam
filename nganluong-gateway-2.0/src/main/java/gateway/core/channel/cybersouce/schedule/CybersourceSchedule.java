package gateway.core.channel.cybersouce.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.cybersouce.dto.CybersourceConfig;
import gateway.core.channel.cybersouce.service.CybersouceService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CybersourceSchedule {
    private static final Logger logger = LogManager.getLogger(CybersourceSchedule.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CybersouceService cybersouceService;

    @Autowired
    CommonLogService commonLogService;

    @Scheduled(cron = "0 55 23 * * ?")
    public void cancelSubscriptionCybersource(){
        logger.info(commonLogService.createContentLogStartEndFunction(
                CybersourceConfig.CHANNEL_CODE,"cancelSubscriptionCybersource","cancelSubscriptionCybersource",true));
        List<Payment> paymentCBS = paymentService.getAllTokenizationNotSave();
        try {
            if(paymentCBS != null) {
                for (Payment payment : paymentCBS) {
                    String request = "{\"transactionReferenceId\":\"gw" + getClientTimestamp()
                            + "\",\"subscriptionId\":\"" + payment.getVirtualAccountNo() + "\"}";
                    cybersouceService.deleteToken(request, payment.getMerchantCode());
                }
            }
        }catch(Exception e){
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        logger.info(commonLogService.createContentLogStartEndFunction(
                CybersourceConfig.CHANNEL_CODE,"cancelSubscriptionCybersource","cancelSubscriptionCybersource",true));
    }

    private String getClientTimestamp() {
        String pattern = "yyyyMMddHHmmssSSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String timestamp = simpleDateFormat.format(new Date());
        return timestamp;
    }
}
