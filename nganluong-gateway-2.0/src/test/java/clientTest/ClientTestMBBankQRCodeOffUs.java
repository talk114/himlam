package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.dto.request.DataRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ClientTestMBBankQRCodeOffUs extends BaseTest {
    static {
        USER_CODE = "NL";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        CHANNEL_NAME = "MBBANK_QRCODE_OFFUS";
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String func = "";
        String param = "";

        func = "createQRCode";
        param = paramForCreateQrCode();

//        func = "checkOrder";
//        param = paramForCheckOrder();

        callApi(param, func, 0);
    }

    public static String paramForCheckOrder() throws JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        DataRequest dataRequest = new DataRequest();
//        dataRequest.setTraceTransfer("20040615283077435170");
//        dataRequest.setBillNumber("NL1586155154");
        //type = 2 - dong
//        dataRequest.setConsumerLabel("nbthanh");
//        dataRequest.setTerm("1586159015");
        //type = 3
        dataRequest.setReferenceLabelTime("1586157312");
        dataRequest.setReferenceLabelCode("NGLA158615731");
        dataRequest.setTransId(uuid.toString());
        return mapper.writeValueAsString(dataRequest);
    }


    public static String paramForCreateQrCode() throws JsonProcessingException {
        //QR code type = 1- động
        UUID uuid = UUID.randomUUID();
        DataRequest dataRequest = new DataRequest();
        // Kiểu 1 - Động
//        dataRequest.setTerminalId("NL11");
//        dataRequest.setQrcodeType(1);
//        dataRequest.setInitMethod(12); // Qr code Động
//        dataRequest.setAmount(1000000.0);
//        dataRequest.setBillNumber("raccoonT1");
//        dataRequest.setAdditionalAddress(1);
//        dataRequest.setAdditionalMobile(0);
//        dataRequest.setAdditionalEmail(1);
         //Kiểu 1 - Tĩnh
//        dataRequest.setTerminalId("NL11");
//        dataRequest.setQrcodeType(1);
//        dataRequest.setInitMethod(11);
//        dataRequest.setTransactionPurpose("Ten dich vu1");
//        dataRequest.setAdditionalAddress(1);
//        dataRequest.setAdditionalMobile(0);
//        dataRequest.setAdditionalEmail(1);
        //Type 2- Động
//        dataRequest.setTerminalId("NL11");
//        dataRequest.setQrcodeType(2);
//        dataRequest.setInitMethod(12);
//        dataRequest.setConsumerLabel("nbthanh");
//        dataRequest.setTransactionPurpose("ten dich vu 1");
//        dataRequest.setTerm("1586159015");
        //Type 2- Tinh
//        dataRequest.setTerminalId("NL11");
//        dataRequest.setQrcodeType(2);
//        dataRequest.setInitMethod(11);
//        dataRequest.setConsumerLabel("nbthanh");
//        dataRequest.setTransactionPurpose("ten dich vu 1");
//        dataRequest.setTerm("1586161517");
        //Type 3
        dataRequest.setTerminalId("NL21");
        dataRequest.setQrcodeType(4);
        dataRequest.setInitMethod(12);
        dataRequest.setAmount(10000.0);
        dataRequest.setReferenceLabelTime("1599301362");
        dataRequest.setReferenceLabelCode("5061399");
        dataRequest.setTransactionPurpose("Ten dich vu1");
//        dataRequest.setConsumerLabel("TokuroFujiwara");
        dataRequest.setAdditionalAddress(1);
        dataRequest.setAdditionalMobile(0);
        dataRequest.setAdditionalEmail(1);

        dataRequest.setTransId(uuid.toString());
        return mapper.writeValueAsString(dataRequest);
    }
}
