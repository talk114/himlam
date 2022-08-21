package gateway.core.channel.msb_va.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.msb_va.dto.MSBVAContants;
import lombok.Data;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HistoryTransReq {
    private String fromDate;
    private String toDate;
    private final String vaCode = MSBVAContants.VA_CODE;
    private String vaNumber;
    private String page;
    private String rows;
    private final String mId = MSBVAContants.MID;
    private final String tId = MSBVAContants.TID;
    private String tokenKey;

    public String getmId() {
        return mId;
    }

    public String gettId() {
        return tId;
    }
}
