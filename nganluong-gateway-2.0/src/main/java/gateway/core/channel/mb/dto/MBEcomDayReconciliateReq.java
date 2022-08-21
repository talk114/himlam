package gateway.core.channel.mb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MBEcomDayReconciliateReq implements Serializable {

    private String from_date;
    private String to_date;

    public MBEcomDayReconciliateReq(String from_date, String to_date) {
        this.from_date = from_date;
        this.to_date = to_date;
    }

    public MBEcomDayReconciliateReq() {
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }
}
