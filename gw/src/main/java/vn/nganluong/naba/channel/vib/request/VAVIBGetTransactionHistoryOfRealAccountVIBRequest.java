package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class VAVIBGetTransactionHistoryOfRealAccountVIBRequest implements Serializable {


    private String page_num;
    private String page_size;

    private String from_date;
    private String to_date;
    private String from_amt;
    private String to_amt;
    private String c_d;
    private String actual_acct;

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
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

    public String getFrom_amt() {
        return from_amt;
    }

    public void setFrom_amt(String from_amt) {
        this.from_amt = from_amt;
    }

    public String getTo_amt() {
        return to_amt;
    }

    public void setTo_amt(String to_amt) {
        this.to_amt = to_amt;
    }

    public String getC_d() {
        return c_d;
    }

    public void setC_d(String c_d) {
        this.c_d = c_d;
    }

    public String getActual_acct() {
        return actual_acct;
    }

    public void setActual_acct(String actual_acct) {
        this.actual_acct = actual_acct;
    }
}
