package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DomesticAccountInquiry {
    @JsonProperty("AccountNo")
    private String accountNo;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("RefNumber")
    private String refNumber;


}
