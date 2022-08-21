package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sonln
 */
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Secure {
    private String acsEci;
    private Object authenticationRedirect;
    private String authenticationToken;
    private String paResStatus;
    private String veResEnrolled;
    private String xid;
}
