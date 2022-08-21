package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

// @JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBCallbackRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5621054491501428915L;

    //	@JsonProperty(value = "SEQ_NO")
    private String seq_no;

    private String virtual_acct;

    private String actual_acct;

    private String tran_amt;

    private String ccy;

    private String tran_date_time;

    private String narrative;

    private String tran_type;

    private String trace_id;

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getVirtual_acct() {
        return virtual_acct;
    }

    public void setVirtual_acct(String virtual_acct) {
        this.virtual_acct = virtual_acct;
    }

    public String getActual_acct() {
        return actual_acct;
    }

    public void setActual_acct(String actual_acct) {
        this.actual_acct = actual_acct;
    }

    public String getTran_amt() {
        return tran_amt;
    }

    public void setTran_amt(String tran_amt) {
        this.tran_amt = tran_amt;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getTran_date_time() {
        return tran_date_time;
    }

    public void setTran_date_time(String tran_date_time) {
        this.tran_date_time = tran_date_time;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getTran_type() {
        return tran_type;
    }

    public void setTran_type(String tran_type) {
        this.tran_type = tran_type;
    }

    public String getTrace_id() {
        return trace_id;
    }

    public void setTrace_id(String trace_id) {
        this.trace_id = trace_id;
    }
}
