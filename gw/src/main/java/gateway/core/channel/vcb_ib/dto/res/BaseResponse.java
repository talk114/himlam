package gateway.core.channel.vcb_ib.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse {

    @JsonProperty(value = "errCode")
    protected String errCode;
    @JsonProperty(value = "errDesc")
    protected String errDesc;
    @JsonProperty(value = "VcbTranId")
    protected String VCBTranID;

    public String getVCBTranID() {
        return VCBTranID;
    }

    public void setVCBTranID(String VCBTranID) {
        this.VCBTranID = VCBTranID;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
