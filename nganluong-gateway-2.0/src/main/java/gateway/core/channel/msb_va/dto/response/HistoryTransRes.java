package gateway.core.channel.msb_va.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.msb_va.dto.obj.RespDomainHistory;
import gateway.core.channel.msb_va.dto.obj.RespMessage;
import lombok.Data;

/**
 * @author sonln
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryTransRes {
    private RespMessage respMessage;
    private RespDomainHistory respDomain;

}
