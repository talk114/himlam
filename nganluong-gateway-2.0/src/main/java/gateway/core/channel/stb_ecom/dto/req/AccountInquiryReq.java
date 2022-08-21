package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AccountInquiryReq implements Serializable {

    @JsonProperty("ToSTBAccountNo")
    private String to_stbAccountNo;

    @JsonProperty("RefNumber")
    private String refNumber;

    public String getTo_stbAccountNo() {
        return to_stbAccountNo;
    }

    public void setTo_stbAccountNo(String to_stbAccountNo) {
        this.to_stbAccountNo = to_stbAccountNo;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }
}
