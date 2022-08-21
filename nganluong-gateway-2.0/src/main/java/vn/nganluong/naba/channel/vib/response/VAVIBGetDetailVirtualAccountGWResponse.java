package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBGetDetailVirtualAccountGWResponse implements Serializable {

    @JsonProperty("virtual_account ")
    private VAVIBVirtualAccountDetailVIBResponse data;

    public VAVIBVirtualAccountDetailVIBResponse getData() {
        return data;
    }

    public void setData(VAVIBVirtualAccountDetailVIBResponse data) {
        this.data = data;
    }
}
