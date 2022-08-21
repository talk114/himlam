package gateway.core.channel.vcb_ib.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VCBCallbackResponse extends BaseResponse {

    @JsonProperty(value = "PartnerId")
    private String PartnerId;
    @JsonProperty(value = "amount")
    private String amount;
    @JsonProperty(value = "trans_id")
    private String nglTransId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNglTransId() {
        return nglTransId;
    }

    public void setNglTransId(String nglTransId) {
        this.nglTransId = nglTransId;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String partnerId) {
        PartnerId = partnerId;
    }
}
