package gateway.core.channel.migs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.migs.entity.Secure;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sonln
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessAcsResultRes {
    @JsonProperty("3DSecure")
    private Secure _3DSecure;
    @JsonProperty("3DSecureId")
    private String _3DSecureId;
    private String merchant;
}
