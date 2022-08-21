package gateway.core.channel.ocb.service.schedule;

import gateway.core.channel.ocb.dto.OCBConstants;
import gateway.core.channel.ocb.dto.OCBReconciliation;
import gateway.core.channel.ocb.service.OCBService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author dungla
 */

@Component
public class OCBScheduleMaker {

    private static final Logger logger = LogManager.getLogger(OCBService.class);
    private final String privateKeyPath = "OCB/";
    private static String filePath = "";
    @Autowired
    PaymentService paymentService;
    @Autowired
    CommonLogService commonLogService;


//    @Scheduled(cron = "0 0 3 * * ?")
    public void sendReconciliationFile() {
        logger.info(commonLogService.createContentLogStartEndFunction(
                OCBConstants.CHANNEL_CODE,"sendReconciliationFile","sendReconciliationFile",true));
        List<Payment> paymentsOCB = getAllPaymentsSucceededYesterday();

        List<OCBReconciliation> ocbReconciliations = transformPaymentsToOCBCrossChecks(paymentsOCB);

        writeToFile(ocbReconciliations);

        logger.info(commonLogService.createContentLogStartEndFunction(
                OCBConstants.CHANNEL_CODE,"sendReconciliationFile","sendReconciliationFile",false));
    }

    private List<OCBReconciliation> transformPaymentsToOCBCrossChecks(List<Payment> paymentsOCB) {

        List<OCBReconciliation> checkList = new ArrayList();

        for (Payment payment : paymentsOCB) {
            OCBReconciliation ocbReconciliation = new OCBReconciliation(
                    payment.getMerchantTransactionId(),
                    payment.getChannelTransactionId(),
                    payment.getChannelTransactionSeq(),
                    new SimpleDateFormat("dd/MM/yyyy").format(payment.getTimeCreated()),
                    String.valueOf(payment.getAmount()),
                    "VND", null);
            checkList.add(ocbReconciliation);
        }
        return checkList;
    }

    private List<Payment> getAllPaymentsSucceededYesterday() {
        return paymentService.getAllPaymentsSucceededYesterday(OCBConstants.OCB_CHANNEL_ID);
    }

    private String getWorkingDirectory() {

        return System.getProperty("user.dir");
    }

    private boolean writeToFile(List<OCBReconciliation> list) {
        filePath = getWorkingDirectory() +"/"+ getYesterdayDateString() + "_NGANLUONGJSC_TRANS.csv";

        File file = new File(filePath);
        FileWriter writer = null;
        try {
            if(file.createNewFile()){
                writer = new FileWriter(file, true);
                writer.append("NGAN LUONG_TRANS_ID,OCB_TRANS_ID,OCB_FT,TRANS_DATE,AMOUNT,CCY,RRC,CHECK_SUM\n");
                for (OCBReconciliation ocb : list) {
                    writer.append(ocb.toString()).append("\n");
                }
                writer.close();
            }
            return true;
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.info(ExceptionUtils.getStackTrace(e));
                }
            }
        }

        return false;
    }

    private boolean deleteCrossCheckFile(){
        File file = new File(filePath);
        return file.delete();
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(yesterday());
    }
}
