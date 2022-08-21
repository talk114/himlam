package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.dto.request.DataRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ClientTestOnePay extends BaseTest {

    static {
        USER_CODE = "NL";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        CHANNEL_NAME = "ONEPAY";
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String func = "";
        String param = null;

//        func = "VerifyCard";
//        param = paramForVerifyCard();

//        func = "VerifyAuthen";
//        param = paramForVerifyOTP();

//        func = "Query";
//        param = paramForQuery();

        func = "refund";
        param =paramForRefund();
//
//        func = "VerifyCard";
//        param = paramForVerifyCard_UAT();

        callApi(param, func, 0);
    }

    private static String paramForVerifyCard_UAT() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setVpcVersion("2");
        dataRequest.setVpcCommand("pay");
//        dataRequest.setVpcAccessCode("D67342C2");
//        dataRequest.setVpcMerchant("ONEPAY");
        dataRequest.setVpcMerchTxnRef("NL21992728");
        dataRequest.setVpcOrderInfo("NL21992728");
        dataRequest.setVpcAmount("1000000");
        dataRequest.setVpcLocale("vn");
        dataRequest.setVpcCurrency("VND");
        dataRequest.setVpcTicketNo("127.0.0.1");
        dataRequest.setVpcReturnURL("https:\\/\\/www.nganluong.vn\\/onepay.return.php");
        dataRequest.setAgainLink("https:\\/\\/www.nganluong.vn\\/");
        dataRequest.setTitle("Thanh toan qua vi dien tu NganLuong.vn");
        dataRequest.setVpcCustomerUserAgent("");
        dataRequest.setVpcBankId("1");
        dataRequest.setVpcCardNo("9704250000000001");
        dataRequest.setVpcCardName("NGUYEN VAN A");
        dataRequest.setVpcCardMonth("01");
        dataRequest.setVpcCardYear("13");
        dataRequest.setVpcCardType("");
        dataRequest.setVpcAuthMethod("SMS");
        return mapper.writeValueAsString(dataRequest);
    }

    private static String paramForRefund() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setVpcMerchTxnRef("NGLA" + System.currentTimeMillis());
        dataRequest.setVpcOrgMerchTxnRef("NGLA1583907371909");
        dataRequest.setVpcAmount("50000");
        dataRequest.setVpcOperator("thanhnb");// user thuc hien refund
        dataRequest.setVpcVersion("2");
        return mapper.writeValueAsString(dataRequest);
    }

    public static String paramForQuery() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setVpcVersion("1");
        dataRequest.setVpcCommand("queryDR");
        dataRequest.setVpcMerchTxnRef("NGLA1583143773826");
        return mapper.writeValueAsString(dataRequest);
    }

    public static String paramForVerifyCard() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setVpcVersion("2");
        dataRequest.setVpcCommand("pay");
        dataRequest.setVpcMerchTxnRef("NGLA" + System.currentTimeMillis());
        dataRequest.setVpcOrderInfo("NGLA" + System.currentTimeMillis());
        dataRequest.setVpcAmount("1000000");
        dataRequest.setVpcLocale("vn");
        dataRequest.setVpcCurrency("VND");
        dataRequest.setVpcTicketNo("123.24.170.147");
        dataRequest.setVpcReturnURL("https://www.nganluong.vn/onepay.return.php");
        dataRequest.setAgainLink("https://www.nganluong.vn/");
        dataRequest.setTitle("Thanh toan qua vi dien tu NganLuong.vn");
        dataRequest.setVpcCustomerUserAgent("thanhnb");
        dataRequest.setVpcBankId("15");
        dataRequest.setVpcCardNo("9704250000000001");
        dataRequest.setVpcCardName("NGUYEN VAN A");
        dataRequest.setVpcCardMonth("01");
        dataRequest.setVpcCardYear("13");
        return mapper.writeValueAsString(dataRequest);
    }

    public static String paramForVerifyOTP() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setVpcTicketNo("123.24.170.147");
        dataRequest.setVpcCustomerUserAgent("thanhnb");
        dataRequest.setVpcBankId("15");
        dataRequest.setVpcAuthURL("https%3A%2F%2Fmtf.onepay.vn%2Fonecomm-pay%2Fabbauth.op%3Ftransaction_id%3DTmZsR0HnoeObmbr_LcQcEYFrn8AeMyoHOmpSp-P8_C_0QDQ%26amount%3D10000%26merchant_name%3DONEPAY%2BTEST%26merchant_id%3DONEPAY%26order_info%3DNGLA1583907371909%26language%3Dvn%26request_locale%3Dvn");
        dataRequest.setVpcOtp("123456");
        dataRequest.setVpcReturnURL("https%3A%2F%2Fwww.nganluong.vn%2Fonepay.return.php");
        return mapper.writeValueAsString(dataRequest);
    }
}
