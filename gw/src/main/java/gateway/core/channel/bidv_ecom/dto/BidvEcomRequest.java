package gateway.core.channel.bidv_ecom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sonln@nganluong.vn
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BidvEcomRequest {
    @JsonProperty("Service_Id")
    private String serviceId;

    @JsonProperty("Merchant_Id")
    private String mid;

    @JsonProperty("Merchant_Name")
    private String merchantName;

    @JsonProperty("Trandate")
    private String Trandate;

    @JsonProperty("Trans_Id")
    private String transId;

    @JsonProperty("Trans_Desc")
    private String transDesc;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Curr")
    private String curr;

    @JsonProperty("Payer_Id")
    private String payerId;

    @JsonProperty("Payer_Name")
    private String payerName;

    @JsonProperty("Payer_Addr")
    private String payerAddr;

    @JsonProperty("Secure_Code")
    private String secureCode;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Custmer_Id")
    private String customerId;

    @JsonProperty("Customer_Name")
    private String customerName;

    @JsonProperty("IssueDate")
    private String issuedate;
}
