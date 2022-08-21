package gateway.core.channel.bidv.bidv_transfer_247.object247;

import java.util.List;

public class NganLuongDSResponse {

    private String error_code;
    private String error_description;
    private List<TransInfoDS> data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public List<TransInfoDS> getData() {
        return data;
    }

    public void setData(List<TransInfoDS> data) {
        this.data = data;
    }
}
