package gateway.core.channel.mb_qrcode.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreateQrCodeResponse extends BaseResponse implements Serializable {

    @JsonProperty(value = "qrcode")
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
