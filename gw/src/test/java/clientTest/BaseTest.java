package clientTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.dto.PGRequest;
import gateway.core.util.PGSecurity;
import gateway.core.util.RandomUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    protected static String CHANNEL_NAME;

    protected static String USER_CODE;

    protected static String MD5;

    protected static String USER_AUTH;

    protected static String PASS_AUTH;

    protected static ObjectMapper mapper = new ObjectMapper();

    public static void callApi(String param, String func, int isLocal)
            throws IOException, NoSuchAlgorithmException {
        String url = "";
        if (isLocal == 0)
            url = "http://localhost:8080/PaymentGateway/restful/api/request";
        else if (isLocal == 1)
            url = "http://10.0.0.180:8083/PaymentGateway/restful/api/request";
            //url = "https://gateway-sandbox.vimo.vn/PaymentGateway/restful/api/request";
        else if (isLocal == 2)
            url = "http://10.0.0.69:8080/PaymentGateway/restful/api/request";


        PGRequest request = new PGRequest();
        request.setChannelName(CHANNEL_NAME);
        request.setFnc(func);
        request.setPgUserCode(USER_CODE);
        request.setData(param);
        request.setChecksum(PGSecurity.md5(request.getData() + MD5));
        System.out.println(mapper.writeValueAsString(request));

        // String userPass = USER_AUTH + ":" + PASS_AUTH;
        //String responseFromService = HttpUtilTest.sendRequest(url, mapper.writeValueAsString(request), userPass);
        //System.out.println("Response String: " + responseFromService);
    }

    protected static void resetChannel(int isLocal) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String url = "";
        if(isLocal == 0)
            url = "http://localhost:8080/PaymentGateway/restful/api/resetChannel";
        else if(isLocal == 1)
            //url = "http://10.0.0.169:8081/PaymentGateway/restful/api/resetChannel";
            url = "http://192.168.11.40:8129/PaymentGateway/restful/api/resetChannel";

        else
            url = "https://gateway.vimo.vn/PaymentGateway/restful/api/resetChannel";

        PGRequest request = new PGRequest();
        request.setFnc("resetChannel");
        request.setPgUserCode("VIMO");
        request.setData(CHANNEL_NAME);
        request.setChecksum(PGSecurity.md5(request.getData() + "04392bb24a98c047dce89046f08ad188"));

        String userPass = "Vimo@2017" + ":" + "0290f14cf8c4fb4c38deef8479cab69b";
        String responseFromService = HttpUtilTest.sendRequest(url, mapper.writeValueAsString(request), userPass);
        System.out.println("Response String: " + responseFromService);
    }

    protected static void resetChannel(String channelName, int isLocal) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String url = "";
        if(isLocal == 0)
            url = "http://localhost:8082/PaymentGatewayDev/restful/api/resetChannel";
        else if(isLocal == 1)
            url = "http://10.0.0.70:8081/PaymentGateway/restful/api/resetChannel";
        else if(isLocal == 2)
            url = "http://10.0.0.69:8080/PaymentGateway/restful/api/resetChannel";

        PGRequest request = new PGRequest();
        request.setFnc("resetChannel");
        request.setPgUserCode("VIMO");
        request.setData(channelName);
        request.setChecksum(PGSecurity.md5(request.getData() + "04392bb24a98c047dce89046f08ad188"));

        String userPass = "Vimo@2017" + ":" + "0290f14cf8c4fb4c38deef8479cab69b";
        String responseFromService = HttpUtilTest.sendRequest(url, mapper.writeValueAsString(request), userPass);
        System.out.println("Response String: " + responseFromService);
    }

    protected static String callApiPartner(String request, String funcPartner, int isLocal) throws Exception {
        String url = "";
        if (isLocal == 0)
            url = "http://localhost:8080/restful/apiVM/" + funcPartner;
        else if (isLocal == 1) {
            url = "http://10.0.0.180:8083/PaymentGateway/restful/apiVM/" + funcPartner;
        } else {
            url = "http://10.0.0.69:8085/PaymentGateway/restful/apiVM/" + funcPartner;
        }

        String responseFromService = HttpUtilTest.sendRequest(url, request, null);
        System.out.println("Response String: " + responseFromService);
        return responseFromService;
    }

    protected static String getTrxTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    protected static void reloadAllChannel(int isLocal) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String url = "";
        if(isLocal == 0)
            url = "http://localhost:8082/PaymentGatewayDev/restful/api/reloadChannels";
        else if(isLocal == 1)
            url = "http://10.0.0.70:8081/PaymentGateway/restful/api/reloadChannels";

        PGRequest request = new PGRequest();
        request.setFnc("resetChannel");
        request.setPgUserCode("VIMO");
        request.setChecksum(PGSecurity.md5(request.getData() + "04392bb24a98c047dce89046f08ad188"));

        String userPass = "Vimo@2017" + ":" + "0290f14cf8c4fb4c38deef8479cab69b";
        String responseFromService = HttpUtilTest.sendRequest(url, mapper.writeValueAsString(request), userPass);
        System.out.println("Response String: " + responseFromService);
    }

    protected static String buildTransId(){
        return RandomUtil.randomDigitString(10, "VM");
    }

    public static void main(String args[]) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
        System.out.println(df.format(new Date()));
        System.out.println(getTrxTime());
    }
}
