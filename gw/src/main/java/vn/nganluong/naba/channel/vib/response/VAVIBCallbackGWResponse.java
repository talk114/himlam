package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBCallbackGWResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6737228227385410967L;

    private String status_code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String seq_no;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String virtual_acct;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String actual_acct;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String tran_type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
