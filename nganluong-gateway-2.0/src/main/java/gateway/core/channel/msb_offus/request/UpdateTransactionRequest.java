package gateway.core.channel.msb_offus.request;

import gateway.core.channel.msb_qr.dto.req.*;
import java.io.Serializable;

public class UpdateTransactionRequest extends BaseRequest implements Serializable {

    private String transId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}