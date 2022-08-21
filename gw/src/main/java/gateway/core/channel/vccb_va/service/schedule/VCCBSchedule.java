package gateway.core.channel.vccb_va.service.schedule;

import com.jcraft.jsch.SftpException;
import gateway.core.util.EmailUtil;
import gateway.core.util.SFTPFunction;
import gateway.core.util.SFTPUploader;
import gateway.core.util.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author dungla
 *
 * Class thực hiện việc lấy file đối soát từ sftp format và gửi cho vận hành
 * 1. File lấy được là file thuần text sẽ được convert sang csv
 * 2. Việc gửi file sẽ được chia làm 5 giai đoạn theo yêu cầu của vận hành.
 *  - Giai đoạn 1: Lấy file từ 2 am to 8 am - chạy vào 8h10 am
 *  - Giai đoạn 2: Lấy file từ 9 am to 12 pm - chạy vào 12h10 am
 *  - Giai đoạn 3: Lấy file từ 13 pm to 16 pm - chạy vào 16h10 am
 *  - Giai đoạn 4: Lấy file từ 17 pm to 21 pm - chạy vào 22h10 am
 *  - Giai đoạn 5: Lấy file từ 22 pm to 1 am - chạy vào 1h10 am
 */

@Service
public class VCCBSchedule {
    private static final String PATH = "/in/";
    private static final String DIR = "/data/doisoat/ban_viet/";
    private static final Logger logger = LogManager.getLogger(VCCBSchedule.class);
    /**
     * Giai đoạn 1: Lấy file từ 2 am to 8 am - chạy vào 8h10 am
     */
    @Scheduled(cron = "0 35 8 * * ?")
    public void phaseOne(){
        String[] list = {"02","03","04","05","06","07","08"};
        this.pathBuilder(list);
    }

    /**
     * Giai đoạn 2: Lấy file từ 9 am to 12 pm - chạy vào 12h10 am
     */
    @Scheduled(cron = "0 35 12 * * ?")
    public void phaseTwo(){
        String[] list = {"09","10","11","12"};
        this.pathBuilder(list);
    }

    /**
     * Giai đoạn 3: Lấy file từ 13 pm to 16 pm - chạy vào 16h10 am
     */
    @Scheduled(cron = "0 35 16 * * ?")
    public void phaseThree(){
        String[] list = {"13","14","15","16"};
        this.pathBuilder(list);
    }

    /**
     * Giai đoạn 4: Lấy file từ 17 pm to 21 pm - chạy vào 21h10 am
     */
    @Scheduled(cron = "0 35 21 * * ?")
    public void phaseFour(){
        String[] list = {"17","18","19","20","21"};
        this.pathBuilder(list);
    }

    /**
     * Giai đoạn 5: Lấy file từ 22 pm to 1 am - chạy vào 1h10 am
     */
    @Scheduled(cron = "0 35 1 * * ?")
    public void phaseFive(){
        String[] list = {"22","23","00","01"};
        this.pathBuilder(list);
    }

    /**
     * Lấy file T - 1
     */
    @Scheduled(cron = "0 30 8 * * ?")
    public void yesterdayFile(){
        this.pathBuilder();
    }

    private void pathBuilder() {
        String day = TimeUtil.getYesterday("yyyMMdd");
        List<String> fileNames = new ArrayList<>();
        fileNames.add("VA_NGLG_All_" + day + ".txt");
        String fileMake = "VA_NGLG_All_" + day +".csv";
        this.execute(fileNames, fileMake, false);
    }

    private void pathBuilder(String[] list){
        List<String> fileNames = new ArrayList<>();
        for(String name: list){
            String time;
            if(name.equals("22") || name.equals("23")) {
                time = TimeUtil.getYesterday("yyyMMdd");
            }else{
                time = TimeUtil.getCurrentTime("yyyMMdd");
            }

            fileNames.add("VA_NGLG_" + time + name + ".txt");
        }
        String fileMake = "VA_NGLG_" + TimeUtil.getCurrentTime("yyyMMdd")+ list[0] + "-" + list[list.length-1] +".csv";
        this.execute(fileNames, fileMake, true);
    }

    private void execute(List<String> fileNames, String fileDone, boolean phase){
        logger.info("VCCB - VA - SCHEDUALE - BEGIN: "+ TimeUtil.getCurrentTime("dd-MM-yyy hh:mm:sss"));
        ClassLoader classLoader = VCCBSchedule.class.getClassLoader();
        String fileAuth = Objects.requireNonNull(classLoader.getResource("vccb_key/sftp_banviet_1")).getFile();
        SFTPUploader.downloadSftp("sftp_banviet","10.0.14.10",4122, fileAuth, this.getFunction(fileNames,fileDone));
        String subject;
        if (phase) {
            subject =  "Hệ thống gửi file đối soát dịch vụ Virtual Account của Bản Việt Bank (theo giờ) ngày " + TimeUtil.getCurrentTime("dd/MM/yyy hh:mm:ss");
        } else {
            subject = "Hệ thống gửi file đối soát dịch vụ Virtual Account của Bản Việt Bank (theo ngày) ngày " + TimeUtil.getYesterday("dd/MM/yyy");
        }
        logger.info("VCCB - VA - SCHEDUALE - MAIL - SENDING: "+ TimeUtil.getCurrentTime("dd-MM-yyy hh:mm:sss"));
        EmailUtil.sendMail(DIR + fileDone,subject,subject,EmailUtil.RECONCILIATION,"");
        logger.info("VCCB - VA - SCHEDUALE - END: "+ TimeUtil.getCurrentTime("dd-MM-yyy hh:mm:sss"));
    }

    private SFTPFunction getFunction(List<String> list, String fileDone){
        return channel -> {
            channel.cd(PATH);
            File file = new File(DIR+fileDone);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fr = new FileWriter(file,true);
            fr.write("External RefNo,Transaction Date,Partner Code,Related Account,Credit Account,Amount,CCY,XXXX,Receipt\n");
            for(String filename:list){
                try(InputStream stream = channel.get(filename);
                    BufferedReader br = new BufferedReader(new InputStreamReader(stream))){

                    String line;
                    while ((line = br.readLine() ) != null){
                        LineMaker lineMaker = new LineMaker(line);
                        System.out.println(lineMaker);
                        String a = lineMaker.toString();
                        fr.write(a);
                    }
                }catch (SftpException | IOException e){
                    e.printStackTrace();
                }
            }
            fr.close();
        };
    }
}
