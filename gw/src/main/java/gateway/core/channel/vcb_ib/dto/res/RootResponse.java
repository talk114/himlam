package gateway.core.channel.vcb_ib.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RootResponse {

    @JsonProperty(value = "PartnerId")
    private String PartnerId;
    @JsonProperty(value = "TransactionID")
    private String TransactionID;
    @JsonProperty(value = "Data")
    private String Data;
    @JsonProperty(value = "Signature")
    private String Signature;

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String partnerId) {
        PartnerId = partnerId;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
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
