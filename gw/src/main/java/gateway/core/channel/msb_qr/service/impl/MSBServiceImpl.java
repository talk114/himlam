//package gateway.core.channel.msb_qr.service.impl;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import gateway.core.channel.ChannelCommonConfig;
//import gateway.core.channel.PaymentGate;
//import gateway.core.channel.msb_qr.ApiClient;
//import gateway.core.channel.msb_qr.dto.MsbConstants;
//import gateway.core.channel.msb_qr.dto.req.BaseRequest;
//import gateway.core.channel.msb_qr.dto.req.CheckOrderReq;
//import gateway.core.channel.msb_qr.dto.req.CreateQrCodeReq;
//import gateway.core.channel.msb_qr.dto.res.CheckOrderRes;
//import gateway.core.channel.msb_qr.dto.res.CreateQrCodeRes;
//import gateway.core.channel.msb_qr.service.MSBService;
//import gateway.core.dto.PGResponse;
//import gateway.core.dto.request.DataRequest;
//import gateway.core.util.PGSecurity;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//
//@Service
//public class MSBServiceImpl extends PaymentGate implements MSBService {
//    public PGResponse QrCodePayment(String inputStr) {
//        try {
//            WriteInfoLog("VIMO_MC/NGL - MSB QRCODE PAYMENT");
//            DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
//
//            CreateQrCodeReq req = new CreateQrCodeReq();
//            req.setAppId(ChannelCommonConfig.MSB_QRCode_PROVIDER_ID);
//            req.setMerchantName(ChannelCommonConfig.MSB_QRCode_MERCHANT_NAME);
//            req.setMerchantCode(ChannelCommonConfig.MSB_QRCode_MERCHANT_ID);
//
//            req.setBillNumber(input.getBillNo());
//            req.setTerminalId(input.getTerminalId());
//            req.setPayType(input.getPayMethod()); // Kiểu Qr
//
//            req.setAmount(df.format(input.getAmount()));
//            req.setTipAndFee(String.format("%.0f", input.getFeeAmount()));
//            req.setExpDate(input.getExpireDate());
//            req.setDesc(input.getDescription());
//
//            String paymethod = input.getPayMethod();
//            String indexCheckLog = "";
//
////		// 01 : QR đơn hàng (QR Online – cổng thanh toán trực tuyến)
//            if (MsbConstants.QR_TYPE_ORDER_ONLINE.equals(paymethod)) {
//                req.setTxnId(input.getTransId());
//                indexCheckLog = input.getTransId();
//            }
////		// 02 : Qr Sản phẩm
//            if (MsbConstants.QR_TYPE_PRODUCT.equals(paymethod)) {
//                req.setProductId(input.getProductId());
//                req.setBillNumber(input.getBillNo());
//                indexCheckLog = input.getProductId() + "|" + input.getBillNo();
//            }
////		// 03 : QR Hóa đơn (QR Offline)
//            if (MsbConstants.QR_TYPE_ORDER_OFFLINE.equals(paymethod)) {
//                req.setBillNumber(input.getBillNo());
//                indexCheckLog = input.getBillNo();
//            }
//// 04 : QR Billing
//            if (MsbConstants.QR_TYPE_BILLING.equals(paymethod)) {
//                req.setConsumerID(input.getUserId());
//                req.setPurpose(input.getPurpose());
//                indexCheckLog = input.getUserId() + "|" + input.getPurpose();
//            }
//
//            // default
//            req.setMerchantType("0102"); // test 0102
//            req.setCcy("704");
//            req.setMasterMerCode("970426");
//            req.setServiceCode("02");
//            req.setCountryCode("VN");
//
//            String checkSumReq = PGSecurity.md5(req.rawData(ChannelCommonConfig.MSB_QRCode_ENCRYP_KEY));
//            req.setChecksum(checkSumReq);
//            WriteInfoLog("2. MSB QR REQ - QrCodePayment API:", objectMapper.writeValueAsString(req));
//            String typeAPI = "QrCodePayment";
//
//            // TODO: call api MSB
//            return process(req, MsbConstants.CREATE_QRCODE_URL_SUFFIX, indexCheckLog, typeAPI);
//        } catch (Exception e) {
//            // TODO
//        }
//        return null;
//    }
//
//    public PGResponse CheckQrOrder(String inputStr) {
//        try {
//            WriteInfoLog("NGANLUONG - MSB CHECK ORDER");
//            DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
//
//            CheckOrderReq req = new CheckOrderReq();
//            req.setMerchantId(ChannelCommonConfig.MSB_QRCode_MERCHANT_ID);
//            req.setTerminalId(input.getTerminalId());
//            req.setPayType(input.getPayMethod());
//            req.setBillNumber(input.getBillNo());
//            WriteInfoLog("2. MSB QR REQ - CheckQrOrder API: ", objectMapper.writeValueAsString(req));
//            String typeAPI = "CheckQrOrder";
//            String res = ApiClient.sendRequest(ChannelCommonConfig.MSB_QRCode_URL_API_CALL_MSB, objectMapper.writeValueAsString(req), null, input.getBillNo(), typeAPI);
////        String res = "";
//
//            WriteInfoLog("3. MSB QR RES - CheckQrOrder API: ", res);
//            // Parse Response
//            CheckOrderRes qrCodeRes = objectMapper.readValue(res, CheckOrderRes.class);
//            PGResponse pgRes = new PGResponse();
//            pgRes.setStatus(true);
//            pgRes.setData(res);
//            pgRes.setErrorCode(qrCodeRes.getErrorCode());
//            pgRes.setMessage(qrCodeRes.getMessage());
//            //return objectMapper.writeValueAsString(pgRes);
//            return pgRes;
//        } catch (Exception e) {
//            // TODO
//        }
//        return null;
//    }
//
////    // call back notify MSB -> NL
////    public void CompleteQrPayment(String data) throws IOException {
////        WriteInfoLog("3. MSB QR REQ - msbCompleteVMQrPayment API: ", data);
////        Map<String, Object> map = new HashMap<>();
////        map.put("params", data);
////        String result = HttpUtil.send(ChannelCommonConfig.MSB_QRCode_URL_API_CALL_NGL, map, null);
////        WriteInfoLog("3. MSB QR RES - msbCompleteVMQrPayment API: ", result);
////    }
//
//    private PGResponse process(BaseRequest req, String api, String indexCheckLog, String typeAPI)
//            throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException, KeyManagementException {
//
//        String res = ApiClient.sendRequest(ChannelCommonConfig.MSB_QRCode_URL_API_CALL_MSB, objectMapper.writeValueAsString(req), null, indexCheckLog, typeAPI);
//        WriteInfoLog("3. MSB QR RES - QrCodePayment API: ", res);
//
//        // Parse Response
//        CreateQrCodeRes qrCodeRes = objectMapper.readValue(res, CreateQrCodeRes.class);
//        PGResponse pgRes = new PGResponse();
//        pgRes.setStatus(true);
//
//        // TODO: validate checksum
////		String checkSumRes = PGSecurity.md5(qrCodeRes.rawData(getPaymentAccount().getEncrypKey()));
////		if (qrCodeRes.getCode().equals("00") && !checkSumRes.equalsIgnoreCase(qrCodeRes.getChecksum())) {
////			pgRes.setErrorCode("06");
////			pgRes.setMessage("MSB Qr Payment Checksum Invalid");
////		} else {
//        pgRes.setErrorCode(qrCodeRes.getCode());
//        pgRes.setMessage(qrCodeRes.getMessage());
////		}
////        qrCodeRes.setCode(null);
////        qrCodeRes.setMessage(null);
////        qrCodeRes.setChecksum(null);
//
//        pgRes.setData(objectMapper.writeValueAsString(qrCodeRes));
//        //return objectMapper.writeValueAsString(pgRes);
//        return pgRes;
//    }
//
//    public static void main(String args[]) throws Exception {
//        String res = "{\"code\":\"00\",\"message\":\"Success\",\"data\":\"0002010102122624000697042601100106001236520483585303704540420005802VN5913NGANLUONG JSC6005HANOI62780110NL136423560317CTY CP NGAN LUONG052401190122151501NL136423560711110000032046304F6AD\",\"url\":\"\",\"checksum\":\"68D42A14347B723639EE28DAB5B32B43\",\"isDelete\":true,\"idQrcode\":\"3477\"}";
//        CreateQrCodeRes qrCodeRes = objectMapper.readValue(res, CreateQrCodeRes.class);
//        String rawData = qrCodeRes.rawData("msb1991@test");
//        System.out.println(rawData);
//        String checkSumRes = PGSecurity.md5(rawData);
//        System.out.println("1 " + qrCodeRes.getChecksum());
//        System.out.println("2 " + checkSumRes);
//    }
//}
