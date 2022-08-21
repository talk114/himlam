package gateway.core.channel.napas.dto.obj;

/**
 * @author sonln
 */
public class Acquirer2 {

    private String batch;
    private String date;
    private String id;
    private String napas_trans_id;
    private String merchantId;

    public String getNapas_trans_id() {
        return napas_trans_id;
    }

    public void setNapas_trans_id(String napas_trans_id) {
        this.napas_trans_id = napas_trans_id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
