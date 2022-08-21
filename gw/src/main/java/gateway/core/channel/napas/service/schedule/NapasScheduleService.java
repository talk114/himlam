package gateway.core.channel.napas.service.schedule;

import com.google.common.base.Strings;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.napas.doi_soat.*;
import gateway.core.channel.napas.dto.response.PurchaseOtpRes;
import gateway.core.util.EmailUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.PaymentService;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static gateway.core.channel.napas.doi_soat.DoiSoatConstants.*;

/**
 * @author sonln
 */
@Service
@Component("NapasScheduleService")
public class NapasScheduleService extends PaymentGate {

    private static final Logger logger = LogManager.getLogger(NapasScheduleService.class);
    public static final String PREDIX = "DOI SOAT NAPAS ";
    @Autowired
    private PaymentService paymentService;

    //    @Scheduled(cron = "0 10 10 * * ?")
    public void doiSoat() {
        try {
            WriteInfoLog(PREDIX + formatDateTimeByPattern("dd/MM/YYYY-HH:mm:ss", String.valueOf(System.currentTimeMillis())));
            // Download file Napas
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            String dayOfMonth = day + "";
            if (day < 10) {
                dayOfMonth = "0" + day;
            }
            String monthOfYear = month + "";
            if (month < 10) {
                monthOfYear = "0" + month;
            }
            String yearOfYear = year + "";
            String fileDay = monthOfYear + "" + dayOfMonth + "" + yearOfYear.substring(2, 4);
            String fileName = fileDay + "_ACQ_NGANLUONGCE_971019_1_TC_ECOM.dat.pgp";
            String filePath = LOCAL_PATH_DOWNLOAD + File.separator + fileName;
            File readFileNapas = new File(filePath);
            WriteInfoLog(PREDIX + "TAI FILE NAPAS : " + fileName);
            SFTPServer.downloadSFTPNapas(readFileNapas, REMOTE_PATH, fileName);
            WriteInfoLog(PREDIX + "TAI FILE TC THANH CONG : " + fileName);
            // Giai ma File Napasf
            String fileNameDecode = "DECODE_" + fileName;
            String filePathAfterDecode = LOCAL_PATH_DECODE + File.separator + fileNameDecode;
            PGPas.PGPdecrypt(filePath, filePathAfterDecode, PRIVATE_KEY_DECRYPT_PATH);
            WriteInfoLog(PREDIX + "DECODE FILE TC : " + filePathAfterDecode);
            // Get data file Napas After decode
            Set<String> daysRec = new LinkedHashSet<>();
            FileGDSuccessNapas dataTC = readFileAsString(filePathAfterDecode, daysRec);
            WriteInfoLog(PREDIX + "DATA TC FILE: " + objectMapper.writeValueAsString(dataTC));
            //create csv
            String fileTCCsv = LOCAL_PATH_CSV + File.separator + fileDay + "_ACQ_NGANLUONGCE_971019_1_TC_ECOM.csv";
            Map<String, List<DoiSoatDetails>> dataTCCSV = new HashMap<>();
            dataTCCSV.put("ORIGINAL DATA NAPAS", dataTC.getDetails());
            createCSVFile(fileTCCsv, dataTCCSV);
            WriteInfoLog(PREDIX + "FILE CSV TC" + fileTCCsv);
            // get data gw
            List<Payment> dataGwSucces = getDataGw(daysRec);
            WriteInfoLog(PREDIX + "DATA GW SUCCESS: " + objectMapper.writeValueAsString(dataGwSucces));
            //  convert to rec item
            List<DoiSoatDetails> doiSoatNLDetails = convertTrxNLNapasInfoToDoiSoat(dataGwSucces);
            //  Loc giao dich sai lech giua Ngan Luong va Napas:
            Map<String, List<DoiSoatDetails>> data = filter(dataTC, doiSoatNLDetails);
            WriteInfoLog(PREDIX + "LIST GD SAI LECH: " + objectMapper.writeValueAsString(data));
            // write file csv
            String fileSLCsv = LOCAL_PATH_CSV + File.separator + fileDay + "_ACQ_NGANLUONGCE_971019_1_SL_ECOM.csv";
            createCSVFile(fileSLCsv, data);
            WriteInfoLog(PREDIX + "FILE CSV SL" + fileSLCsv);
            // tạo file giao dịch sai lệch gửi napas va Ma hoa File
            String fileNameBeforeEncode = fileDay + "_ACQ_NGANLUONGCE_971019_1_SL_ECOM.dat";
            String fileNameAfterEncode = fileDay + "_ACQ_NGANLUONGCE_971019_1_SL_ECOM.dat.pgp";
            createFileGDSaiLech(data, fileNameBeforeEncode, fileNameAfterEncode);
            // upload file sai lệch lên SFTP cho Napas
            File fileEncodeUpload = new File(LOCAL_PATH_ENCODE + File.separator + fileNameAfterEncode);
            SFTPServer.uploadSFTPNapas(fileEncodeUpload, REMOTE_PATH_UPLOAD + fileNameAfterEncode);
            WriteInfoLog(PREDIX + "UPLOAD TO NAPAS SUCCESS");
            //  get file XL
            FileGDSuccessNapas dataXL = getFileXL(fileDay);
            String fileXLCsv = LOCAL_PATH_CSV + File.separator + fileDay + "_ACQ_NGANLUONGCE_971019_1_XL_ECOM.csv";
            Map<String, List<DoiSoatDetails>> dataXLCsv = new HashMap<>();
            dataXLCsv.put("DATA XL NAPAS", dataXL.getDetails());
            createCSVFile(fileXLCsv, dataXLCsv);

            WriteInfoLog(PREDIX + "FILE CSV XL" + fileXLCsv);
            //  send mail
            String subjectMail = "Hệ thống gửi file đối soát NAPAS CASHIN ngày " + day + "/" + month + "/" + year;
            String contentMail = "Hệ thống gửi file đối soát NAPAS CASHIN ngày " + day + "/" + month + "/" + year;
            String filesSendMail = fileTCCsv + "," + fileSLCsv + "," + fileXLCsv;
            EmailUtil.sendMail(filesSendMail.split(","), subjectMail, contentMail, "sonln@nganluong.vn", "luongdt@nganluong.vn,vantt2@peacesoft.net");
            WriteInfoLog(PREDIX + "Send mail done");
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            WriteInfoLog(PREDIX + "DOI SOAT KHONG THANH CONG " + e.getMessage());
        }

    }

    private FileGDSuccessNapas getFileXL(String fileDay) {
        FileGDSuccessNapas dataFile = new FileGDSuccessNapas();
        try {
            String fileName = fileDay + "_ACQ_NGANLUONGCE_971019_1_XL_ECOM.dat.pgp";//fileDay+"_ACQ_NGANLUONGCE_971019_1_TC_ECOM.dat.pgp";
            String filePath = LOCAL_PATH_DOWNLOAD + File.separator + fileName;
            WriteInfoLog(PREDIX + "DOWNLOAD FILE XL : " + filePath);
            File readFileNapas = new File(filePath);
            SFTPServer.downloadSFTPNapas(readFileNapas, REMOTE_PATH, fileName);
            String fileNameDecode = "DECODE_" + fileName;
            String filePathAfterDecode = LOCAL_PATH_DECODE + File.separator + fileNameDecode;
            WriteInfoLog(PREDIX + "FILE XL DECODE");
            PGPas.PGPdecrypt(filePath, filePathAfterDecode, PRIVATE_KEY_DECRYPT_PATH);
            // Get data file Napas After decode
            dataFile = readFileAsString(filePathAfterDecode, null);
            WriteInfoLog(PREDIX + "DATA FILE XL: " + objectMapper.writeValueAsString(dataFile));
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            WriteInfoLog(PREDIX + "GET FILE XL FAILED: " + e.getMessage());
        }
        return dataFile;

    }

    /**
     * tạo file GD sai lệch và mã hóa file bằng public_key của Napas
     **/
    private static void createFileGDSaiLech(Map<String, List<DoiSoatDetails>> data, String fileNameBeforeEncode, String fileNameAfterEncode) throws Exception {
        List<DoiSoatDetails> successInNapasAndNotSuccessInNGL = data.get("SUCCESS_IN_NAPAS_NOT_SUCCESS_IN_NL"); // nhung GD thanh cong tren Napas nhung k thanh cong tren NGL
        List<DoiSoatDetails> successInNGLAndNotSuccessInNapas = data.get("SUCCESS_IN_NL_NOT_SUCCESS_IN_NAPAS"); //// nhung GD thanh cong tren NGL nhung k thanh cong tren Napas
        List<DoiSoatDetails> listGDSaiLech = new ArrayList<>();
        listGDSaiLech.addAll(successInNapasAndNotSuccessInNGL);
        listGDSaiLech.addAll(successInNGLAndNotSuccessInNapas);
        //Sap xep cac giao dich theo create_time
        listGDSaiLech.sort((doiSoatDetails, t1) -> {
            if (doiSoatDetails.getTransactionDate() != null && t1.getTransactionDate() != null) {
                long dateTime = dateToLong(doiSoatDetails.getTransactionDate(), "MMddyyyy");
                long dateTime1 = dateToLong(t1.getTransactionDate(), "MMddyyyy");
                if (dateTime > dateTime1) return 1;
                else return -1;
            }
            return 0;
        });
        //HEADER
        Header headerFile = new Header();
        headerFile.setReceiverBankCode(RECEIVER_BANK_CODE);
        if (listGDSaiLech.size() != 0) {
            headerFile.setLastTransactionDate(String.valueOf(dateToLong(listGDSaiLech.get(listGDSaiLech.size() - 1).getTransactionDate(), "MMddyyyy")));
        } else {
            headerFile.setLastTransactionDate(String.valueOf(System.currentTimeMillis()));
        }

        //TRAILER
        Trailer trailerFile = new Trailer();
        trailerFile.setNumberOfTransactions(String.valueOf(listGDSaiLech.size()));
        trailerFile.setFileCreater(CREATER_FILE);
        trailerFile.setTimeCreatFile(String.valueOf(System.currentTimeMillis()));
        trailerFile.setDateCreateFile(String.valueOf(System.currentTimeMillis()));

        DataForDoiSoat dataForDoiSoat = new DataForDoiSoat();
        dataForDoiSoat.setHeader(headerFile.toString());
        dataForDoiSoat.setDetails(dataWriteFile(listGDSaiLech));
        dataForDoiSoat.setTrailer(trailerFile.toString());

        writeFile(fileNameBeforeEncode, LOCAL_PATH_DECODE, dateForWriteFile(dataForDoiSoat));
        // mã hóa file
        WriteInfoLog(PREDIX + "MA HOA FILE SAI LECH : " + fileNameBeforeEncode);
        PGPas.PGPencrypt(LOCAL_PATH_DECODE + File.separator + fileNameBeforeEncode,
                LOCAL_PATH_ENCODE + File.separator + fileNameAfterEncode, PUBLIC_KEY_ENCYPT_PATH);
        WriteInfoLog(PREDIX + "MA HOA FILE SAI LECH XONG : : " + fileNameAfterEncode);
    }

    /**
     * lọc các giao dịch sai lệch giữa Ngân Lượng và Napas
     *
     * @param dataNapasSuccess: List cac dịch thành công của Ngân Lượng qua Napas
     * @param dataNapasSuccess: List cac giao dich thanh cong cua Napas
     **/
    private static Map<String, List<DoiSoatDetails>> filter(FileGDSuccessNapas dataNapasSuccess, List<DoiSoatDetails> dataGwSuccess) {
        Map<String, List<DoiSoatDetails>> mappingData = new HashMap<>();
        List<DoiSoatDetails> listDataNapasSuccess = dataNapasSuccess.getDetails();
        List<DoiSoatDetails> listDataNglSuccess = dataGwSuccess;

        List<DoiSoatDetails> successInNapasAndNotSuccessInNGL = new ArrayList<>(); // nhung GD thanh cong tren Napas nhung k thanh cong tren NGL
        List<DoiSoatDetails> successInNGLAndNotSuccessInNapas = new ArrayList<>(); //// nhung GD thanh cong tren NGL nhung k thanh cong tren Napas
        if (listDataNapasSuccess != null) {
            for (DoiSoatDetails napasSuccess : listDataNapasSuccess) {
                if (!listDataNglSuccess.contains(napasSuccess)) {
                    napasSuccess.setReconciliationResponseCode("0115");
                    successInNapasAndNotSuccessInNGL.add(napasSuccess);
                }
            }
        }
        mappingData.put("SUCCESS_IN_NAPAS_NOT_SUCCESS_IN_NL", successInNapasAndNotSuccessInNGL);
        if (listDataNglSuccess != null) {
            for (DoiSoatDetails nlSuccess : listDataNglSuccess) {
                if (!listDataNapasSuccess.contains(nlSuccess)) {
                    nlSuccess.setReconciliationResponseCode("0117");
                    successInNGLAndNotSuccessInNapas.add(nlSuccess);
                }
            }
        }

        mappingData.put("SUCCESS_IN_NL_NOT_SUCCESS_IN_NAPAS", successInNGLAndNotSuccessInNapas);
        return mappingData;
    }

    /**
     * Lấy các GD Thành công của Napas trong file
     **/
    public static FileGDSuccessNapas readFileAsString(String fileName, Set<String> dayRec) throws Exception {
        FileGDSuccessNapas fileGDSuccessNapas = new FileGDSuccessNapas();
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        // verify checksum file
        String checksumFile = data.substring(data.indexOf("[CSF]") + 5, data.indexOf("[CSF]") + 37);
        WriteInfoLog("############# Checksum File : " + checksumFile + "#########################");
        String dataForVerifyChecksum = data.substring(data.indexOf("TR[NOT]"), data.indexOf("[CSF]") + 5);
        String checkSum = checkSum(dataForVerifyChecksum, SECRET_KEY);
        WriteInfoLog("############# Data Checksum File : " + dataForVerifyChecksum + "#########################");
        WriteInfoLog("############# Checksum: " + checkSum + "#########################");

        if (checkSum.equals(checksumFile)) {
            List<String> dataAfterSplit = Arrays.asList(data.split("\n"));
            //header
            Header header = new Header();
            String dataHeader = dataAfterSplit.get(0);
            header.setReceiverBankCode(dataHeader.substring(dataHeader.indexOf("[REV]") + 5, dataHeader.indexOf("[REV]") + 13).trim());
            header.setLastTransactionDate(dataHeader.substring(dataHeader.indexOf("[DATE]") + 6, dataHeader.indexOf("[DATE]") + 14).trim());
            //trail
            Trailer trailer = new Trailer();
            String dataTrailer = dataAfterSplit.get(dataAfterSplit.size() - 1);
            trailer.setNumberOfTransactions(dataTrailer.substring(dataTrailer.indexOf("[NOT]") + 5, dataTrailer.indexOf("[NOT]") + 14));
            trailer.setFileCreater(dataTrailer.substring(dataTrailer.indexOf("[CRE]") + 5, dataTrailer.indexOf("[CRE]") + 25).trim());
            trailer.setTimeCreatFile(dataTrailer.substring(dataTrailer.indexOf("[TIME]") + 6, dataTrailer.indexOf("[TIME]") + 12));
            trailer.setDateCreateFile(dataTrailer.substring(dataTrailer.indexOf("[DATE]") + 6, dataTrailer.indexOf("[DATE]") + 14));
            trailer.setChecksumFile(dataTrailer.substring(dataTrailer.indexOf("[CSF]") + 5, dataTrailer.indexOf("[CSF]") + 37));
            fileGDSuccessNapas.setHeader(header);
            fileGDSuccessNapas.setTrailer(trailer);
            //Details
            List<DoiSoatDetails> details = new ArrayList<>();
            for (int i = 1; i < dataAfterSplit.size(); i++) {
                DoiSoatDetails detail = new DoiSoatDetails();
                Field[] fields = detail.getClass().getDeclaredFields();
                String dataDetail = dataAfterSplit.get(i);
                // verify checksum line
                if (dataDetail.contains("DR[MTI]")) {
                    String dataChecksumForVerify = dataDetail.substring(0, dataDetail.indexOf("[CSR]") + 5);
                    String checksumLine = (dataDetail.substring(dataDetail.indexOf("[CSR]") + 5, dataDetail.indexOf("[CSR]") + 37).trim());
                    if (checkSum(dataChecksumForVerify, SECRET_KEY).equals(checksumLine)) {
//                        String notUsed = dataDetail.substring(dataDetail.indexOf("[SVFISSNP]"), dataDetail.indexOf("[F37]"));
//                        String dataGetValue = dataDetail.replace(notUsed, "");
                        String[] valueArray = dataDetail.split("\\[");
                        Map map = new JSONObject(objectMapper.writeValueAsString(detail)).toMap();
                        for (int valueNum = 1; valueNum < valueArray.length; valueNum++) {
                            for (int numF = valueNum; numF < fields.length; numF++) {
                                String key = fields[numF].getName();
                                map.put(key, StringUtils.substringAfterLast(valueArray[valueNum], "]"));
                                break;
                            }
                        }
                        detail = objectMapper.readValue(objectMapper.writeValueAsString(map), DoiSoatDetails.class);
                        if (dayRec != null) {
                            dayRec.add(detail.getTransactionDate());
                        }
                        details.add(detail);
                    }
                }
            }
            fileGDSuccessNapas.setDetails(details);
        }
        return fileGDSuccessNapas;
    }

    public static String formatDateTimeByPattern(String pattern, String time) {
        Date date = new Date(Long.parseLong(time));
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String genWhiteSpaceOrZero(String input, int numberSpace, String whiteSpaceOrZero) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberSpace; i++) {
            if (whiteSpaceOrZero.equals("WHITE_SPACE")) {
                result.append(" ");
            } else if (whiteSpaceOrZero.equals("ZERO")) {
                result.append("0");
            }
        }
        result.append(input);
        return result.toString();
    }

    public static long dateToLong(String date, String fomat) {
        String year = formatDateTimeByPattern("yyyy", String.valueOf(System.currentTimeMillis()));
        long milliseconds = -1;
        SimpleDateFormat f = new SimpleDateFormat(fomat);
        try {
            Date d = f.parse(date + year);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return milliseconds;
    }

    private static String dataWriteFile(List<DoiSoatDetails> doiSoatDetails) throws IllegalAccessException, NoSuchAlgorithmException {
        Field[] fields = DoiSoatDetails.class.getDeclaredFields();
        StringBuilder dataFile = new StringBuilder();
        for (DoiSoatDetails element : doiSoatDetails) {
            StringBuilder data = new StringBuilder();
            data.append("DR[MTI]").append(element.getMTI());
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("cardNumber")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F2]").append(genWhiteSpaceOrZero(value, 19 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("processingCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F3]").append(genWhiteSpaceOrZero(value, 6 - value.length(), ZERO));
                }
                if (field.getName().equals("serviceCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[SVC]").append(genWhiteSpaceOrZero(value, 10 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("transactionChannelCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[TCC]").append(genWhiteSpaceOrZero(value, 2 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("transactionAmount")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F4]").append(genWhiteSpaceOrZero(value, 12 - value.length(), ZERO));
                }
                if (field.getName().equals("realTransactionAmount")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[RTA]").append(genWhiteSpaceOrZero(value, 12 - value.length(), ZERO));
                }
                if (field.getName().equals("currencyCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F49]").append(genWhiteSpaceOrZero(value, 3 - value.length(), ZERO));
                }
                if (field.getName().equals("amountSettlement")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F5]").append(genWhiteSpaceOrZero(value, 12 - value.length(), ZERO));
                }
                if (field.getName().equals("amountSettlementCurrencyCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F50]").append(genWhiteSpaceOrZero(value, 3 - value.length(), ZERO));
                }
                if (field.getName().equals("rate")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F9]").append(genWhiteSpaceOrZero(value, 8 - value.length(), ZERO));
                }
                if (field.getName().equals("amountOfCardholder")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F6]").append(genWhiteSpaceOrZero(value, 12 - value.length(), ZERO));
                }
                if (field.getName().equals("realCardHolderAmount")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[RCA]").append(genWhiteSpaceOrZero(value, 12 - value.length(), ZERO));
                }
                if (field.getName().equals("currencyCodeCardHolder")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F51]").append(genWhiteSpaceOrZero(value, 3 - value.length(), ZERO));
                }
                if (field.getName().equals("rateAmountCardHolder")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F10]").append(genWhiteSpaceOrZero(value, 8 - value.length(), ZERO));
                }
                if (field.getName().equals("systemTrace")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F11]").append(genWhiteSpaceOrZero(value, 6 - value.length(), ZERO));
                }
                if (field.getName().equals("transactionTime")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F12]").append(value);
                }
                if (field.getName().equals("transactionDate")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F13]").append(value);
                }
                if (field.getName().equals("settlementDate")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F15]").append(genWhiteSpaceOrZero(value, 4 - value.length(), ZERO));
                }
                if (field.getName().equals("agencyCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F18]").append(AGENCY_CODE_TYPE);
                }
                if (field.getName().equals("PANNumber")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F22]").append(genWhiteSpaceOrZero(value, 3 - value.length(), ZERO));
                }
                if (field.getName().equals("conditionalCodeAtServiceAccessPoint")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F25]").append(genWhiteSpaceOrZero(value, 2 - value.length(), ZERO));
                }
                if (field.getName().equals("agencyCodeIdentify")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F41]").append(genWhiteSpaceOrZero(value, 8 - value.length(), ZERO));
                }
                if (field.getName().equals("acquirerIdentify")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[ACQ]").append(genWhiteSpaceOrZero(value, 8 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("issuerIdentify")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[ISS]").append(genWhiteSpaceOrZero(value, 8 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("identifyCodeAcceptsCard")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[MID]").append(genWhiteSpaceOrZero(value, 15 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("beneficiaryIdentify")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[BNB]").append(genWhiteSpaceOrZero(value, 8 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("accountNumberMaster")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F102]").append(genWhiteSpaceOrZero(value, 28 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("destinationAccountNumberOrCardNumber")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F103]").append(genWhiteSpaceOrZero(value, 28 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("retrievalReferenceNumber")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F37]").append(genWhiteSpaceOrZero(value, 12 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("authorizationNumber")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[F38]").append(genWhiteSpaceOrZero(value, 6 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("transactionReferenceNumber")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[TRN]").append(genWhiteSpaceOrZero(value, 16 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("reconciliationResponseCode")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[RRC]").append(genWhiteSpaceOrZero(value, 4 - value.length(), ZERO));
                }
                if (field.getName().equals("reserveInformation1")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[RSV1]").append(genWhiteSpaceOrZero(value, 100 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("reserveInformation2")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[RSV2]").append(genWhiteSpaceOrZero(value, 100 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("reserveInformation3")) {
                    String value = (String) field.get(element);
                    if (Strings.isNullOrEmpty(value)) value = "";
                    data.append("[RSV3]").append(genWhiteSpaceOrZero(value, 100 - value.length(), WHITE_SPACE));
                }
                if (field.getName().equals("checksumLine")) {
                    data.append("[CSR]");
                    // GEN CHECKSUM
                    String checksum = checkSum(data.toString(), SECRET_KEY);
                    data.append(genWhiteSpaceOrZero(checksum, 32 - checksum.length(), WHITE_SPACE));
                }
            }
            dataFile.append(data).append("\n");
        }
        return dataFile.toString();
    }

    public static void writeFile(String fileName, String dirPath, String data) throws IOException {

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }
        try (OutputStream out = new FileOutputStream(filePath);
             Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            writer.write(data);
        }
    }

    static String checkSum(String content, String key) throws NoSuchAlgorithmException {
        String str = "";
        String strMa = md5(content);

        String strKQ = strMa;
        key = "5" + key + "5";
        char[] chars = key.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            int idx1 = Integer.valueOf(key.substring(i, i + 1));
            int idx2 = idx1 + (20 - Integer.valueOf(key.substring(i + 1, i + 2)));
            str = str + strMa.substring(idx1, idx2);
        }
        strKQ = md5(str);
        return strKQ;
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] byteData = null;
        try {
            byteData = instance.digest(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        // convert the byte to hex format
        String hexString = DatatypeConverter.printHexBinary(byteData);
        return hexString.toLowerCase();
    }

    private static String dateForWriteFile(DataForDoiSoat dataForDoiSoat) throws NoSuchAlgorithmException {
        String data = dataForDoiSoat.getHeader() + "\r\n" +
                dataForDoiSoat.getDetails() +
                dataForDoiSoat.getTrailer();
        String checksum = checkSum(data, SECRET_KEY);
        data += checksum;
        return data;
    }

    private static List<DoiSoatDetails> convertTrxNLNapasInfoToDoiSoat(List<Payment> data) throws IOException {
        List<DoiSoatDetails> doiSoatDetailsList = new ArrayList<>();
        for (Payment item : data) {
            PurchaseOtpRes purchaseOtpRes =
                    objectMapper.readValue(item.getRawResponse().replace("** Add transaction:", ""), PurchaseOtpRes.class);
            DoiSoatDetails doiSoatDetails = new DoiSoatDetails();
            doiSoatDetails.setMTI("0210");
            doiSoatDetails.setCardNumber(purchaseOtpRes.getPaymentResult().getSourceOfFunds()
                    .getProvided().getCard().getNumber().replace("x", "_"));
            doiSoatDetails.setTransactionChannelCode(NGL_CHANNEL_CODE_TRANSACTION);
            doiSoatDetails.setTransactionAmount(item.getAmount().intValue() + "00");
            doiSoatDetails.setRealTransactionAmount(item.getAmount().intValue() + "00");
            doiSoatDetails.setAmountSettlement(item.getAmount().intValue() + "00");
            doiSoatDetails.setCurrencyCode(VND);
            doiSoatDetails.setAmountSettlementCurrencyCode(VND);
            doiSoatDetails.setCurrencyCodeCardHolder(VND);
            doiSoatDetails.setTransactionTime(new SimpleDateFormat("HHmmss")
                    .format(new Date(purchaseOtpRes.getPaymentResult().getTransaction().getAcquirer().getDate())));
            doiSoatDetails.setTransactionDate(new SimpleDateFormat("MMdd").format(new Date(item.getTimeCreated().getTime())));
            doiSoatDetails.setSettlementDate(DateTimeFormatter.ofPattern("MMdd").format(LocalDateTime.now()));
            doiSoatDetails.setAgencyCode(AGENCY_CODE_TYPE);
            doiSoatDetails.setConditionalCodeAtServiceAccessPoint(CONDITIONAL_CODE_AT_SERVICE_ACCESS_POINT);
            doiSoatDetails.setAcquirerIdentify(NGL_ACQUIRER_IDENTIFY);
            doiSoatDetails.setRetrievalReferenceNumber(purchaseOtpRes.getPaymentResult().getTransaction().getAcquirer().getId());
            doiSoatDetails.setIdentifyCodeAcceptsCard(NGL_MID);
            doiSoatDetails.setReconciliationResponseCode("0000");
            doiSoatDetails.setServiceCode(SERVICE_CODE);
            doiSoatDetails.setAgencyCodeIdentify(AGENCY_CODE_IDENTIFY);
//            doiSoatDetails.setIssuerIdentify("970500");agencyCodeIdentify
//            doiSoatDetails.setTransactionReferenceNumber("AAAD2QDOWNo0pQAA");
            doiSoatDetails.setReserveInformation1(item.getChannelTransactionId());
            doiSoatDetailsList.add(doiSoatDetails);
        }
        return doiSoatDetailsList;
    }

    public boolean createCSVFile(String fileName, Map<String, List<DoiSoatDetails>> data) throws IOException {
        FileWriter out = new FileWriter(fileName);
        List<String> paramsHeader = new ArrayList<>();
        Field[] fields = DoiSoatDetails.class.getDeclaredFields();
        for (int i = 1; i < fields.length - 1; i++) {
            paramsHeader.add(fields[i].getName());
        }
        String headerOrigin = "MTI,F2,F3,SVC,TCC,F4,RTA,F49,F5,F50,F9,F6,RCA,F51,F10,F11," +
                "F12,F13,F15,F18,F22,F25,F41,ACQ,ISS,MID,BNB,F102,F103,SVFISSNP,IRFISSACQ,IRFISSBNB,SVFACQNP,IRFACQISS,IRFACQBNB," +
                "SVFBNBNP,IRFBNBISS,IRFBNBACQ,F37,F38,TRN,RRC,RSV1,RSV2,RSV3,CSR";
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headerOrigin.split(",")))) {
            data.forEach((k, v) -> {
                try {
                    printer.printRecord(k);
                    for (DoiSoatDetails record : v) {
                        List<String> listValue = new ArrayList<>();
                        Map map = new JSONObject(objectMapper.writeValueAsString(record)).toMap();
                        for (String key : paramsHeader) {
                            String value = "" + map.get(key);
                            listValue.add(value.replace(" ", ""));
                        }
                        printer.printRecord(listValue);
                    }
                } catch (Exception e) {
                    logger.info(ExceptionUtils.getStackTrace(e));
                }
            });
            return true;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }


    private List<Payment> getDataGw(Set<String> daysRec) throws Exception {
        Calendar cal = Calendar.getInstance();
        List<Payment> dataGwSucces = new ArrayList<>();
        daysRec.forEach((e) -> {
            String monthRecTo = e.substring(0, 2);
            String dayRecTo = e.substring(2, 4);
            String yearTo = String.valueOf(cal.get(Calendar.YEAR));
            String yearFro = yearTo;
            String dayRecFro = String.valueOf(Integer.parseInt(dayRecTo) - 1).length() == 1 ?
                    "0" + (Integer.parseInt(dayRecTo) - 1) : String.valueOf(Integer.parseInt(dayRecTo) - 1);
            String monthRecFro = monthRecTo;
            if (dayRecFro.equals("00")) {
                monthRecFro = String.valueOf(Integer.parseInt(monthRecTo) - 1).length() == 1 ?
                        "0" + (Integer.parseInt(monthRecTo) - 1) : String.valueOf(Integer.parseInt(monthRecTo) - 1);
                int lastDay;
                if (monthRecFro.equals("00")) {
                    YearMonth yearMonth = YearMonth.of((cal.get(Calendar.YEAR) - 1), 12);
                    lastDay = yearMonth.lengthOfMonth();
                    yearFro = String.valueOf(cal.get(Calendar.YEAR) - 1);
                    monthRecFro = "12";
                } else {
                    YearMonth yearMonth = YearMonth.of((cal.get(Calendar.YEAR)), Integer.parseInt(monthRecFro));
                    lastDay = yearMonth.lengthOfMonth();
                }

                dayRecFro = String.valueOf(lastDay);
            }
            String timeCreateFro = MessageFormat.format("{0}-{1}-{2} 23:00:00", yearFro, monthRecFro, dayRecFro);
            String timeCreateTo = MessageFormat.format("{0}-{1}-{2} 22:59:59", yearTo, monthRecTo, dayRecTo);
            WriteInfoLog(PREDIX + "GET DATA GW SUCCESS: " + timeCreateFro + " to " + timeCreateTo);
            List<Payment> dataGwSuccesInDay = paymentService.getAllPaymentSuccessBetweenCreateTime(15, timeCreateFro, timeCreateTo, true);
            dataGwSucces.addAll(dataGwSuccesInDay);
        });
        return dataGwSucces;
    }
}
