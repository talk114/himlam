package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.dong_a_bank.DABSecurity;
import gateway.core.channel.dong_a_bank.dto.req.DABNotifyEcomReq;
import gateway.core.channel.dong_a_bank.dto.req.UpdateOrderStatusReq;
import vn.nganluong.naba.channel.vib.request.*;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ClientTestVIBBank extends BaseTest {


    static {

        USER_CODE = "NL";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        CHANNEL_NAME = "VIB";
    }

    public static void main(String args[]) throws Exception {

        // Search so du Nha cung cap
        String function = "";
        String param = null;

//        function = "VIB_IBFT_VALID_ACCOUNT";
//        param = CreateValidAccountVIBRequets();
//
//        function = "VIB_IBFT_BALANCE_ACCOUNT";
//        param = getAccountBalance();

//        function = "VIB_IBFT_ADD_TRANSACTION";
//        param = addTransaction_VIBA();
//
//        function = "VIB_IBFT_STATUS_TRANSACTION";
//        param = getTransactionStatus_VIBA();
//
//
//        function = "VIB_IBFT_ADD_TRANSACTION";
//        param = addTransaction_NAPAS();
//
//        function = "VIB_IBFT_STATUS_TRANSACTION";
//        param = getTransactionStatus_NAPAS();
//
        function = "VIB_IBFT_HISTORY_TRANSACTION";
        param = getHistoryTransaction();



//		 function = "UpdateOrderStatus";
//		 param = updateOrderStatus();

//        function = "VIB_VA_CREATE_VA";
//        param = CreateVirtualAccountVIBRequets();

//        function = "VIB_VA_DELETE_VA";
//        param = DeleteVirtualAccountVIBRequest();

//        function = "VIB_VA_ENABLE_VA";
//        param = EnableVirtualAccountVIBRequest();

//        function = "VIB_VA_HISTORY_TRANSACTION_VA";
//        param = VAVIBGetTransactionHistoryOfVAVIBRequets();

//        function = "VIB_VA_HISTORY_TRANSACTION_VA_OF_REAL_ACCOUNT";
//        param = VAVIBGetTransactionHistoryOfRealAccountVIBRequest();

//        function = "VIB_VA_LIST_VA";
//        param = VAGetListVirtualAccountVIBRequest();


        callApi(param, function, 0);
        //resetChannel(CHANNEL_NAME, 2);
        // callNotify();
    }

    private static String CreateValidAccountVIBRequets() throws JsonProcessingException {
        ValidAccountVIBRequets req = new ValidAccountVIBRequets();
        req.setAccount_no("001704060026697");
        req.setAccount_type("VIB");

        req.setAccount_no("6666688888");
        req.setAccount_type("NAPAS");
        req.setBank_id("970403");
        return mapper.writeValueAsString(req);
    }

    private static String CreateVirtualAccountVIBRequets() throws JsonProcessingException {
        CreateVirtualAccountVIBRequets req = new CreateVirtualAccountVIBRequets();
//        req.setMerchant_code("10002");
//        req.setPhone_number("0968888888");
//        req.setAccount_name("Nguyen Van A");
        return mapper.writeValueAsString(req);
    }

    private static String DeleteVirtualAccountVIBRequest() throws JsonProcessingException {
        DeleteVirtualAccountVIBRequest req = new DeleteVirtualAccountVIBRequest();
        req.setVirtual_account_no("NLV100020968666888BZC4LFM");
        return mapper.writeValueAsString(req);
    }

    private static String VAVIBGetTransactionHistoryOfRealAccountVIBRequest() throws JsonProcessingException {
        VAVIBGetTransactionHistoryOfRealAccountVIBRequest req = new VAVIBGetTransactionHistoryOfRealAccountVIBRequest();
        req.setFrom_date("10/10/2020");
        req.setTo_date("22/12/2020");
        req.setPage_num("0");
        req.setPage_size("10");
        req.setActual_acct("002704060002045");
        req.setFrom_amt("10000");
        req.setTo_amt("100000000");
        req.setC_d("C");

        return mapper.writeValueAsString(req);
    }

    // VAVIBGetTransactionHistoryOfRealAccountVIBRequest

    // EnableVirtualAccountVIBRequest

    private static String EnableVirtualAccountVIBRequest() throws JsonProcessingException {
        EnableVirtualAccountVIBRequest req = new EnableVirtualAccountVIBRequest();
        req.setVirtual_account_no("NLV100020968666888BZC4LFM");
        req.setStatus("ENABLE");
        return mapper.writeValueAsString(req);
    }
    private static String VAGetListVirtualAccountVIBRequest() throws JsonProcessingException {
        VAGetListVirtualAccountVIBRequest req = new VAGetListVirtualAccountVIBRequest();
        req.setPage_num("1");
        req.setPage_size("10");
        return mapper.writeValueAsString(req);
    }

    // VAGetListVirtualAccountVIBRequest

    private static String VAVIBGetTransactionHistoryOfVAVIBRequets() throws JsonProcessingException {
        VAVIBGetTransactionHistoryOfVAVIBRequets req = new VAVIBGetTransactionHistoryOfVAVIBRequets();
        req.setFrom_date("21/12/2020");
        req.setTo_date("22/12/2020");
        req.setPage_num("1");
        req.setPage_size("10");
        req.setVa_result("Y");
        return mapper.writeValueAsString(req);
    }

    private static String getAccountBalance() throws JsonProcessingException {
        GetAccountBalanceVIBRequets req = new GetAccountBalanceVIBRequets();
        req.setAccount_no("002704060002045");
        return mapper.writeValueAsString(req);
    }

    private static String addTransaction_VIBA() throws JsonProcessingException {
        AddTransactionVIBRequets req = new AddTransactionVIBRequets();
        req.setTo_account("001704060026697");
        req.setAmount("500000");
        req.setNarrative("Kiem tra ma giao dich client trans 2021");
        req.setService_id("VIBA");
        req.setClient_transaction_id("VIB000AA089");
        req.setFeeside("OUR");
        return mapper.writeValueAsString(req);
    }

    private static String getTransactionStatus_VIBA() throws JsonProcessingException {
        GetTransactionStatusVIBRequets req = new GetTransactionStatusVIBRequets();
        req.setTransaction_type("ERPTRANSID");
        req.setService_type("VIBA");
        req.setClient_transaction_id("VIB000AA089");
        req.setFrom_date("20/11/2020");
        req.setTo_date("20/01/2021");
        return mapper.writeValueAsString(req);
    }

    private static String addTransaction_NAPAS() throws JsonProcessingException {
        AddTransactionVIBRequets req = new AddTransactionVIBRequets();
        req.setTo_account("6666688888");
        req.setAmount("600000");
        req.setNarrative("Thanh toan napas 2021");
        req.setService_id("SMLACCT");
        req.setClient_transaction_id("NAPAS000AA089");
        req.setFeeside("OUR");
        req.setAccount_name("NGUYEN LOC PHAT");
        req.setBen_name("NGUYEN LOC PHAT");
        req.setBen_bank_id("970403");
        req.setBen_bank_name("NH TMCP SAIGONTHUONGTIN(SACOMBANK)");
        return mapper.writeValueAsString(req);
    }

    private static String getTransactionStatus_NAPAS() throws JsonProcessingException {
        GetTransactionStatusVIBRequets req = new GetTransactionStatusVIBRequets();
        req.setTransaction_type("ERPTRANSID");
        req.setService_type("SMLACCT");
        req.setClient_transaction_id("NAPAS000AA089");
        req.setFrom_date("20/11/2020");
        req.setTo_date("20/01/2021");
        return mapper.writeValueAsString(req);
    }


    private static String getHistoryTransaction() throws JsonProcessingException {
        GetTransactionHistoryVIBRequets req = new GetTransactionHistoryVIBRequets();
        req.setC_d("");
        req.setAccount_no("002704060002045");
        req.setPage_num("1");
        req.setPage_size("500");
        req.setFrom_date("20/11/2020");
        req.setTo_date("20/01/2021");
        return mapper.writeValueAsString(req);
    }





    private static String updateOrderStatus() throws JsonProcessingException {
        UpdateOrderStatusReq req = new UpdateOrderStatusReq();
        req.setChange(0);
        req.setStatus("NOTICE");
        req.setOrderID("VM2498271570");
        return mapper.writeValueAsString(req);
    }

    private static String getTimeOrder() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(new Date());
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