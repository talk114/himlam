package clientTest;


import gateway.core.channel.msb_ecom.dto.req.RootRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ClientTestMSBEcom extends BaseTest {
    static {
        CHANNEL_NAME = "MSB_ECOM";
        USER_CODE = "NL";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
    }

    public static void main(String[] args) throws Exception {


        String func = "";
        RootRequest param = null;

        // TEST

//        func = "MSB_ECOM_CREATE_PAYMENT";
//        param = createPayment();

//        func = "MSB_ECOM_VERIFY_TRANSACTION";
//        param = verifyTransaction();

//        func = "resendOTP";
//        param = resendOTP();

        func = "MSB_ECOM_INQUIRY_TRANSACTION";
        param = inquiryTransaction();

        callApi(mapper.writeValueAsString(param), func, 1);
//		 resetChannel(1);
    }

    private static RootRequest createPayment() {
        RootRequest req = new RootRequest();
        req.setCardName("MSB BANK");
        req.setCardNumber("9704269980068382");
        req.setReleaseMonth(11);
        req.setReleaseYear(22);
        req.setAmount(100000);
        req.setTransId("NL" + System.currentTimeMillis());
        req.setOrderInfo("Thanh toan DH:5067747");
        return req;
    }

    private static RootRequest verifyTransaction() {
        RootRequest req = new RootRequest();
        req.setRequestTransId("201109113043ECOM0110000000000026");
        req.setOtp("11111111");
        return req;
    }


    private static RootRequest resendOTP() {
        RootRequest req = new RootRequest();
        req.setRequestTransId("201109113043ECOM0110000000000026");
        return req;
    }


    private static RootRequest inquiryTransaction() {
        RootRequest req = new RootRequest();
        req.setRequestTransId("201109113043ECOM0110000000000026");
        return req;
    }


    protected static String getTrxTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return df.format(new Date());
    }

    public static int getRandomNumberInts() {
        Random random = new Random();
        int x = random.ints(1000, (100000000 + 1)).findFirst().getAsInt();
        return x;
    }

}
