//package gateway.core.channel.napas.doi_soat.scheduling_config;
//
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.SftpException;
//import org.springframework.stereotype.Component;
//import pg.channel.napas_payment_v3.NapasPaymentV3;
//
//import java.io.IOException;
//
//@Component("Scheduling")
//public class Scheduling {
//
////    @Scheduled(cron = "0 * * ? * *")
//    public static void cronDownloadFile() throws JSchException, SftpException, IOException {
//        try {
//            NapasPaymentV3.cronDownloadFile();
//        } catch (Exception e) {
//            logger.info(ExceptionUtils.getStackTrace(e));
//        }
//    }
//
////    @Scheduled(cron = "0 * * ? * *")
//    public static void cronDownloadFileNapas() {
//        try {
//            NapasPaymentV3.doiSoat();
//        } catch (Exception e) {
//            logger.info(ExceptionUtils.getStackTrace(e));
//        }
//    }
//
//}
