package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBGetListVirtualAccountGWResponse implements Serializable {

    private String total_record;

    @JsonProperty("list_virtual_account ")
    private List<VAVIBVirtualAccountDetailVIBResponse> data;

    public List<VAVIBVirtualAccountDetailVIBResponse> getData() {
        return data;
    }

    public void setData(List<VAVIBVirtualAccountDetailVIBResponse> data) {
        this.data = data;
    }

    public String getTotal_record() {
        return total_record;
    }

    public void setTotal_record(String total_record) {
        this.total_record = total_record;
    }
}
