package clientTest;


import gateway.core.channel.bidv.bidv_transfer_247.dto.req.DSRequest;
import gateway.core.dto.request.DataRequest;

public class ClientTestBIDVTransfer247 extends BaseTest {

    static {
        CHANNEL_NAME = "BIDV_TRANSFER_247";
        USER_CODE = "NL";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
    }

    public static void main(String args[]) throws Exception {

        String func = "";
        Object param = null;
        func = "getFileBIDVDaily";
        param = getDailyCdr();
//        func = "getListBank247";
//        param = getListBank247();
//        func = "getName247";
//        param = getName247();
//        func = "tranfer2Bank247";
//        param = tranfer2Bank247();
//        func = "getNameBidv";
//        param = getNameBidv();
//        func = "inquery";
//        param = inquery();
//        func = "checkProviderBalance";
//        param = checkProviderBalance();
//        tranfer2Bank247();
        callApi(mapper.writeValueAsString(param), func, 0);
    }
    public static DataRequest getMonthlyCdr() {
        DataRequest dataRequest = new DataRequest();
        return dataRequest;
    }
    public static DSRequest getDailyCdr() {
        DSRequest dsRequest = new DSRequest();
        dsRequest.setMessageCode("1001");
        // Test doi soat theo Ngay - File BIDVOUT_019_20200727
//        dsRequest.setTranDate("20200917");//co giao dich
        dsRequest.setTranDate("20201009");
        dsRequest.setFileType("1");

        // Test doi soat theo Ngay - File BIDV_REFUND_019_20200727
//        dsRequest.setTranDate("20200728");//co giao dich
//        dsRequest.setTranDate("20201009");
//        dsRequest.setFileType("3");
        return dsRequest;
    }
    private static DataRequest getNameBidv() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChannel("212901");
        dataRequest.setCardNumber("123123123123123");
        return dataRequest;
    }

    private static DataRequest tranfer2Bank247() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChannel("212901");
        dataRequest.setTransId("NL" + System.currentTimeMillis());
        //dataRequest.setMerchantName("Chi ho chuyen tien 247");
        dataRequest.setAmount(100000.0);
        dataRequest.setCardNumber("12010004003096");
//        dataRequest.setCardHolerName("NGUYEN VAN AN");
        dataRequest.setBankCode("970418");
        return dataRequest;
    }

    private static DataRequest getName247() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChannel("212901");
        dataRequest.setCardNumber("0129837294");
//        dataRequest.setCardHolerName("NGUYEN VAN AN");
        dataRequest.setBankCode("970406");
        return dataRequest;
    }

    private static DataRequest getListBank247() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChannel("212901");
//        dataRequest.setCardNumber("123456789123456789");
//        dataRequest.setCardHolerName("NGUYEN VAN AN");
        return dataRequest;
    }

    private static DataRequest inquery() {
        DataRequest dataRequest = new DataRequest();
//        dataRequest.setTransId("NL" + System.currentTimeMillis());
        dataRequest.setTransId("NL1595845372641");
        dataRequest.setTransTime("200727");
        return dataRequest;
    }

    public static DataRequest checkProviderBalance() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setChannel("212901");
        return dataRequest;
    }
}
