package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.dong_a_bank.DABSecurity;
import gateway.core.channel.dong_a_bank.dto.req.DABNotifyEcomReq;
import gateway.core.channel.mb.dto.MBEcomDayReconciliateReq;
import gateway.core.channel.mb.dto.request.MBConfirmTransactionMBRequets;
import gateway.core.channel.mb.dto.request.MBCreateTransactionMBRequets;
import gateway.core.channel.mb.dto.request.MBRevertTransactionMBRequets;
import gateway.core.channel.mb.dto.request.MBStatusTransactionMBRequets;


public class ClientTestMBBank extends BaseTest {


    static {

        USER_CODE = "NL";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        CHANNEL_NAME = "MB";
    }

    public static void main(String args[]) throws Exception {

        // Search so du Nha cung cap
        String function = "";
        String param = null;
//
//        function = "MB_ECOM_ADD_TRANSACTION";
//        param = addTransaction();

//        function = "MB_ECOM_CONFIRM_TRANSACTION";
//        param = confirmTransaction();
//
        function = "MB_ECOM_REVERT_TRANSACTION";
        param = revertTransaction();
//
//        function = "MB_ECOM_STATUS_TRANSACTION";
//        param = statusTransaction();

//        function = "MB_ECOM_RECONCILIATION_DAY";
//        param = MBEcomDayReconciliateReq();


        callApi(param, function, 0);
        //resetChannel(CHANNEL_NAME, 2);
        // callNotify();
    }

    private static String addTransaction() throws JsonProcessingException {
        MBCreateTransactionMBRequets req = new MBCreateTransactionMBRequets();
        req.setClient_transaction_id("MB100ACCBA4");
        req.setCard_name("AUTO AUTNCZSIC");
        req.setCard_no("9704229200000093624");
        req.setAmount("160000");
        req.setDate_open("2020-12");
        req.setFee("10000");
        req.setMerchant("8998");
        req.setMobile("0973214568");
        req.setPayment_description("Chuyen khoan tien dien nuoc");
        req.setService_type("Thu tien ECOM");

        return mapper.writeValueAsString(req);
    }

    private static String confirmTransaction() throws JsonProcessingException {
        MBConfirmTransactionMBRequets req = new MBConfirmTransactionMBRequets();
        req.setClient_transaction_id("MBAAAA001");
        req.setOtp("944887184");
        return mapper.writeValueAsString(req);
    }

    private static String revertTransaction() throws JsonProcessingException {
        MBRevertTransactionMBRequets req = new MBRevertTransactionMBRequets();
        req.setClient_transaction_id("MBAAAA001");
        req.setAmount("160000");
        return mapper.writeValueAsString(req);
    }

    private static String statusTransaction() throws JsonProcessingException {
        MBStatusTransactionMBRequets req = new MBStatusTransactionMBRequets();
        req.setClient_transaction_id("MBAAAA001");
        return mapper.writeValueAsString(req);
    }

    private static String MBEcomDayReconciliateReq() throws JsonProcessingException {
        MBEcomDayReconciliateReq req = new MBEcomDayReconciliateReq();
        req.setFrom_date("20201223");
        req.setTo_date("20201223");
        return mapper.writeValueAsString(req);
    }


    private static String callNotify() throws Exception {
        DABNotifyEcomReq req = new DABNotifyEcomReq();
        req.setPartnerCode("DAB");
        req.setOrderId("NL5028240");
        req.setState(1);
        String data = req.getPartnerCode() + "|" + req.getOrderId() + "|" + req.getState();
        req.setDataSign(DABSecurity.sign(data, "/var/lib/payment_gateway_test/key/dong_a_bank/dab_nl_pri.pem"));

        return callApiPartner(mapper.writeValueAsString(req), "dongabankCallbackNganluong", 1);
    }

}