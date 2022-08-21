package gateway.core.channel.bidv_ecom.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author sonln@nganluong.vn
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidvCallbackReq {
    @JsonProperty("Bank_Code")
    private String bankCode;

    @JsonProperty("Bank_Name")
    private String bankName;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Service_Id")
    private String serviceId;

    @JsonProperty("Merchant_Id")
    private String merchantId;

    @JsonProperty("Trans_Id")
    private String transId;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Result_Code")
    private String resultCode;

    @JsonProperty("TxnCode")
    private String txnCode;

    @JsonProperty("Trace_Number")
    private String traceNumber;

    @JsonProperty("More_Info")
    private String moreInfo;

    @JsonProperty("Secure_Code")
    private String secureCode;

}
