package gateway.core.channel.tcb.service.schedule;

import gateway.core.channel.tcb.dto.TCBConstants;
import gateway.core.util.EmailUtil;
import gateway.core.util.PGUtil;
import gateway.core.util.SFTPUploader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.YearMonth;
import java.util.Calendar;

/**
 * @author taind
 */
@Service
public class TCBScheduleService {
    private static final Logger logger = LogManager.getLogger(TCBScheduleService.class);
    public static final String PREDIX = "DOI SOAT TCB ";

    @Scheduled(cron = "0 10 12 * * ?")
    public void doDownloadFile() {
        System.out.println(PREDIX + "run cron TCB START!!! - " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
        try {
            Calendar cal = Calendar.getInstance();
            final int year = cal.get(Calendar.YEAR);
            String month = ((cal.get(Calendar.MONTH) + 1) < 10) ? "0" + ((cal.get(Calendar.MONTH) + 1)) : "" + ((cal.get(Calendar.MONTH)) + 1);
            String day = ((cal.get(Calendar.DAY_OF_MONTH)) - 1) < 10 ? ("0" + (cal.get(Calendar.DAY_OF_MONTH) - 1)) : "" + (cal.get(Calendar.DAY_OF_MONTH) - 1);
            final String hour = cal.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + cal.get(Calendar.DAY_OF_MONTH)) : "" + cal.get(Calendar.DAY_OF_MONTH);
            final String minute = cal.get(Calendar.MINUTE) < 10 ? ("0" + cal.get(Calendar.MINUTE)) : "" + cal.get(Calendar.MINUTE);
            final String second = cal.get(Calendar.SECOND) < 10 ? ("0" + cal.get(Calendar.SECOND)) : "" + cal.get(Calendar.SECOND);
            if (day.equals("00")) {
                YearMonth yearMonth = YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
                int lastDay = yearMonth.lengthOfMonth();
                day = String.valueOf(lastDay);
                month = String.valueOf(Integer.parseInt(month) - 1);
                month = month.length() < 2 ? "0" + month : "" + month;
            }
            String dateFileName = year + month + day;
            String rootRemote = TCBConstants.SFTP_LOCAL_PATH;
            String encryptFile = TCBConstants.SFTP_ENCRYPT_FILE_PATH;
            String remoteFileName = "TCB_NGLUONG_19026410788055_" + dateFileName + ".CSV.pgp";
            String fileSendMail = "TCB_NGLUONG_19026410788055_" + dateFileName + ".CSV";
            System.out.println(PREDIX + "download file : " + remoteFileName);
            String downFileStt = SFTPUploader.downloadTCB(remoteFileName, encryptFile);
            if (downFileStt.equals("success")) {
                // Giải mã
                System.out.println(PREDIX + "decrypt file download");
                final String secKey = rootRemote + "key/pgp-key-sec.asc";
                final String encryptFolder = rootRemote + "encrypt/";
                final String decryptFolder = rootRemote + "decrypt/";
                final String archiveFolder = rootRemote + "archive/";
                final String partnerPassphrase = "ywy1CVPmcxl44t7nF0ib";

                final String curdate = year + month + day;
                final String curtime = hour + ":" + minute + ":" + second;
                // Start do decrypt
                System.out.println(PREDIX + "++++++++++++++++++++++++++++++ Date: " + curdate + " and Time: " + curtime + " ++++++++++++++++++++++++++++");
                System.out.println(PREDIX + "Looping through all files in " + encryptFolder + ", looking for pgp files to decrypt");
                System.out.println(PREDIX + "---------------------------------------- Thread Start ---------------------------------------------");
                // Add Bouncy Castle Provider
                Security.addProvider(new BouncyCastleProvider());

                // Partner decrypt files (*.pgp) in encrypt folder with partner secret key
                boolean decryptSuccess = OpenPGPDecryption.partnerDecryptReconfiles(encryptFolder, decryptFolder, secKey, partnerPassphrase);
                if (!decryptSuccess) {
                    System.out.println(PREDIX + "---------------Thread End (Decrypt Error)-----------------");
                    return;
                }
                OpenPGPDecryption.archiveReconfiles(encryptFolder, archiveFolder, curdate);
                System.out.println(PREDIX + "decrypt file download done");
                // gửi mail
                System.out.println(PREDIX + " send mail");
                String subjectMail = "Hệ thống gửi file đối soát IBFT Techcombank ngày " + day + "/" + month + "/" + year;
                String contentMail = "Hệ thống gửi file đối soát IBFT Techcombank ngày " + day + "/" + month + "/" + year;
                String partFile = rootRemote + "decrypt/" + fileSendMail;
                EmailUtil.sendMail(partFile, subjectMail, contentMail, "withdraw@nganluong.vn", "");
                System.out.println(PREDIX + "send mail done");
            } else {
                System.out.println(PREDIX + "failed");
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            System.out.println(PREDIX + e.getMessage());
        }
        System.out.println(PREDIX + "run cron TCB SUCCESS!!! - " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
    }
}