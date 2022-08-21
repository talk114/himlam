package gateway.core.channel.vcb_ib.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryResponse extends BaseResponse {

    @JsonProperty(value = "balance")
    private String balance;
    @JsonProperty(value = "TranStatus")
    private String TranStatus;
    @JsonProperty(value = "queryTranID")
    private String queryTranID;
    @JsonProperty(value = "VCBTranID")
    private String VCBTranID;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTranStatus() {
        return TranStatus;
    }

    public void setTranStatus(String tranStatus) {
        TranStatus = tranStatus;
    }

    public String getQueryTranID() {
        return queryTranID;
    }

    public void setQueryTranID(String queryTranID) {
        this.queryTranID = queryTranID;
    }

    @Override
    public String getVCBTranID() {
        return VCBTranID;
    }

    @Override
    public void setVCBTranID(String VCBTranID) {
        this.VCBTranID = VCBTranID;
    }
}
