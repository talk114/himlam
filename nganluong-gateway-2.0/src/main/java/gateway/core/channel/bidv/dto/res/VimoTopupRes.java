package gateway.core.channel.bidv.dto.res;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 * @author vinhnt <b>Response GET_INFO, TOPUP call Api VIMO</b>
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VimoTopupRes implements Serializable {

    @JsonProperty(value = "error_code")
    private String errorCode;
    private String message;
    private VimoDataTopupRes data;

    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public VimoDataTopupRes getData() {
        return data;
    }
    public void setData(VimoDataTopupRes data) {
        this.data = data;
    }



}
