package gateway.core.channel.migs.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sonln
 */

@Getter
@Setter
public class ProcessAcsResultReq {
    private String apiOperation;
    private String paRes;
    @JsonProperty("3DSecureId")
    private String _3DSecureId;
}
