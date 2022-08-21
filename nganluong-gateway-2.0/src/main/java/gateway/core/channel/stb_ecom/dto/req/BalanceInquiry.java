package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceInquiry {
    @JsonProperty("RefNumber")
    private String refNumber;
}
