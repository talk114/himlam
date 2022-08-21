package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBGetDetailVirtualAccountVIBResponse implements Serializable {

    @JsonProperty("Result")
    private VAVIBGetDetailVirtualAccountResultVIBResponse result;

    public VAVIBGetDetailVirtualAccountResultVIBResponse getResult() {
        return result;
    }

    public void setResult(VAVIBGetDetailVirtualAccountResultVIBResponse result) {
        this.result = result;
    }
}
