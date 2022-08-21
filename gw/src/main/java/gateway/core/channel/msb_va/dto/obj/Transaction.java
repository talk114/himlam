package gateway.core.channel.msb_va.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Transaction {
    private String id;
    private String vaCode;
    private String vaNumber;
    private String fromAccountName;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String toAccountName;
    private String tranAmount;
    private String tranRemark;
    private String tranDate;
}
