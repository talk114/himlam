package gateway.core.channel.vcb_ib.dto.req;

public class RefundRequest extends BaseRequest {
    private String RefundSources;
    private String RefundTranID;

    public String getRefundSources() {
        return RefundSources;
    }

    public void setRefundSources(String refundSources) {
        RefundSources = refundSources;
    }

    public String getRefundTranID() {
        return RefundTranID;
    }

    public void setRefundTranID(String refundTranID) {
        RefundTranID = refundTranID;
    }
}
