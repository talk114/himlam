package gateway.core.channel.mb_qrcode.dto.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDS {

    private String amount;
    private String nl_trans_id;
    private String mb_trans_id;
    private String bank_trans_id;
    private String time_created;
    private String time_paid;

    public DataDS() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNl_trans_id() {
        return nl_trans_id;
    }

    public void setNl_trans_id(String nl_trans_id) {
        this.nl_trans_id = nl_trans_id;
    }

    public String getMb_trans_id() {
        return mb_trans_id;
    }

    public void setMb_trans_id(String mb_trans_id) {
        this.mb_trans_id = mb_trans_id;
    }

    public String getBank_trans_id() {
        return bank_trans_id;
    }

    public void setBank_trans_id(String bank_trans_id) {
        this.bank_trans_id = bank_trans_id;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public String getTime_paid() {
        return time_paid;
    }

    public void setTime_paid(String time_paid) {
        this.time_paid = time_paid;
    }
}
