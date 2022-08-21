package gateway.core.channel.mb_qrcode.dto.res;

import java.io.Serializable;

public class RootResponse implements Serializable {

    private String status;
    private String error;
    private String soaErrorCode;
    private String soaErrorDesc;
    private String clientMessageId;
    private String path;
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSoaErrorCode() {
        return soaErrorCode;
    }

    public void setSoaErrorCode(String soaErrorCode) {
        this.soaErrorCode = soaErrorCode;
    }

    public String getSoaErrorDesc() {
        return soaErrorDesc;
    }

    public void setSoaErrorDesc(String soaErrorDesc) {
        this.soaErrorDesc = soaErrorDesc;
    }

    public String getClientMessageId() {
        return clientMessageId;
    }

    public void setClientMessageId(String clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
