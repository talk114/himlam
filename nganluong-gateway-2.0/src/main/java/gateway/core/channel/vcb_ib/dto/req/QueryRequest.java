package gateway.core.channel.vcb_ib.dto.req;

public class QueryRequest extends BaseRequest {
    private String queryTranId;

    public String getQueryTranId() {
        return queryTranId;
    }

    public void setQueryTranId(String queryTranId) {
        this.queryTranId = queryTranId;
    }
}
