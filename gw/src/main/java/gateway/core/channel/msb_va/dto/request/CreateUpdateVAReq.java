package gateway.core.channel.msb_va.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.msb_va.dto.MSBVAContants;
import gateway.core.channel.msb_va.dto.obj.ReqAccountVA;
import lombok.Data;

import java.util.List;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreateUpdateVAReq {
    private final String serviceCode = MSBVAContants.SERVICE_CODE;
    private final String mId = MSBVAContants.MID;
    private final String tId = MSBVAContants.TID;
    private String tokenKey;
    private List<ReqAccountVA> rows;

    public String getmId() {
        return mId;
    }

    public String gettId() {
        return tId;
    }
}
