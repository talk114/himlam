package gateway.core.channel.msb_ecom.dto.res;

public class RootResponse {

    private String bodyRes;

    private int httpCode;

    public String getBodyRes() {
        return bodyRes;
    }

    public void setBodyRes(String bodyRes) {
        this.bodyRes = bodyRes;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
