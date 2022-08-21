package gateway.core.channel.mb_qrcode.dto.req;

import java.io.Serializable;

public class TerminalListTidRequest extends BaseRequest implements Serializable {

    private int page; // Số trang cần lấy, nếu không truyền sang thì mặc định là 1
    private int size; // số lượng bản ghi trong trang, nếu không truyền sang thì mặc định là 10
    private String keyword; //tìm kiếm theo tên của Doanh nghiệp

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
