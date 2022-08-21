package gateway.core.channel.msb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryTransactionRequest extends BaseRequest implements Serializable {

    @JsonProperty(value = "merchantId")
    private String merchantId;

    @JsonProperty(value = "transId")
    private String transId;

    @JsonProperty(value = "secureHash")
    private String secureHash;

    // build tham số chuỗi mã hóa
    public String buildDataRaw(String accessCode) {
        return accessCode + this.merchantId + this.transId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getSecureHash() {
        return secureHash;
    }

    public void setSecureHash(String secureHash) {
        this.secureHash = secureHash;
    }
}
