package clientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.dong_a_bank.DABSecurity;
import gateway.core.channel.dong_a_bank.dto.req.CreateOrderReq;
import gateway.core.channel.dong_a_bank.dto.req.DABNotifyEcomReq;
import gateway.core.channel.dong_a_bank.dto.req.UpdateOrderStatusReq;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ClientTestDongABank extends BaseTest {

    // http://192.168.26.10:8081/PaymentGateway/restful/apiVM/agbApiNgl
    // http://192.168.26.9:8080/PaymentGateway/restful/apiVM/agbApiNgl

    static {
//		CHANNEL_NAME = "DONG_A_BANK_NGL";
//		USER_CODE = "NL";
//		MD5 = "06dc6699dc1c08cf2772890ae9a399cb";
//		USER_AUTH = "NglDAB@2019";
//		PASS_AUTH = "19ccdadc2a9ea2b584592db14b768e88";

        USER_CODE = "NL";
        USER_AUTH = "NL@2017";
        PASS_AUTH = "0290f14cf8c4fb4c38deef8479cab69b";
        MD5 = "04392bb24a98c047dce89046f08ad188";
        CHANNEL_NAME = "DONG_A_BANK";
    }

    public static void main(String args[]) throws Exception {

        // Search so du Nha cung cap
        String function = "";
        String param = null;

        function = "CreateOrder";
        param = createOrder();

//		 function = "UpdateOrderStatus";
//		 param = updateOrderStatus();

        callApi(param, function, 0);
        //resetChannel(CHANNEL_NAME, 2);
        // callNotify();
    }

    private static String createOrder() throws JsonProcessingException {
        CreateOrderReq req = new CreateOrderReq();
        req.setOrderID(buildTransId());
        req.setCustName("NGUYEN VAN TAI");
        req.setCustAddress("Minh Khai - Ha noi");
        req.setDateOrder(getTimeOrder());
        req.setPhone("0123666999");
        req.setDateDelivery("17/12/2020");
        req.setAddressDelivery("TEST");
        req.setAmount(50000d);

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