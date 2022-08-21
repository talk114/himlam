package gateway.core.channel.vcb_ib.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VcbCallbackRequest {
    @JsonProperty(value = "PartnerTranId")
    private String PartnerTranId;
    @JsonProperty(value = "Data")
    private String Data;
    @JsonProperty(value = "Signature")
    private String Signature;

    public String getPartnerTranId() {
        return PartnerTranId;
    }

    public void setPartnerTranId(String partnerTranId) {
        PartnerTranId = partnerTranId;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }
}
