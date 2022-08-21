package gateway.core.channel.bidv.bidv_transfer_247.service.impl;

import com.google.common.base.Strings;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.bidv.BIDVSecurity;
import gateway.core.channel.bidv.bidv_transfer_247.dto.BIDVTransferConstants;
import gateway.core.channel.bidv.bidv_transfer_247.object247.BankInfo;
import gateway.core.channel.bidv.bidv_transfer_247.object247.NganLuongDSResponse;
import gateway.core.channel.bidv.bidv_transfer_247.object247.TransInfoDS;
import gateway.core.channel.bidv.dto.BIDVConstants;
import gateway.core.channel.bidv.bidv_transfer_247.dto.report.FormReportBIDV;
import gateway.core.channel.bidv.bidv_transfer_247.dto.req.DSRequest;
import gateway.core.channel.bidv.bidv_transfer_247.dto.res.DSResponse;
import gateway.core.channel.bidv.bidv_transfer_247.service.BIDVTransfer247Service;
import gateway.core.channel.bidv.bidv_transfer_247.BIDVTransfer247Security;
import gateway.core.channel.bidv.bidv_transfer_247.wstthdrecon.GetFile;
import gateway.core.channel.bidv.bidv_transfer_247.object247.InfoDS;
import gateway.core.channel.bidv.dto.res.WalletRes;
import gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static gateway.core.channel.bidv.bidv_transfer_247.dto.BIDVTransferConstants.MERCHANT_ID;
import static gateway.core.channel.bidv.bidv_transfer_247.dto.BIDVTransferConstants.PRIVATE_KEY;
import static gateway.core.channel.bidv.bidv_transfer_247.dto.BIDVTransferConstants.SERVICE_ID;

@Service
public class BIDVTransfer247ServiceImpl extends PaymentGate implements BIDVTransfer247Service {
    private String formatDateTimeByPattern(String pattern, long time) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public String monthlyCdr(String request) throws Exception {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String monthOfYear = month + "";
        if (monthOfYear.length() == 1) monthOfYear = "0" + monthOfYear;
//        String day = year + "" + monthOfYear;
        String day = "202007";
        String md5data = "<PROVIDER_ID>019</PROVIDER_ID><TRAN_MONTH>" + day + "</TRAN_MONTH><FILE_TYPE>2</FILE_TYPE>";
        String key = BIDVTransfer247Security.sign(md5data);
        String input = "<MSG>"
                + "<HEADER>"
                + "<MESSAGE_CODE>1002</MESSAGE_CODE>"
                + "<SIGN>" + key + "</SIGN>"
                + "</HEADER>"
                + "<DATA>"
                + md5data
                + "</DATA>"
                + "</MSG>";
        GetFile service = new GetFile();
        WriteInfoLog("input=" + input);

        String wsdl = service.getTTHDOLRecon().submit(input);

        WriteInfoLog("day=" + day + ",wsdl=" + wsdl);
//        int start = wsdl.indexOf("<FILE_CONTENT>");
//        int end = wsdl.indexOf("</FILE_CONTENT>");
//        String content = wsdl.substring(start, end).replaceAll("<FILE_CONTENT>", "").trim();
//        byte[] contentDecode = Base64.decodeBase64(content);
//        String decode = new String(contentDecode, "UTF-8");
        //WriteInfoLog("response from bidv by month=" + day + ",rs=" + wsdl + ", content=" + content + ", decode=" + decode);
        return wsdl;
    }

    @Override
    public PGResponse getFileBIDVDaily(String request) throws Exception {
        DSRequest dsRequest = objectMapper.readValue(request, DSRequest.class);
        String wsdlResponse = callXMLBIDV247(dsRequest);
        int startIndexContent = wsdlResponse.indexOf("<FILE_CONTENT>");
        int endIndexContent = wsdlResponse.indexOf("</FILE_CONTENT>");
        int startIndexErrorCode = wsdlResponse.indexOf("<ERROR_CODE>");
        int endIndexErrorCode = wsdlResponse.indexOf("</ERROR_CODE>");
        String errorCode = (startIndexErrorCode >= 0 && endIndexErrorCode >= startIndexErrorCode) ? wsdlResponse.substring(startIndexErrorCode, endIndexErrorCode).replaceAll("<ERROR_CODE>", "").trim() : "005";

        // build Response to NL
        PGResponse responseNL = new PGResponse();
        responseNL.setStatus(true);
        responseNL.setErrorCode(errorCode);
        responseNL.setMessage(BIDVConstants.getErrorMessageDS(responseNL.getErrorCode()));
        List<TransInfoDS> transInfoDSList = new ArrayList<>();
        if (startIndexContent < 0 || !errorCode.equals("000")) {
            WriteInfoLog("   RUN DOI SOAT  - DAY: " + dsRequest.getTranDate() + " --- FAIL" + ", RAW DATA = " + wsdlResponse);
            responseNL.setData(objectMapper.writeValueAsString(wsdlResponse));
        } else if (startIndexContent > 0) {
            WriteInfoLog("   RUN DOI SOAT  - DAY: " + dsRequest.getTranDate() + " --- SUCCESS");
            DSResponse response = new DSResponse();
            String content = wsdlResponse.substring(startIndexContent, endIndexContent).replaceAll("<FILE_CONTENT>", "").trim();
            byte[] contentDecode = Base64.decodeBase64(content);
            String decode = new String(contentDecode, StandardCharsets.UTF_8);
            WriteInfoLog("RESPONSE DS BIDV - DAY: " + dsRequest.getTranDate() + ",RAW DATA = " + wsdlResponse + ", ENCRYPT DATA = " + content + ", DECRYPT DATA = " + decode);
            String[] parseDecode = decode.split("\\n");
            if (parseDecode.length == 1) {
                String[] parseData = parseDecode[0].split("\\|");
                if (parseData.length == 3) {
                    // build data InfoDS
                    InfoDS infoDS = new InfoDS(parseData[0], parseData[1], parseData[2]);
                    response.setInfoDS(infoDS);
                }
            } else if (parseDecode.length > 1) {
                for (String s : parseDecode) {
                    String[] parseData = s.split("\\|");
                    if (parseData.length == 21) {
                        // build data TransInfoDS
                        TransInfoDS transInfoDS = new TransInfoDS(parseData[0], parseData[1], parseData[2], parseData[3], parseData[4], parseData[5], parseData[6]
                                , parseData[7], parseData[8], parseData[9], parseData[10], parseData[11], parseData[12], parseData[13]
                                , parseData[14], parseData[15], parseData[16], parseData[17], parseData[18], parseData[19], parseData[20]);
                        transInfoDSList.add(transInfoDS);
                    } else if (parseData.length == 3) {
                        // build data InfoDS
                        InfoDS infoDS = new InfoDS(parseData[0], parseData[1], parseData[2]);
                        response.setInfoDS(infoDS);
                    }
                }
                response.setTransInfoDS(transInfoDSList);
            }

//            // Compare data NL && BIDV ==> file Sai Lệch
//            if (response.getTransInfoDS() != null && response.getTransInfoDS().size() > 0) {
//                //Call BIDV result DS
//                WriteInfoLog("   PUSH FILE DOI SOAT TO BIDV  - DAY: " + dsRequest.getTranDate() + " --- FAIL");
//                String responseXML = toolDSForBIDV(response.getTransInfoDS(), dsRequest);
//                startIndexErrorCode = responseXML.indexOf("<ERROR_CODE>");
//                endIndexErrorCode = responseXML.indexOf("</ERROR_CODE>");
//                errorCode = (startIndexErrorCode >=0 && endIndexErrorCode >= startIndexErrorCode) ? responseXML.substring(startIndexErrorCode, endIndexErrorCode).replaceAll("<ERROR_CODE>", "").trim() : "005";
//                responseNL.setErrorCode(errorCode);
//                responseNL.setMessage(BIDVConstants.getErrorMessageDS(responseNL.getErrorCode()));
//                responseNL.setData(responseXML);
//            }
        }
        // From data build file Excel send email Vận Hành
        if (dsRequest.getFileType().equals("1")) {
            FormReportBIDV.sendEmailBIDV(transInfoDSList, 1);
        } else if (dsRequest.getFileType().equals("3")) {
            FormReportBIDV.sendEmailBIDV(transInfoDSList, 3);
        }
        //return objectMapper.writeValueAsString(responseNL);
        return responseNL;
    }

    public static String callXMLBIDV247(DSRequest request) throws GeneralSecurityException, IOException, URISyntaxException, ParseException {
        String dataSHA1RSA = "<PROVIDER_ID>" + BIDVConstants.PROVIDER_ID + "</PROVIDER_ID><TRAN_DATE>" + request.getTranDate() + "</TRAN_DATE><FILE_TYPE>" + request.getFileType() + "</FILE_TYPE>";
        if (request.getMessageCode().equals("1003")) {
            String fileName = BIDVConstants.PROVIDER_ID + "_" + getTranDateString(request.getTranDate(), 1);//BIDVOUT_019_20200727
            dataSHA1RSA += "<FILE_NAME>" + fileName + "</FILE_NAME><FILE_CONTENT>" + Base64.encodeBase64String(request.getFileContent().getBytes()) + "</FILE_CONTENT>";
        } else if (request.getMessageCode().equals("1001")) {
            String fileName = "BIDVOUT_" + BIDVConstants.PROVIDER_ID + "_" + MERCHANT_ID + "_" + getTranDateString(request.getTranDate(), 1);//BIDVOUT_019_20200727
            dataSHA1RSA += "<FILE_NAME>" + fileName + "</FILE_NAME>";
        }
        String sign = BIDVTransfer247Security.sign(dataSHA1RSA);

        String input = "<MSG>"
                + "<HEADER>"
                + "<MESSAGE_CODE>" + request.getMessageCode() + "</MESSAGE_CODE>"
                + "<SIGN>" + sign + "</SIGN>"
                + "</HEADER>"
                + "<DATA>"
                + dataSHA1RSA
                + "</DATA>"
                + "</MSG>";
        GetFile service = new GetFile();
        return service.getTTHDOLRecon().submit(input);
    }

    public String toolDSForBIDV(List<TransInfoDS> listTranBIDV, DSRequest request) throws Exception {
        //get list Trans From NL
        List<TransInfoDS> listTranNL = buildListTransNL();
        List<TransInfoDS> listTranSuccessBIDV = new ArrayList<>(listTranBIDV);
        List<TransInfoDS> listTranSuccessNL = new ArrayList<>(listTranNL);
        List<TransInfoDS> listTranSuccessNLAndBIDV = new ArrayList<>(listTranBIDV);
        List<TransInfoDS> listTranFailNLOrBIDV = new ArrayList<>();

        listTranSuccessBIDV.removeAll(listTranNL);// BIDV Success - NL Fail
        listTranSuccessNL.removeAll(listTranBIDV);// BIDV Fail - NL Success
        listTranSuccessNLAndBIDV.retainAll(listTranNL);// BIDV Success - NL Success

        listTranSuccessBIDV.forEach(transBIDV -> transBIDV.setResultsDS("004"));
        listTranSuccessNL.forEach(transNL -> transNL.setResultsDS("005"));
        listTranFailNLOrBIDV.addAll(listTranSuccessBIDV);
        listTranFailNLOrBIDV.addAll(listTranSuccessNL);
        //build File Content
        StringBuilder content = new StringBuilder();
        double sumAmount = 0;
        for (TransInfoDS tran : listTranFailNLOrBIDV) {
            content.append(tran.dataraw());
            sumAmount += Double.parseDouble(tran.getAmount());
        }
        content.append(new InfoDS(String.valueOf(listTranFailNLOrBIDV.size()),
                String.format("%.0f", sumAmount), formatDateTime("HHmmssddMMyyyy", System.currentTimeMillis())).dataRaw());

        //build Request XML to BIDV
        DSRequest dsRequest = new DSRequest(request.getFileType(), request.getMessageCode().equals("1001") ? "1003" : "", null, content.toString(), request.getTranDate());

        // call XML BIDV
//        String responseXML = callXMLBIDV247(dsRequest);
        String responseXML = "";

        return responseXML;
    }

    public static String getTranDateString(String dateString, int index) throws ParseException {
        Date date = new SimpleDateFormat("yyyyMMdd").parse(dateString);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date.getTime() - index * 24 * 60 * 60 * 1000L);
    }

    private String formatDateTime(String partern, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partern);
        return simpleDateFormat.format(new Date(time));
    }

    @Override
    public PGResponse getListBank247(PaymentAccount paymentAccount, String request) throws Exception {

        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        gateway.core.channel.bidv.ws.nccwalletinput_schema.Root rootInput = new gateway.core.channel.bidv.ws.nccwalletinput_schema.Root();
        rootInput.setServiceId(SERVICE_ID);
        rootInput.setMerchantId(MERCHANT_ID);
        rootInput.setMerchantName("");
        rootInput.setTrandate("");
        rootInput.setTransId("");
        rootInput.setTransDesc("");
        rootInput.setAmount("");
        rootInput.setCurr("");
        rootInput.setPayerId("");
        rootInput.setPayerName("");
        rootInput.setPayerAddr("");
        rootInput.setType("");
        rootInput.setCustmerId("");
        rootInput.setCustomerName("");
        rootInput.setIssueDate("");
        rootInput.setChannelId(dataRequest.getChannel());
        rootInput.setLinkType("");
        rootInput.setOtpNumber("");
//        rootInput.setMoreInfo("|||" + dataRequest.getCardNumber() + "|" + dataRequest.getCardHolerName());
        rootInput.setMoreInfo("");
        rootInput.setFromProcess("");
        String md5data = PRIVATE_KEY + "|" + rootInput.getServiceId() + "|" + rootInput.getMerchantId() + "|" + rootInput.getMerchantName()
                + "|" + rootInput.getTrandate() + "|" + rootInput.getTransId() + "|" + rootInput.getTransDesc()
                + "|" + rootInput.getAmount() + "|" + rootInput.getCurr() + "|" + rootInput.getPayerId() + "|" + rootInput.getPayerName()
                + "|" + rootInput.getPayerAddr() + "|" + rootInput.getType() + "|" + rootInput.getCustmerId() + "|" + rootInput.getCustomerName()
                + "|" + rootInput.getIssueDate() + "|" + rootInput.getChannelId() + "|" + rootInput.getLinkType() + "|" + rootInput.getOtpNumber() + "|" + rootInput.getMoreInfo();
        rootInput.setSecureCode(BIDVTransfer247Security.getMD5data(md5data));
        WriteInfoLog("rootInput=" + rootInput.getSecureCode() + ", cardName=" + rootInput.getMoreInfo());

        //TODO: CALL BIDV
        gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL service = new gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL();
        gateway.core.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
        gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root rootOutput = port.GetBankList247(rootInput);
        return processResponseWalletApi(paymentAccount, rootOutput, false, "getListBank247");
    }

    /**
     * Hàm này được TGTT/NCCDV gọi để lấy thông tin Tên người hưởng tại ngân
     * hàng ngoài BIDV dùng cho chi hộ chuyển tiền liên ngân hàng 24/7.
     *
     * @param request
     * @return
     * @throws java.lang.Exception
     * @Param: payer_id
     * @Param: channel_id
     * @Param: So the hoac so tai khoan(Nếu chuyển đến người hưởng bằng STK thì
     * Bank_Code là bắt buộc, nếu mà bằng số thẻ thì có thể k truyền )
     */
    @Override
    public PGResponse getName247(PaymentAccount paymentAccount, String request) throws Exception {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        gateway.core.channel.bidv.ws.nccwalletinput_schema.Root rootInput = new gateway.core.channel.bidv.ws.nccwalletinput_schema.Root();
        rootInput.setServiceId(SERVICE_ID);
        rootInput.setMerchantId(MERCHANT_ID);
        rootInput.setMerchantName("");
        rootInput.setTrandate("");
        rootInput.setTransId(dataRequest.getTransId());
        rootInput.setTransDesc("");
        rootInput.setAmount("");
        rootInput.setCurr("");
        rootInput.setPayerId("");
        rootInput.setPayerName("");
        rootInput.setPayerAddr("");
        rootInput.setType("");
        rootInput.setCustmerId("");
        rootInput.setCustomerName("");
        rootInput.setIssueDate("");
        rootInput.setChannelId(dataRequest.getChannel());
        rootInput.setLinkType("");
        rootInput.setOtpNumber("");
        rootInput.setMoreInfo("|||" + dataRequest.getCardNumber() + "|" + StringUtils.defaultString(dataRequest.getBankCode(), ""));
        rootInput.setFromProcess("");
        String md5data = PRIVATE_KEY + "|" + rootInput.getServiceId() + "|" + rootInput.getMerchantId() + "|" + rootInput.getMerchantName()
                + "|" + rootInput.getTrandate() + "|" + rootInput.getTransId() + "|" + rootInput.getTransDesc()
                + "|" + rootInput.getAmount() + "|" + rootInput.getCurr() + "|" + rootInput.getPayerId() + "|" + rootInput.getPayerName()
                + "|" + rootInput.getPayerAddr() + "|" + rootInput.getType() + "|" + rootInput.getCustmerId() + "|" + rootInput.getCustomerName()
                + "|" + rootInput.getIssueDate() + "|" + rootInput.getChannelId() + "|" + rootInput.getLinkType() + "|" + rootInput.getOtpNumber() + "|" + rootInput.getMoreInfo();
        rootInput.setSecureCode(BIDVTransfer247Security.getMD5data(md5data));
        WriteInfoLog("rootInput=" + rootInput.getSecureCode() + ", cardName=" + rootInput.getMoreInfo());
        //TODO: CALL BIDV
        gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL service = new gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL();
        gateway.core.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
        gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root rootOutput = port.GetName247(rootInput);
        return processResponseWalletApi(paymentAccount, rootOutput, false, "getName247");
    }

    /**
     * @param request
     * @return
     * @throws java.lang.Exception
     * @Param: merchant_name
     * @Param: trans_id
     * @param: amount
     * @param: channel_id
     * @param: card_number or bankAccountNumber
     */
    @Override
    public PGResponse tranfer2Bank247(PaymentAccount paymentAccount, String request) throws Exception {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        gateway.core.channel.bidv.ws.nccwalletinput_schema.Root rootInput = new gateway.core.channel.bidv.ws.nccwalletinput_schema.Root();
        rootInput.setServiceId(SERVICE_ID);
        rootInput.setMerchantId(MERCHANT_ID);
        rootInput.setMerchantName("Chi ho chuyen tien 247");
        rootInput.setTrandate("");
        rootInput.setTransId(dataRequest.getTransId());
        rootInput.setTransDesc("NGANLUONG_Chuyen tien thanh toan hang hoa");
        rootInput.setAmount(String.valueOf(dataRequest.getAmount()));
        rootInput.setCurr("VND");
        rootInput.setPayerId("");
        rootInput.setPayerName("");
        rootInput.setPayerAddr("");
        rootInput.setType("");
        rootInput.setCustmerId("");
        rootInput.setCustomerName("");
        rootInput.setIssueDate("");
        rootInput.setChannelId(dataRequest.getChannel());
        rootInput.setLinkType("1");
        rootInput.setOtpNumber("");
        String bankCode = dataRequest.getBankCode();
        if (bankCode == null || bankCode.equals("970418")) {
            bankCode = "";
        }
        rootInput.setMoreInfo("|||" + dataRequest.getCardNumber() + "|" + bankCode);
        rootInput.setFromProcess("");

        String md5data = rootInput.getServiceId() + "|" + rootInput.getMerchantId() + "|" + rootInput.getMerchantName()
                + "|" + rootInput.getTrandate() + "|" + rootInput.getTransId() + "|" + rootInput.getTransDesc()
                + "|" + rootInput.getAmount() + "|" + rootInput.getCurr() + "|" + rootInput.getPayerId() + "|" + rootInput.getPayerName()
                + "|" + rootInput.getPayerAddr() + "|" + rootInput.getType() + "|" + rootInput.getCustmerId() + "|" + rootInput.getCustomerName()
                + "|" + rootInput.getIssueDate() + "|" + rootInput.getChannelId() + "|" + rootInput.getLinkType() + "|" + rootInput.getOtpNumber() + "|" + rootInput.getMoreInfo();

        WriteInfoLog("dataBeforEncode=" + md5data);
        rootInput.setSecureCode(BIDVTransfer247Security.sign(md5data, PRIVATE_KEY));
        WriteInfoLog("rootInput=" + rootInput.getSecureCode() + ", cardName=" + rootInput.getMoreInfo());

        gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL service = new gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL();
        gateway.core.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
        if (!Strings.isNullOrEmpty(dataRequest.getBankCode()) && dataRequest.getBankCode().equals("970418")) {
            gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root rootOutput = port.Tranfer2BankBIDV(rootInput);
            return processResponseWalletApi(paymentAccount, rootOutput, true, "Tranfer2BankBIDV");
        } else {
            gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root rootOutput = port.Tranfer2Bank247(rootInput);
            return processResponseWalletApi(paymentAccount, rootOutput, true, "tranfer2Bank247");
        }
    }

    /**
     * @param: channel_id
     * @param: bankAccountNumber
     */
    @Override
    public PGResponse getNameBidv(PaymentAccount paymentAccount, String request) throws Exception {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        gateway.core.channel.bidv.ws.nccwalletinput_schema.Root rootInput = new gateway.core.channel.bidv.ws.nccwalletinput_schema.Root();

        rootInput.setServiceId(SERVICE_ID);
        rootInput.setMerchantId(MERCHANT_ID);
        rootInput.setMerchantName("");
        rootInput.setTrandate("");
        rootInput.setTransId("");
        rootInput.setTransDesc("");
        rootInput.setAmount("");
        rootInput.setCurr("");
        rootInput.setPayerId("");
        rootInput.setPayerName("");
        rootInput.setPayerAddr("");
        rootInput.setType("");
        rootInput.setCustmerId("");
        rootInput.setCustomerName("");
        rootInput.setIssueDate("");
        rootInput.setChannelId(dataRequest.getChannel());
        rootInput.setLinkType("");
        rootInput.setOtpNumber("");
        rootInput.setMoreInfo("|||" + dataRequest.getCardNumber() + "|");
        rootInput.setFromProcess("");

        String md5data = PRIVATE_KEY + "|" + rootInput.getServiceId() + "|" + rootInput.getMerchantId() + "|" + rootInput.getMerchantName()
                + "|" + rootInput.getTrandate() + "|" + rootInput.getTransId() + "|" + rootInput.getTransDesc()
                + "|" + rootInput.getAmount() + "|" + rootInput.getCurr() + "|" + rootInput.getPayerId() + "|" + rootInput.getPayerName()
                + "|" + rootInput.getPayerAddr() + "|" + rootInput.getType() + "|" + rootInput.getCustmerId() + "|" + rootInput.getCustomerName()
                + "|" + rootInput.getIssueDate() + "|" + rootInput.getChannelId() + "|" + rootInput.getLinkType() + "|" + rootInput.getOtpNumber() + "|" + rootInput.getMoreInfo();

        WriteInfoLog("dataBeforEncode=" + md5data);
        rootInput.setSecureCode(BIDVTransfer247Security.getMD5data(md5data));
        WriteInfoLog("rootInput=" + rootInput.getSecureCode() + ", cardName=" + rootInput.getMoreInfo());

        gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL service = new gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL();
        gateway.core.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
        gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root rootOutput = port.GetNameBIDV(rootInput);
        return processResponseWalletApi(paymentAccount, rootOutput, false, "getNameBidv");
    }

    private PGResponse processResponseWalletApi(PaymentAccount paymentAccount, Root rootOutput, boolean isSign, String funApi)
            throws Exception {

        BIDVSecurity.initParam(paymentAccount);

        // Procress Response
        WalletRes response = new WalletRes();
        response.setServiceId(rootOutput.getServiceId());
        response.setMerchantId(rootOutput.getMerchantId());
        response.setTransactionDate(rootOutput.getTrandate());
        response.setTransactionId(rootOutput.getTransId());
        response.setResponseCode(rootOutput.getResponseCode());
        response.setBankTransactionId(rootOutput.getResponseTxnCode());
        response.setList(rootOutput.getList());

        // parse data moreInfo of BIDV-247
        if (rootOutput.getResponseCode().equals("000") && funApi.equals("getListBank247")) {
            String[] parseMoreInfo = rootOutput.getMoreInfo().replaceAll("\\{", "").split("}");
            List<BankInfo> bankInfos = new ArrayList<>();
            for (String bankInfoStr : parseMoreInfo) {
                String[] parseBankInfo = bankInfoStr.split("\\|");
                if (parseBankInfo.length == 4) {
                    BankInfo bankInfo = new BankInfo(parseBankInfo[0], parseBankInfo[1], parseBankInfo[2], parseBankInfo[3]);
                    bankInfos.add(bankInfo);
                }
            }
            response.setMoreInfo(objectMapper.writeValueAsString(bankInfos));
        } else {
            response.setMoreInfo(rootOutput.getMoreInfo());
        }

        response.setRedirectUrl(rootOutput.getRedirectUrl());

        String[] arrayResponse = {rootOutput.getServiceId(), rootOutput.getMerchantId(), rootOutput.getTrandate(),
                rootOutput.getTransId(), rootOutput.getResponseCode(), rootOutput.getResponseTxnCode(),
                rootOutput.getList(), rootOutput.getMoreInfo(), rootOutput.getRedirectUrl()};

        WriteInfoLog("3. BIDV RESPONSE", objectMapper.writeValueAsString(rootOutput));

        PGResponse responseVimo = new PGResponse();
        responseVimo.setStatus(true);
        responseVimo.setErrorCode(response.getResponseCode());
        responseVimo.setMessage(BIDVConstants.getErrorMessage(response.getResponseCode()));
        responseVimo.setData(objectMapper.writeValueAsString(response));
        // responseVimo.setData(response);

        // Check signature, md5 when transaction SUCCESS
        if (BIDVConstants.ERROR_CODE_SUCCESS.equals(rootOutput.getResponseCode())) {

            if (isSign) {

                boolean signatureValidate = BIDVTransfer247Security.verifySign(arrayResponse, rootOutput.getSecureCode(),
                        PRIVATE_KEY);
                WriteInfoLog("signatureBIDV=" + signatureValidate);
//                if (!signatureValidate) {
//                    WriteErrorLog("BIDV SIGNATURE INCORRECT");
//                    responseVimo.setErrorCode("007");
//                    responseVimo.setMessage(BIDVConstants.getErrorMessage("007"));
//                }
                responseVimo.setErrorCode(rootOutput.getResponseCode());
                responseVimo.setMessage(BIDVConstants.getErrorMessage(rootOutput.getResponseCode()));
            } else {
                String secureCodeRes = BIDVTransfer247Security.md5(arrayResponse, PRIVATE_KEY);
                WriteInfoLog("secureCodeRes=" + secureCodeRes);
                if (!secureCodeRes.equals(rootOutput.getSecureCode())) {
                    WriteErrorLog("BIDV MD5 INCORRECT");
                    responseVimo.setErrorCode("007");
                    responseVimo.setMessage(BIDVConstants.getErrorMessage("007"));
                }

            }
        }

        //return objectMapper.writeValueAsString(responseVimo);
        return responseVimo;
    }

    @Override
    public PGResponse inquery(PaymentAccount paymentAccount, String inputStr) throws Exception {
        WriteInfoLog("BIDV QueryTransaction");

        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
//        if (StringUtils.isNotEmpty(BIDVValidator.validateQueryParam(input))) {
//            return BIDVValidator.createErrorResponse(BIDVValidator.validateQueryParam(input));
//        }

        gateway.core.channel.bidv.ws.nccinput_schema.Root rootInput = new gateway.core.channel.bidv.ws.nccinput_schema.Root();

        rootInput.setServiceId(SERVICE_ID);
        rootInput.setMerchantId(SERVICE_ID);
        rootInput.setMerchantName("");
        rootInput.setTrandate(input.getTransTime()); // YYMMDD chi cho phep giao
        rootInput.setTransId(input.getTransId());
        rootInput.setTransDesc("");
        rootInput.setAmount("");
        rootInput.setCurr("");
        rootInput.setPayerId("");
        rootInput.setPayerName("");
        rootInput.setPayerAddr("");
        rootInput.setType("");
        rootInput.setCustmerId("");
        rootInput.setCustomerName("");
        rootInput.setIssueDate("");
        rootInput.setFromProcess("");
        String md5data = PRIVATE_KEY + "|" + rootInput.getServiceId() + "|" + rootInput.getMerchantId() + "|" + rootInput.getMerchantName()
                + "|" + rootInput.getTrandate() + "|" + rootInput.getTransId() + "|" + rootInput.getTransDesc()
                + "|" + rootInput.getAmount() + "|" + rootInput.getCurr() + "|" + rootInput.getPayerId() + "|" + rootInput.getPayerName()
                + "|" + rootInput.getPayerAddr() + "|" + rootInput.getType() + "|" + rootInput.getCustmerId() + "|" + rootInput.getCustomerName()
                + "|" + rootInput.getIssueDate();

        WriteInfoLog("dataBeforEncode=" + md5data);
        rootInput.setSecureCode(BIDVTransfer247Security.getMD5data(md5data));
        WriteInfoLog("rootInput=" + rootInput.getSecureCode());
        // Call BIDV
//        URL url = new URL(getPaymentChannel().getUrl());
//        pg.channel.bidv.ws.bidvws.NCCWSDL service = new pg.channel.bidv.ws.bidvws.NCCWSDL(url);
//        pg.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
//        pg.channel.bidv.ws.nccoutput_schema.Root rootOutput = port.inquiry(rootInput);
        gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL service = new gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL();
        gateway.core.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
        gateway.core.channel.bidv.ws.nccoutput_schema.Root rootOutput = port.inquiry(rootInput);

        return processResponseTransApiQuery(paymentAccount, rootOutput, false);
    }

    @Override
    public PGResponse checkProviderBalance(PaymentAccount paymentAccount, String request) throws Exception {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        gateway.core.channel.bidv.ws.nccwalletinput_schema.Root rootInput = new gateway.core.channel.bidv.ws.nccwalletinput_schema.Root();

        rootInput.setServiceId(SERVICE_ID);
        rootInput.setMerchantId(MERCHANT_ID);
        rootInput.setMerchantName("");
        rootInput.setTrandate("");
        rootInput.setTransId("");
        rootInput.setTransDesc("");
        rootInput.setAmount("");
        rootInput.setCurr("");
        rootInput.setPayerId("");
        rootInput.setPayerName("");
        rootInput.setPayerAddr("");
        rootInput.setType("");
        rootInput.setCustmerId("");
        rootInput.setCustomerName("");
        rootInput.setIssueDate("");
        rootInput.setChannelId(dataRequest.getChannel());
        rootInput.setLinkType("");
        rootInput.setOtpNumber("");
        rootInput.setMoreInfo("");
        rootInput.setFromProcess("");

        String md5data = PRIVATE_KEY + "|" + rootInput.getServiceId() + "|" + rootInput.getMerchantId() + "|" + rootInput.getMerchantName()
                + "|" + rootInput.getTrandate() + "|" + rootInput.getTransId() + "|" + rootInput.getTransDesc()
                + "|" + rootInput.getAmount() + "|" + rootInput.getCurr() + "|" + rootInput.getPayerId() + "|" + rootInput.getPayerName()
                + "|" + rootInput.getPayerAddr() + "|" + rootInput.getType() + "|" + rootInput.getCustmerId() + "|" + rootInput.getCustomerName()
                + "|" + rootInput.getIssueDate() + "|" + rootInput.getChannelId() + "|" + rootInput.getLinkType() + "|" + rootInput.getOtpNumber() + "|" + rootInput.getMoreInfo();

        WriteInfoLog("dataBeforEncode=" + md5data);
        rootInput.setSecureCode(BIDVTransfer247Security.getMD5data(md5data));
        WriteInfoLog("rootInput=" + rootInput.getSecureCode() + ", cardName=" + rootInput.getMoreInfo());

        gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL service = new gateway.core.channel.bidv.bidv_transfer_247.bidvws.NCCWSDL();
        gateway.core.channel.bidv.ws.bidvws.NCCPortType port = service.getNCCPortTypeEndpoint0();
        gateway.core.channel.bidv.ws.nccwalletoutput_schema.Root rootOutput = port.providerBalance(rootInput);
        return processResponseWalletApi(paymentAccount, rootOutput, false, "checkProviderBalance");
    }

    private PGResponse processResponseTransApiQuery(PaymentAccount paymentAccount, gateway.core.channel.bidv.ws.nccoutput_schema.Root rootOutput, boolean isSign)
            throws Exception {

        BIDVSecurity.initParam(paymentAccount);

        // Procress Response
        WalletRes response = new WalletRes();
        response.setServiceId(rootOutput.getServiceId());
        response.setMerchantId(rootOutput.getMerchantId());
        response.setTransactionDate(rootOutput.getTrandate());
        response.setTransactionId(rootOutput.getTransId());
        response.setResponseCode(rootOutput.getResponseCode());
        response.setBankTransactionId(rootOutput.getResponseTxnCode());
        response.setList(rootOutput.getList());

        response.setRedirectUrl(rootOutput.getRedirectUrl());

        String[] arrayResponse = {rootOutput.getServiceId(), rootOutput.getMerchantId(), rootOutput.getTrandate(),
                rootOutput.getTransId(), rootOutput.getResponseCode(), rootOutput.getResponseTxnCode(),
                rootOutput.getList(), rootOutput.getRedirectUrl()};

        WriteInfoLog("3. BIDV RESPONSE", objectMapper.writeValueAsString(rootOutput));

        PGResponse responseNL = new PGResponse();
        responseNL.setStatus(true);
        responseNL.setErrorCode(response.getResponseCode());
        responseNL.setMessage(BIDVConstants.getErrorMessage(response.getResponseCode()));
        responseNL.setData(objectMapper.writeValueAsString(response));
        // responseVimo.setData(response);

        // Check signature, md5 when transaction SUCCESS
        if (BIDVConstants.ERROR_CODE_SUCCESS.equals(rootOutput.getResponseCode())) {

            if (isSign) {
                boolean signatureValidate = BIDVTransfer247Security.verifySign(arrayResponse, rootOutput.getSecureCode(),
                        PRIVATE_KEY);
                if (!signatureValidate) {
                    WriteErrorLog("BIDV SIGNATURE INCORRECT");
                    responseNL.setErrorCode("007");
                    responseNL.setMessage(BIDVConstants.getErrorMessage("007"));
                }
            } else {
                String secureCodeRes = BIDVTransfer247Security.md5(arrayResponse, PRIVATE_KEY);
                if (!secureCodeRes.equals(rootOutput.getSecureCode())) {
                    WriteErrorLog("BIDV MD5 INCORRECT");
                    responseNL.setErrorCode("007");
                    responseNL.setMessage(BIDVConstants.getErrorMessage("007"));
                }
            }
        }

        //return objectMapper.writeValueAsString(responseNL);
        return responseNL;
    }

    public List<TransInfoDS> buildListTransNL() throws Exception {
        List<TransInfoDS> listTranSuccessNL = new ArrayList<>();
        //Call NL get list trans BIDV
        String nganLuongResponse = HttpUtil.sendGet(BIDVTransferConstants.URL_API_CALL_NL_DS);
        // convert dataString to Object
        return objectMapper.readValue(nganLuongResponse, NganLuongDSResponse.class).getData();
    }
}
