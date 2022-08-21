/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.response;

import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author taind
 */
public class FunTransferTxInfResponse implements Serializable {
    
    private String RefId;
    private String FeeAmt;

    public FunTransferTxInfResponse(JSONObject json) {
        this.RefId = json.get("ns:RefId").toString();
        this.FeeAmt = json.get("ns:FeeAmt").toString();
    }

    public FunTransferTxInfResponse() {
    }

    public String getRefId() {
        return RefId;
    }

    public void setRefId(String RefId) {
        this.RefId = RefId;
    }

    public String getFeeAmt() {
        return FeeAmt;
    }

    public void setFeeAmt(String FeeAmt) {
        this.FeeAmt = FeeAmt;
    }
}
