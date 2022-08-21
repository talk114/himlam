package gateway.core.channel.msb_va.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.msb_va.dto.obj.RespDomainLogin;
import gateway.core.channel.msb_va.dto.obj.RespMessage;
import lombok.Data;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginRes {
    private RespMessage respMessage;
    private RespDomainLogin respDomain;
}
