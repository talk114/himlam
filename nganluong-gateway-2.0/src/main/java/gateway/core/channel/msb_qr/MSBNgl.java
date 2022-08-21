//package gateway.core.channel.msb_qr;
//
//import gateway.core.channel.ChannelCommonConfig;
//import gateway.core.channel.msb_qr.service.impl.MSBServiceImpl;
//import gateway.core.dto.PGResponse;
//import gateway.core.util.HttpUtil;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MSBNgl extends MSBServiceImpl {
//
//    @Override
//    public PGResponse QrCodePayment(String inputStr) {
//        return super.QrCodePayment(inputStr);
//    }
//
//    @Override
//    public PGResponse CheckQrOrder(String inputStr) {
//        return super.CheckQrOrder(inputStr);
//    }
//
////	public void CompleteNLQrPayment(String data) throws IOException {
//    public String CompleteQrPayment(String data) throws IOException {
//        // TODO
//        WriteInfoLog("3. MSB QR REQ - msbCompleteVMQrPayment API: ", data);
//        // convert JSON string to Map
//        Map<String, String> map = objectMapper.readValue(data, Map.class);
//        Map<String, Object> mapData = new HashMap<>();
//        mapData.put("params", map.get("body"));
//        String result = HttpUtil.send(ChannelCommonConfig.MSB_QRCode_URL_API_CALL_NGL, mapData, null);
//        // TODO
//        WriteInfoLog("3. MSB QR RES - msbCompleteVMQrPayment API: ", result);
//        return "";
//    }
//}
