package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class DeleteVirtualAccountVIBRequest implements Serializable {

    private String virtual_account_no;

    public String getVirtual_account_no() {
        return virtual_account_no;
    }

    public void setVirtual_account_no(String virtual_account_no) {
        this.virtual_account_no = virtual_account_no;
    }
}
