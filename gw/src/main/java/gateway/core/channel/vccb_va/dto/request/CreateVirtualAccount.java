package gateway.core.channel.vccb_va.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class CreateVirtualAccount {

    @JsonProperty(value = "accNameSuffix")
    private String accountNameSuffix;

    @JsonProperty(value = "accNoSuffix")
    private String accountSuffix;

    private String partnerCode;

    @JsonProperty(value = "accType")
    private String accountType;
}
