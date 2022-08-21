package gateway.core.channel.mb_qrcode.dto.object;

import java.util.ArrayList;
import java.util.List;

public class NganLuongDSResponse {

    private String error_code;
    private String error_description;
    private List<DataDS> data;

    public NganLuongDSResponse() {
    }

    public NganLuongDSResponse(String error_code, String error_description) {
        this.error_code = error_code;
        this.error_description = error_description;
        this.data = new ArrayList<>();
    }

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

    public List<DataDS> getData() {
        return data;
    }

    public void setData(List<DataDS> data) {
        this.data = data;
    }
}
