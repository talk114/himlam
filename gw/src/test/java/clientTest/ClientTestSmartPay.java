package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.dto.request.DataRequest;

public class ClientTestSmartPay extends BaseTest {

    static {
        USER_CODE = "NL";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        CHANNEL_NAME = "SMART_PAY";
    }

    public static void main(String args[]) throws Exception {

        // Search so du Nha cung cap
        String function = "";
        String param = null;

//        function = "createOrder";
//        param = paramForCreateOrder();

//
        function = "queryOrder";
        param = paramForQueryOrder();


//        function = "queryOrder";
//        param = paramForQueryOrder();

//        function = "sendOTP";
//        param = paramForSendOTP();

//        function = "verifyOTP";
//        param = paramForVerifyOtp();

        //resetChannel(1);
        callApi(param, function, 0);
    }

    public static String paramForCreateOrder() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setTransId("NGLA" + System.currentTimeMillis());
        dataRequest.setOrderCode("NGLA" + System.currentTimeMillis());
        dataRequest.setTransTime(String.valueOf(System.currentTimeMillis()));
        dataRequest.setAmount(100000D);
        dataRequest.setNotifyUrl("nganluong.vn");
        dataRequest.setDescription("description");
        return mapper.writeValueAsString(dataRequest);
    }

    public static String paramForQueryOrder() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        long time  = System.currentTimeMillis();
        System.out.println("TIME = " + time);
        dataRequest.setTransId("NGLA" + time);
        dataRequest.setOrderCode("NGLA1581070725693");
        dataRequest.setPrepayId("20200207000000001");
        return mapper.writeValueAsString(dataRequest);
    }

    public static String paramForSendOTP() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        long time  = System.currentTimeMillis();
        System.out.println("TIME = " + time);
        dataRequest.setTransId("NGLA" + time);
        dataRequest.setCustomerPhone("0903151178");
        return mapper.writeValueAsString(dataRequest);
    }

    public static String paramForVerifyOtp() throws JsonProcessingException {
        DataRequest dataRequest = new DataRequest();
        long time  = System.currentTimeMillis();
        System.out.println("TIME = " + time);
        dataRequest.setTransId("NGLA" + time);
        dataRequest.setCustomerPhone("0903151178");
        dataRequest.setOtp("1111111111111");
        dataRequest.setTransIdOTP("27601DNGQBF135QES5STCP32VHCS7");
        return mapper.writeValueAsString(dataRequest);
    }

}
