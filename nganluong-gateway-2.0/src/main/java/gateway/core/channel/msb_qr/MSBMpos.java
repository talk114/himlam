//package gateway.core.channel.msb_qr;
//
//import gateway.core.channel.msb_qr.service.impl.MSBServiceImpl;
//import gateway.core.dto.PGResponse;
//import gateway.core.util.HttpUtil;
//import vn.nganluong.naba.entities.PaymentAccount;
//import vn.nganluong.naba.entities.PgUser;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MSBMpos extends MSBServiceImpl {
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
//    public void CompleteQrPayment(String data) throws IOException {
//        Map<String, Object> map = new HashMap<>();
//        map.put("params", data);
//        // TODO TrungDD
//        PaymentAccount paymentAccount = new PaymentAccount();
//        PgUser pgUser  = new PgUser();
//        String result = HttpUtil.send(paymentAccount.getUrlApi(), map, null);
//        // String result = HttpUtil.send(getPaymentAccount().getUrlApi(), map, null);
//        // TODO
//        // getPaymentUser()
//        WriteInfoLog("3. " + pgUser.getCode() + " response", result);
//    }
//
//}
