package gateway.core.channel.napas.doi_soat;

public class DataNglDoiSoat {

    private String amount;
    private String napasTransId;
    private String nglTransId;
    private String createTime;
    private String transState;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNapasTransId() {
        return napasTransId;
    }

    public void setNapasTransId(String napasTransId) {
        this.napasTransId = napasTransId;
    }

    public String getNglTransId() {
        return nglTransId;
    }

    public void setNglTransId(String nglTransId) {
        this.nglTransId = nglTransId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
    }
}
