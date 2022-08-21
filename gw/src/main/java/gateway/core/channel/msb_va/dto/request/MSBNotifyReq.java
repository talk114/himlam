package gateway.core.channel.msb_va.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MSBNotifyReq {
    private String tranSeq;
    private String vaCode;
    private String vaNumber;
    private String fromAccountName;
    private String fromAccountNumber;
    private String toAccountName;
    private String toAccountNumber;
    private String tranAmount;
    private String tranRemark;
    private String tranDate;
    private String signature;
}
