package gateway.core.channel.mb_qrcode.dto.res;

import java.io.Serializable;

public class NganLuongResponse implements Serializable {

    private String resCode;
    private String resDesc;

    public NganLuongResponse(String resCode, String resDesc) {
        this.resCode = resCode;
        this.resDesc = resDesc;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }
}
