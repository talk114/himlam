package gateway.core.channel.dong_a_bank.dto.res;

public class GetBillInfoRes {
    private String orderID_DAB;

    private String orderID;

    private String custName;

    private String numberCard;

    private String amount;

    private String status;

    public void setOrderID_DAB(String orderID_DAB) {
        this.orderID_DAB = orderID_DAB;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
