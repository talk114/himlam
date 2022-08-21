package gateway.core.channel.mb_qrcode.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.mb_qrcode.MBBankVNPayOffusClientRequest;
import gateway.core.channel.mb_qrcode.dto.object.DataDS;
import gateway.core.channel.mb_qrcode.dto.object.NganLuongDSResponse;
import gateway.core.channel.mb_qrcode.dto.req.*;
import gateway.core.channel.mb_qrcode.dto.res.BaseResponse;
import gateway.core.channel.mb_qrcode.dto.res.CheckOrderResponse;
import gateway.core.channel.mb_qrcode.dto.res.CreateQrCodeResponse;
import gateway.core.channel.mb_qrcode.dto.res.RootResponse;
import gateway.core.channel.mb_qrcode.service.MBBankQRCodeOffusService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import gateway.core.util.PGUtil;
import gateway.core.util.SFTPUploader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.ACCESS_KEY;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.MERCHANT_SHORT_NAME;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.TERMINAL_ID;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.URL_API_CALL_NL;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.URL_API_CALL_NL_DS;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.URL_API_CHECK_ORDER;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.URL_API_CREAT_QRCODE;
import static gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant.USER_PASS_NGAN_LUONG;

@Service
public class MBBankQRCodeOffusServiceImpl extends PaymentGate implements MBBankQRCodeOffusService {
    private static final Logger logger = LogManager.getLogger(MBBankQRCodeOffusServiceImpl.class);
    /**
     * Create QR Code 4 kiểu QR Code là kiểu 1 (Chocác điểm offline), kiểu 2
     * (Cho hóa đơn/mã khách hàng) kiểu 3(Cho sản phẩm) kiểu 4 (Cho đơn hàng
     * trên VNPAY QR)
     */
    @Override
    public PGResponse createQRCode(String request) throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, UnrecoverableKeyException {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        String clientMessageId = dataRequest.getTransId();
        //TODO: build create QR Code request
        CreateQRCodeRequest createQRCodeRequest = new CreateQRCodeRequest();
        createQRCodeRequest.setTerminalID(dataRequest.getTerminalId()); //
        createQRCodeRequest.setQrcodeType(dataRequest.getQrcodeType()); //
        createQRCodeRequest.setInitMethod(dataRequest.getInitMethod()); //
        if ((dataRequest.getQrcodeType() == 1 && dataRequest.getInitMethod() == 12) || dataRequest.getQrcodeType() == 3 || dataRequest.getQrcodeType() == 4) {
            createQRCodeRequest.setTransactionAmount(dataRequest.getAmount()); //
        }
        createQRCodeRequest.setBillNumber(dataRequest.getBillNumber()); //: bắt buộc với QrcodeType = 1 và động
        createQRCodeRequest.setAdditionalAddress(dataRequest.getAdditionalAddress()); //
        createQRCodeRequest.setAdditionalMobile(dataRequest.getAdditionalMobile());
        createQRCodeRequest.setAdditionalEmail(dataRequest.getAdditionalEmail());
        createQRCodeRequest.setTransactionPurpose(dataRequest.getTransactionPurpose());
        createQRCodeRequest.setConsumerLabel(dataRequest.getConsumerLabel());
        createQRCodeRequest.setTerm(Strings.isNullOrEmpty(dataRequest.getTerm()) ? dataRequest.getTerm() : convertDateTimeToPattern("MMYYYY", dataRequest.getTerm(), 1));
        createQRCodeRequest.setReferenceLabelTime(Strings.isNullOrEmpty(dataRequest.getReferenceLabelTime()) ? dataRequest.getReferenceLabelTime() : convertDateTimeToPattern("YYMMddHHmm", dataRequest.getReferenceLabelTime(), 1));
        createQRCodeRequest.setReferenceLabelCode(dataRequest.getReferenceLabelCode());

        return process(createQRCodeRequest, URL_API_CREAT_QRCODE, CreateQrCodeResponse.class, "createQRCode", clientMessageId);
    }

    /*
        API Merchant Check Order
        Merchant gọi đến hệ thống MB-MMS để check thông tin đơn hàng
    **/
    @Override
    public PGResponse checkOrder(String request) throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, UnrecoverableKeyException {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);

        String clientMessageId = dataRequest.getTransId();
        CheckOrderRequest checkOrderRequest = new CheckOrderRequest();

        if (!Strings.isNullOrEmpty(dataRequest.getTraceTransfer())) {
            checkOrderRequest.setTraceTransfer(dataRequest.getTraceTransfer());
        }
        //Nếu muốn check theo số biên lai với Qrcode Type 1 - động
        if (!Strings.isNullOrEmpty(dataRequest.getBillNumber())) {
            checkOrderRequest.setBillNumber(dataRequest.getBillNumber());
        }
        //nếu là Qrcode type 2_động
        if (!Strings.isNullOrEmpty(dataRequest.getConsumerLabel()) && !Strings.isNullOrEmpty(dataRequest.getTerm())) {
            String consumerLabelTerm = dataRequest.getConsumerLabel() + convertDateTimeToPattern("MMYYYY", dataRequest.getTerm(), 1);
            checkOrderRequest.setConsumerLabelTerm(consumerLabelTerm);
        }
        //nếu là Qrcode type 3,4
        if (!Strings.isNullOrEmpty(dataRequest.getReferenceLabelTime()) && !Strings.isNullOrEmpty(dataRequest.getReferenceLabelCode())) {
            String referenceLabel = convertDateTimeToPattern("YYMMddHHmm", dataRequest.getReferenceLabelTime(), 1) + dataRequest.getReferenceLabelCode();
            checkOrderRequest.setReferenceLabel(referenceLabel);
        }
        checkOrderRequest.buildDataChecksum();
        return process(checkOrderRequest, URL_API_CHECK_ORDER, CheckOrderResponse.class, "checkOrder", clientMessageId);
    }

    @Override
    public String mbBankCallBack(String request) throws IOException, NoSuchAlgorithmException {
        // TODO
        WriteInfoLog("############################### MBBANK CALL BACK ###############################");
        WriteInfoLog("############################### MBBANK CALL BACK" + request + "###############################");
        MBBankCallBack mbBankCallBack = objectMapper.readValue(request, MBBankCallBack.class);
        // TODO
        WriteInfoLog("############## MBBANK CALL BACK " + objectMapper.writeValueAsString(mbBankCallBack) + "############");
        JSONObject json = new JSONObject(mbBankCallBack);
        Map<String, Object> map = json.toMap();
        map.put("func", "mbBankCallBack");
//        if (mbBankCallBack.getCheckSum().equals(checksumVerify(map))) {
        //TODO: CALL NGAN LUONG
        return callBackToNganLuong(objectMapper.writeValueAsString(mbBankCallBack), "0", "Thành Công", mbBankCallBack.getTraceTransfer());
//        } else {
//            //TODO: CALL NGAN LUONG VERIFY CHECK SUM FALSE
//            return callBackToNganLuong(objectMapper.writeValueAsString(mbBankCallBack), "1", "Sai Checksum", mbBankCallBack.getTraceTransfer());
//        }
    }

    private String callBackToNganLuong(Object data, String errorCode, String errorMessage, String traceTransfer) throws IOException {
        CallNganLuongRequest callNganLuongRequest = new CallNganLuongRequest();
        callNganLuongRequest.setErrorCode(errorCode);
        callNganLuongRequest.setMessage(errorMessage);
        callNganLuongRequest.setData(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", objectMapper.writeValueAsString(callNganLuongRequest));
        // TODO
        WriteInfoLog("CALL BACK MB_QRCODE NGAN LUONG REQUEST - " + traceTransfer + " " + objectMapper.writeValueAsString(callNganLuongRequest) + "###########");
        String nganLuongResponse = HttpUtil.send(URL_API_CALL_NL, params, USER_PASS_NGAN_LUONG);
        // TODO
        WriteInfoLog("######################## CALL-BACK MB_QRCODE NGAN LUONG RESPONSE- " + traceTransfer + ": " + nganLuongResponse + " ########################");
        return nganLuongResponse;
    }

    /*
      Chạy đối soát hằng ngày
    **/
    public static void runDoiSoatMB() {
        try {
            String dateFormat = PGUtil.formatDateTime("ddMMyyyy", System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
            PGResponse result = new MBBankQRCodeOffusServiceImpl().createFileDS(dateFormat);
            // TODO
            WriteInfoLog("######################## RUN DOI SOAT MB - " + dateFormat + ": " + objectMapper.writeValueAsString(result) + " ########################");
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
    }


    /*
          Chạy đối soát theo thời gian truyền vào
    **/
    @Override
    public PGResponse createFileDS(String dateDS) throws Exception {
        if (dateDS.length() == 8 && HttpUtil.checkIPServerRunSchedule()) {
            // get data from NL
            String nganLuongResponse = HttpUtil.sendGetHttps(URL_API_CALL_NL_DS, dateDS);
//            String nganLuongResponse = "{\"error_code\":\"00\",\"error_description\":\"Th\\u00e0nh c\\u00f4ng\",\"data\":[{\"amount\":75000,\"nl_trans_id\":5062400,\"mb_trans_id\":\"FT20202060558658-14109\",\"bank_trans_id\":\"FT20202060558658-14109\",\"time_created\":1598258483,\"time_paid\":1598267398},{\"amount\":55000,\"nl_trans_id\":5062402,\"mb_trans_id\":\"FT20202390963932-14111\",\"bank_trans_id\":\"FT20202390963932-14111\",\"time_created\":1598258619,\"time_paid\":1598267487}]}";
            return buildResponse(null, "MB DS STEP 1 - SUCCESS", true);
        } else {
            return buildResponse(null, "MB DS STEP 1  - DATE INVALID", false);
        }
    }

    /*
          Chạy đối soát Step 2
    **/
    @Override
    public PGResponse buildFileDS(String data) throws Exception {
        // convert dataString to Object
        System.out.println("NGAN LUONG DATA DOI SOAT: " + data + "    |   " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
        NganLuongDSResponse dataDS = Strings.isNullOrEmpty(data) ? new NganLuongDSResponse("00", "success") : objectMapper.readValue(data, NganLuongDSResponse.class);
        String url = System.getProperty("user.dir");
        String PATH_FOLDER_FILE_DS = "/data/doisoat/mb/live/";//live
        File dir = new File(PATH_FOLDER_FILE_DS);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String newFormatDateDS = formatDateTime("yyMMdd", System.currentTimeMillis()  - 24 * 60 * 60 * 1000L);
        String fileName = newFormatDateDS + "_TRANS_DETAIL_NGLUONG.txt";
        String fileNameDelete = getPatternYesterday("yyMMdd", newFormatDateDS) + "_TRANS_DETAIL_NGLUONG.txt";
        String partFile = PATH_FOLDER_FILE_DS + File.separator + fileName;
        if (buildFile(partFile, dataDS.getData(), newFormatDateDS)) {
            //upload SFTP
            System.out.println("========== START UPLOAD FSPT == " + fileName + "=============");
            File fileDS = new File(partFile);
            ClassLoader classLoader = MBBankQRCodeOffusServiceImpl.class.getClassLoader();
            String partPrivateKey = Objects.requireNonNull(classLoader.getResource("MB/rsa_id")).getPath();
            if (SFTPUploader.updateSFTP(fileDS, partPrivateKey, "/mbin", fileNameDelete)) {
                return buildResponse(null, "MB UPLOAD FILE DS - SUCCESS", true);
            }
            System.out.println("========== END UPLOAD FSPT == " + fileName + "=============");
            return buildResponse(null, "MB UPLOAD FILE DS - FAIL", false);
        } else {
            return buildResponse(null, "MB UPLOAD FILE DS - ERROR", false);
        }
    }

    private boolean buildFile(String partFile, List<DataDS> dataDSList, String dateTrans) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(partFile);
            //write data to file - Write first line
            String firstLine = "0001" +//Loại bản ghi
                    setStringData(MERCHANT_SHORT_NAME, 8, "STR") +//Tên Merchant
                    convertPatternToNewPattern("yyMMdd", "ddMMyy", dateTrans);//Ngày giao dịch
            fileWriter.write(firstLine);
            fileWriter.write("\n");

            //write data to file - Write body file
            for (DataDS dataDS : dataDSList) {
                String dataLine = "0002" +//Loại bản ghi
                        "0210" +//Mã định dạng thông điệp (MTI)
                        "000002" +//Mã xử lý
                        setStringData(dataDS.getAmount(), 12, "NUMBER") +//Số tiền giao dịch
                        setStringData(dataDS.getBank_trans_id().split("-")[1], 20, "STR") +//Số Trace hệ thống
                        setStringData(dataDS.getBank_trans_id().split("-")[0], 20, "STR") +//Mã giao dịch
                        convertDateTimeToPattern("HHmmss", dataDS.getTime_paid(), 1) +//Giờ giao dịch
                        convertPatternToNewPattern("yyMMdd", "MMdd", dateTrans) +//Ngày giao dịch
                        convertPatternToNewPattern("yyMMdd", "MMdd", dateTrans) +//Ngày thanh toán
                        "00" +//Mã số trả lời giao dịch
                        "  " +//Mã số xử lý giao dịch
                        setStringData(TERMINAL_ID, 15, "STR") +//TerminalID
                        setStringData(MERCHANT_SHORT_NAME, 30, "STR") +//Thông tin bổ sung  - MerchantName
                        setStringData("", 60, "STR");//Thông tin bổ sung  - MerchantName
                fileWriter.write(dataLine);
                fileWriter.write("\n");
            }

            //write data to file - Write last line
            String lastLine = "0009" +//Loại bản ghi
                    setStringData(String.valueOf(dataDSList.size()), 9, "NUMBER") +//Số dòng giao dịch trong file
                    "NGANLUONG_EXPORTFILE" +//Người tạo
                    convertDateTimeToPattern("HHmmss", String.valueOf(System.currentTimeMillis()), 2) +//Giờ tạo file
                    convertPatternToNewPattern("yyMMdd", "ddMMyy", dateTrans);//Ngày tạo file
            fileWriter.write(lastLine);

            // close file
            fileWriter.close();
            return true;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    private String setStringData(String data, int lengthDataStr, String type) {
        StringBuilder newStrData = new StringBuilder();
        //build add data string
        StringBuilder addStrData = new StringBuilder();
        if (data.length() < lengthDataStr) {
            if (!Strings.isNullOrEmpty(type) && type.equals("STR")) {
                while (addStrData.toString().length() < lengthDataStr - data.length()) {
                    addStrData.append(" "); // Thêm ký tự trắng vào đầu chuỗi
                }
            } else if (!Strings.isNullOrEmpty(type) && type.equals("NUMBER")) {
                while (addStrData.length() < lengthDataStr - data.length()) {
                    addStrData.append("0"); // Thêm số 0 vào đầu
                }
            }
        }
        newStrData.append(addStrData.toString());
        newStrData.append(data);
        return newStrData.toString();
    }

    private <T extends BaseResponse> PGResponse process(BaseRequest baseRequest, String api, Class<T> resClass, String func, String clientMessageId) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyManagementException, CertificateException, KeyStoreException, UnrecoverableKeyException {

//        checksumVerify(map);
        // TODO
        WriteInfoLog("2. MBBANK QRCODE OFFUS REQUEST: ", objectMapper.writeValueAsString(baseRequest));
        String response = MBBankVNPayOffusClientRequest.callApi(objectMapper.writeValueAsString(baseRequest), api, clientMessageId);
        // TODO
        WriteInfoLog("3. MBBANK QRCODE OFFUS RESPONSE: ", response);
        if (!Strings.isNullOrEmpty(response)) {
            RootResponse rootResponse = objectMapper.readValue(response, RootResponse.class);
            if (rootResponse.getData() != null) {
                T responseData = objectMapper.readValue(objectMapper.writeValueAsString(rootResponse.getData()), resClass);
                if (func.equals("checkOrder")) {
                    JSONObject json = new JSONObject(responseData);
                    Map<String, Object> map = json.toMap();
                    map.put("func", func);
                    if (responseData.getCheckSum().equals(checksumVerify(map))) {
                        return buildResponse(rootResponse, rootResponse.getStatus(), true);
                    }
                    return buildResponse(rootResponse, "VERIFY CHECKSUM FALSE", true);
                }
            }
            return buildResponse(rootResponse, rootResponse.getStatus(), true);
        }
        return buildResponse(null, "CANNOT REQUEST TO BANK", false);
    }

    private String checksumVerify(Map<String, Object> params) throws NoSuchAlgorithmException {
        StringBuilder dataForVerify = new StringBuilder();
        if (!Strings.isNullOrEmpty((String) params.get("traceTransfer"))) {
            dataForVerify.append((String) params.get("traceTransfer"));
        }
        if (params.get("func").equals("mbBankCallBack") && !Strings.isNullOrEmpty((String) params.get("billNumber"))) {
            dataForVerify.append((String) params.get("billNumber"));
        }
        if (!Strings.isNullOrEmpty((String) params.get("payDate"))) {
            dataForVerify.append((String) params.get("payDate"));
        }
        if (!Strings.isNullOrEmpty((String) params.get("debitAmount"))) {
            dataForVerify.append((String) params.get("debitAmount"));
        }
        if (!Strings.isNullOrEmpty((String) params.get("respCode"))) {
            dataForVerify.append((String) params.get("respCode"));
        }
        dataForVerify.append(ACCESS_KEY);
        return PGSecurity.md5(dataForVerify.toString());
    }

    private PGResponse buildResponse(RootResponse data, String errorCode, boolean status) throws JsonProcessingException {
        PGResponse pgResponse = new PGResponse();
        pgResponse.setStatus(status);
        if (data == null) {
            pgResponse.setErrorCode(errorCode);
            pgResponse.setMessage(errorCode);
        } else {
            pgResponse.setErrorCode(data.getStatus());
            pgResponse.setMessage(data.getError());
        }
        pgResponse.setData(data);
        //return objectMapper.writeValueAsString(pgResponse);
        return pgResponse;
    }

    private String convertDateTimeToPattern(String pattern, String time, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        int index = 1;
        if (type == 1) {
            index = 1000;
        }
        Date date = new Date(Long.parseLong(time) * index);
        return simpleDateFormat.format(date);
    }

    private String convertPatternToNewPattern(String oldPattern, String newPattern, String patternTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldPattern);
        Date date = simpleDateFormat.parse(patternTime);
        SimpleDateFormat newDateFormat = new SimpleDateFormat(newPattern);
        return newDateFormat.format(date);
    }

    private String getPatternYesterday(String pattern, String patternTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(patternTime);
        return simpleDateFormat.format(date.getTime() - 24 * 60 * 60 * 1000L);
    }

    private String formatDateTime(String partern, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partern);
        String date = simpleDateFormat.format(new Date(time));
        return date;
    }
}
