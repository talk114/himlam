package gateway.core.channel.msb_offus.request;

import gateway.core.channel.msb_qr.dto.req.*;
import java.io.Serializable;

public class CashinRequest extends BaseRequest implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
