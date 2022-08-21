package gateway.core.channel.vccb_va.dto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateVirtualAccountDto {
    @JsonProperty("accNo")
    private String accountNumber;

    @JsonProperty("accNameSuffix")
    private String accountNameSuffix;

    private String requestId;

    private String phoneNumber;
}
