package clientTest;

import clientTest.vcb_ecom.PgVcbEcomSoapService_PortType;
import clientTest.vcb_ecom.PgVcbEcomSoapService_ServiceLocator;
import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.dto.request.DataRequest;

import java.net.URL;

public class ClientTestVCBEcom extends BaseTest {
    static {
		CHANNEL_NAME = "VCB_ECOM";
		USER_CODE = "NL";
		MD5 = "04392bb24a98c047dce89046f08ad188";
		USER_AUTH = "NL@2017";
		PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";

//        CHANNEL_NAME = "VCB_ECOM";
//        USER_CODE = "VIMO";
//        MD5 = "04392bb24a98c047dce89046f08ad188";
//        USER_AUTH = "Vimo@2017";
//        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
    }

    public static void main(String[] args) throws Exception {
        String func = "";
        DataRequest param = null;
//////
//        func = "VerifyCard2";
//        param = paramVerifyCard();

//		 func = "VerifyOtp2";
//		 param= paramVerifyOtp();

//        func = "Query";
//        param = paramQueryTrans();

        func = "Refund";
        param = paramRefund();

        callApi(mapper.writeValueAsString(param), func, 0);
//		resetChannel(1);

        // callPartner(1);

        // ObjectMapper mapper = new ObjectMapper();
        // String rs = "{\"errCode\":\"95\",\"errDesc\":\"Kh
        // VCB\",\"redirectUrl\":\"a3D.html\"}";
        // VcbNotifyDataRes res = mapper.readValue(rs, VcbNotifyDataRes.class);
        // System.out.println(mapper.writeValueAsString(res));
    }

    private static void callPartner(int server) throws JsonProcessingException, Exception {
        PgVcbEcomSoapService_ServiceLocator service = new PgVcbEcomSoapService_ServiceLocator();

        String url = "";
        if (server == 0)
            url = "http://localhost:8080/PaymentGatewayDev/PgVcbEcomSoapService";
        else if (server == 1)
            url = "http://10.0.0.70:8081/PaymentGateway/PgVcbEcomSoapService";

        URL portAddress = new URL(url);
        PgVcbEcomSoapService_PortType proxy = service.getPgVcbEcomSoapServicePort(portAddress);

        String req = "eyJQYXJ0bmVyVHJhbklkIjoiTkw1MDMxMzgxIiwiRGF0YSI6Ijh0ZUdHcDVKWnRtdTB5ZERLMVcrZlc3WEdEeXVyUndydjgxY1pYeldzc1NNQjN6U0RmWGZxNDU3dzd5Z0lyR0RQQXZTbE44d210ZlFMdVBHbjRFVVMwMzE4cGdrUWxsS2Yzb0hGRjVxOHI5enAxS2ZQKzkzMUFwN2UwK0hxYW1YVWpGQjZqQm9HT1ltNjQ5VmdmbzQxZz09IiwiU2lnbmF0dXJlIjoicmdmTVllTG96YmlMYzNXaTdsUEdNWHFoNWhaN1FBaVBCZXVnam1Wb2NGRkVVcGc1anRycnFKalFYVlk1Y2tLY0FIeDZBaVRRdkpHMHN1QlFnTW1xTGtQTldRd2tDZ01FUURnY0dGdStIL1Yvb0tjd2dOd0taSEFUQ2hIZTAxSExhbVQ4Y3R3WUo2M3hBRmFjR1I5UXV4U3gvNThnMm9BcEl2aHRTRjh5OXhMdm5EazNreFF4NEdBekxGQmNqY2Nld0lwdDIxYXloeFNnMG56Tms5OTI1Uit5QTZaSUU3alIrV25CMWxOa3UzeFhkV0t4TnpkMXZ4cCttVXNsRzFLa0hnc2t4TkpWRjZiTTBLVW5kYll2NVpsNngxbmcvVm5UR01tWkVQd1oxaHl0b0ZneE94YVV5RGJYMkJUSG5DZ1R1Ny9KNm9pLzMwbzNJZDJrUmFMcUVnPT0ifQ==";
        //System.out.println(proxy.notifyEcomVimo(paramNotify()));
        // proxy.notifyEcomNganluong(req);
    }

//    private static String paramNotify() throws JsonProcessingException, Exception {
//        // String partnerTransId = "VM0001995994";
//        // VcbNotifyDataReq data = new VcbNotifyDataReq();
//        // data.setVcbTranId(buildTransId("VCB"));
//        // data.setPartnerId(partnerTransId);
//
//        String reqStr = "{\"errCode\":\"17\",\"errDesc\":\"\",\"PartnerId\":\"VM0001996247\",\"VcbTranId\":\"VCB8156410711\",\"AddInfo\":null}";
//        String dataencrypred = VCBEcomAtmSecurity.encryptAES2(reqStr);
//
//        VcbNotifyReq req = new VcbNotifyReq();
//        req.setData(dataencrypred);
//        req.setPartnerTranId("VM0001996247");
//        req.setSignature(VCBEcomSecurity.sign(req.rawData()));
//
//        return Base64.encodeBase64String(mapper.writeValueAsString(req).getBytes());
//    }

    private static DataRequest paramVerifyCard() {
        DataRequest req = new DataRequest();
        req.setTransId(buildTransId());
        req.setAmount(10000d);

        req.setCardNumber("9704366803594746094"); // 9704366803594746094
        // 9704366603204308255
        req.setCardHolerName("NGUYEN HUU NHAN");
        req.setCardIssueDate("1018"); // 0718 1018
        req.setDescription("Vinh test 2");
        req.setClientIp("1.1.1.1");
        req.setPartnerId("PARTNER_ID_TEST");
        req.setMerchantId("MERCHANT_ID_TEST");
        req.setPartnerPassword("PARTNER_PASSWORD_TEST");
        return req;
    }

    private static DataRequest paramVerifyOtp() {
        DataRequest req = new DataRequest();
        req.setTransId("VM4765931612");
        req.setAmount(10000d);

        req.setCardNumber("9704366803594746094"); // 9704366803594746094
        // 9704366603204308255
        req.setCardHolerName("NGUYEN HUU NHAN");
        req.setCardIssueDate("1018"); // 0718 1018

        req.setOtp("123456");
        req.setHashCode("5202ed0c3a3c5e52281a789c0dcb57d0");
        req.setPartnerId("PARTNER_ID_TEST");
        req.setMerchantId("MERCHANT_ID_TEST");
        req.setPartnerPassword("PARTNER_PASSWORD_TEST");
//		req.setBankTransId("");
        return req;
    }

    private static DataRequest paramQueryTrans() {
        DataRequest req = new DataRequest();
        req.setTransId(buildTransId());
        req.setAmount(1000d);
        req.setInquiryTransId("VM5614697164");
        return req;
    }

    private static DataRequest paramRefund() {
        DataRequest req = new DataRequest();
        req.setTransId("VM7539547268");
        req.setRefTransId(buildTransId());
        req.setAmount(10000d);
        return req;

    }
}
