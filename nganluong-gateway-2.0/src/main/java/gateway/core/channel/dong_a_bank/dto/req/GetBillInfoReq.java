package gateway.core.channel.dong_a_bank.dto.req;

public class GetBillInfoReq {
    private int pos;

    private int method;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }
}
