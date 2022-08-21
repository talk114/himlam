package gateway.core.channel.bidv_ecom;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.bidv_ecom.dto.BidvEcomResponse;

import java.io.IOException;

/**
 * @author sonln@nganluong.vn
 */
public class test {
    public static void main(String[] args) throws IOException {
        String a = "{\"serviceId\":\"019001\",\"merchantId\":\"019001\",\"trandate\":\"\",\"transId\":\"\",\"responseCode\":\"044\",\"responseTxnCode\":\"\",\"list\":\"\",\"redirectUrl\":\"\",\"secureCode\":\"b5d9b30c726592b6559a2862e5d800bb\"}";
        System.out.println(new ObjectMapper().readValue(a, BidvEcomResponse.class));
    }
}
