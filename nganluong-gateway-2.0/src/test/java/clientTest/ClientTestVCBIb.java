package clientTest;

import gateway.core.dto.request.DataRequest;

public class ClientTestVCBIb extends BaseTest {
    static {
        CHANNEL_NAME = "VCB_IB";
        USER_CODE = "NL";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
    }

    public static void main(String[] args) throws Exception {
        String func = "";

        DataRequest param = null;
//        func = "query";
//        param = paramQueryTrans();

        func = "refund";
        param = paramRefund();
//        func = "verifyPayment";
//        param = paramVerifyPayment();

        callApi(mapper.writeValueAsString(param), func, 0);
    }

    private static DataRequest paramVerifyPayment() {
        DataRequest request = new DataRequest();
        request.setTransId(buildTransId());
        request.setAmount(1000d);

        request.setLanguage("VI");
        request.setClientIp("123123123123");
        request.setBenAddInfo("Thông tin bổ sung");
        return request;
    }

    private static DataRequest paramRefund() {
        DataRequest request = new DataRequest();
        request.setTransId(buildTransId());
        request.setAmount(1000d);

        request.setRefTransId("NGLA" + System.currentTimeMillis());
        return request;
    }

    private static DataRequest paramQueryTrans() {
        DataRequest req = new DataRequest();
        req.setTransId(buildTransId());
        req.setAmount(1000d);

        req.setInquiryTransId("VM5614697164");
        return req;
    }
}
