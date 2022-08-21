/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.vib.schedule;

import gateway.core.channel.tcb.service.schedule.ResponseDataVIBIBFT;
import gateway.core.util.EmailUtil;
import gateway.core.util.PGUtil;
import gateway.core.util.SFTPUploader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author taind
 */
@Service
@Component("VIBIBFTScheduleService")
public class VIBIBFTScheduleService {
    private static final Logger logger = LogManager.getLogger(VIBIBFTScheduleService.class);

    @Scheduled(cron = "0 0 9 * * ?")
//    @Scheduled(fixedDelay = 100000000)
    public void doDayReconciliationVIBIBFT() throws Exception {
        System.out.println("run cron VIB IBFT START!!! - " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final String month = (cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : "" + cal.get(Calendar.MONTH) + 1;
        final String day = (cal.get(Calendar.DAY_OF_MONTH)) < 10 ? "0" + (cal.get(Calendar.DAY_OF_MONTH)) : "" + cal.get(Calendar.DAY_OF_MONTH);
        String dateFile = year + month + day;
        String dateFormat = day + "/" + month + "/" + year;

        // download file DoiSoat
        String remoteFileName = "VIB_NGL_" + dateFile + "_ERPLink_Doisoat.txt";
        String localFileName = "VIB_NGL_" + dateFile + "_ERPLink_Doisoat.csv";
        String folderDownloadFile = "/data/www/data.nganluong.vn/banks/vib_ibft/";
        String folderWriteFile = "/data/tomcat/sftp/vib_ibft/out/";
        SFTPUploader.downloadVIBIBFT(remoteFileName, folderDownloadFile);
        // convert file csv
        List<ResponseDataVIBIBFT> list = convertFile(folderDownloadFile, remoteFileName);
        // header file csv
        String[] headerCsv = new String[]{"LOAI BAN GHI", "MA DOI SOAT", "LOAI GD", "TK CHUYEN", "TK NHAN",
            "SO TIEN", "MA TIEN TE", "NOI DUNG GD", "GIO GD", "NGAY GD", "SO GIAY PHEP GD", "SO TRACE NL", "PHI GD", "KET QUA TU NAPAS",
            "KET QUA DOI SOAT", "CHECKSUM"};
        // write file csv
        createCSVFile(folderWriteFile + localFileName, headerCsv, list);
        // send mail file csv
        EmailUtil.sendMail(folderWriteFile + localFileName, "Đối soát VIB IBFT ngày " + dateFormat, 
                "Đối soát VIB IBFT ngày " + dateFormat, "withdraw@nganluong.vn", "");
        EmailUtil.sendMail(folderWriteFile + localFileName, "Đối soát VIB IBFT ngày " + dateFormat, 
                "Đối soát VIB IBFT ngày " + dateFormat, "taind@peacesoft.net", "");
        System.out.println("run cron VIB IBFT END!!! - " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
    }

    private List<ResponseDataVIBIBFT> convertFile(String remotePath, String fileIn) {
        File file = new File(remotePath + fileIn);
        BufferedReader br = null;
        List<ResponseDataVIBIBFT> listDatas = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = br.readLine()) != null) {
                String splitarray[] = text.split("\\|");
                if (splitarray.length > 6) {
                    ResponseDataVIBIBFT data = new ResponseDataVIBIBFT(splitarray);
                    listDatas.add(data);
                }
            }
        } catch (FileNotFoundException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                logger.info(ExceptionUtils.getStackTrace(e));
            }
        }
        return listDatas;
    }

    public boolean createCSVFile(String fileName, String[] header, List<ResponseDataVIBIBFT> listData) throws IOException {
        FileWriter out = new FileWriter(fileName);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(header))) {
            for (ResponseDataVIBIBFT data : listData) {
                printer.printRecord(data.getType(), data.getCompareCode(), data.getTransactionType(), data.getAccountSender(), data.getAccountSender(),
                        data.getAmount(), data.getCurrency(), data.getDescription(), data.getTime(), data.getDate(), data.getAuthenNumber(),
                        data.getTraceNL(), data.getFee(), data.getNapasResponse(), data.getResultCompare(), data.getChecksum());
            }
            return true;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }
}
