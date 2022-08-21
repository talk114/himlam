package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TokenResponse implements Serializable {

    /**
     *
     */

    private static final long serialVersionUID = 1L;
    private String acquirerCode;
    private String gatewayCode;

    public TokenResponse() {
    }

    public TokenResponse(String acquirerCode, String gatewayCode) {
        this.acquirerCode = acquirerCode;
        this.gatewayCode = gatewayCode;
    }

    public String getAcquirerCode() {
        return acquirerCode;
    }

    public void setAcquirerCode(String acquirerCode) {
        this.acquirerCode = acquirerCode;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }
}
