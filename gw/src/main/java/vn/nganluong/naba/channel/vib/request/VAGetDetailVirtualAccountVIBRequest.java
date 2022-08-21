package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class VAGetDetailVirtualAccountVIBRequest implements Serializable {

    private String virtual_acct;

    public String getVirtual_acct() {
        return virtual_acct;
    }

    public void setVirtual_acct(String virtual_acct) {
        this.virtual_acct = virtual_acct;
    }
}
