package gateway.core.channel.bidv_ecom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author sonln@nganluong.vn
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BidvEcomResponse {
    private String serviceId;
    private String merchantId;
    private String trandate;
    private String transId;
    private String responseCode;
    private String responseTxnCode;
    private String list;
    private String redirectUrl;
    private String secureCode;

}
