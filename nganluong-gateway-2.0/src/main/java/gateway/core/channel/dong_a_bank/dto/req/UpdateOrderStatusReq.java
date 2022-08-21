package gateway.core.channel.dong_a_bank.dto.req;

public class UpdateOrderStatusReq {

    private String orderID;

    private String status;

    private long change;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getChange() {
        return change;
    }

    public void setChange(long change) {
        this.change = change;
    }
}
