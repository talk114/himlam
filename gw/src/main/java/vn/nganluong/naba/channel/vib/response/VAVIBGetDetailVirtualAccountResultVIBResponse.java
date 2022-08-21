package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBGetDetailVirtualAccountResultVIBResponse implements Serializable {

    @JsonProperty("STATUSCODE")
    private String status_code;

    @JsonProperty("DATA")
    private VAVIBVirtualAccountDetailVIBResponse data;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public VAVIBVirtualAccountDetailVIBResponse getData() {
        return data;
    }

    public void setData(VAVIBVirtualAccountDetailVIBResponse data) {
        this.data = data;
    }
}
