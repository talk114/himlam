package gateway.core.channel.cybersouce.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class JwtInput implements Serializable {
    private String jti;
    private Order orderObject;
    private String confirmUrl;
//    private String referenceId;
//    private Boolean objectifyPayload;
}
