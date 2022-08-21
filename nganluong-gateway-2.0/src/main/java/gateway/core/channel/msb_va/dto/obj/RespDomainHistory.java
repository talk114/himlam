package gateway.core.channel.msb_va.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RespDomainHistory {
    private String page;
    private String max;
    private String total;
    private String userdata;
    private List<Transaction> rows;
    private String totalElements;
    private String totalPages;
}
