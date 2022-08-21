package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class VAGetListVirtualAccountVIBRequest implements Serializable {

    private String page_num;
    private String page_size;

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
}
