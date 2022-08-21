package gateway.core.channel.msb_offus.response;

import gateway.core.channel.msb_qr.dto.req.*;
import java.io.Serializable;

public class CashinResponse extends BaseRequest implements Serializable {

    private long id;
    private String cashinId;
    private String transId;

    public CashinResponse() {
    }

    public CashinResponse(long id, String cashinId, String transId) {
        this.id = id;
        this.cashinId = cashinId;
        this.transId = transId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCashinId() {
        return cashinId;
    }

    public void setCashinId(String cashinId) {
        this.cashinId = cashinId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
