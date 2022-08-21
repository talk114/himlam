package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class EnableVirtualAccountVIBRequest implements Serializable {

    private String virtual_account_no;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVirtual_account_no() {
        return virtual_account_no;
    }

    public void setVirtual_account_no(String virtual_account_no) {
        this.virtual_account_no = virtual_account_no;
    }
}
