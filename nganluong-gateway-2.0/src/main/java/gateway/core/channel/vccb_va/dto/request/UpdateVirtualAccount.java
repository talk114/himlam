package gateway.core.channel.vccb_va.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class UpdateVirtualAccount {
    @JsonProperty("accNo")
    private String accountNumber;

    @JsonProperty("accNameSuffix")
    private String accountNameSuffix;
}
