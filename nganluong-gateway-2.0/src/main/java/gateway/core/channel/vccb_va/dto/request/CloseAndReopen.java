package gateway.core.channel.vccb_va.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseAndReopen {
    @JsonProperty("accNo")
    private String accountNumber;
}
