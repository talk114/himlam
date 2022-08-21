/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.request;

import gateway.core.channel.msb_qr.dto.req.BaseRequest;
import java.io.Serializable;

/**
 *
 * @author taind
 */
public class InqListBankInfoRequest extends BaseRequest implements Serializable {
    
    private String transId;
    private long date;
    private String description;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
