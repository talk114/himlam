package gateway.core.channel.migs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.migs.entity.Secure;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckEnrollmentRes {
    @JsonProperty("3DSecureId")
    private String _3DSecureId;
    @JsonProperty("3DSecure")
    private Secure secure;
    private String merchant;
    private String veResEnrolled;
}
