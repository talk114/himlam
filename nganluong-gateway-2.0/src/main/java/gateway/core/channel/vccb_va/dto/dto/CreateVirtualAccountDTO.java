package gateway.core.channel.vccb_va.dto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateVirtualAccountDTO {
    @JsonProperty(value = "accNameSuffix")
    private String accountNameSuffix;

    @JsonProperty(value = "accNoSuffix")
    private String accountSuffix;

    private String partnerCode;

    @JsonProperty(value = "accType")
    private String accountType;

    @JsonProperty(value = "requestId")
    private String requestId;

    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
}
