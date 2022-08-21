package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DomesticAccountFundTransfer {
    @JsonProperty("AccountNo")
    private String accountNo;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("RefNumber")
    private String refNumber;

    @JsonProperty("SenderName")
    private String senderName;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("InqRefNumber")
    private String inqRefNumber;



}
