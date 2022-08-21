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
public class FunTransferRspnInfResponse implements Serializable {
    
    private String Id;
    private String TxId;
    private String CreDtTm;
    private String Desc;
    private String Sgntr;

    public FunTransferRspnInfResponse(JSONObject json) {
        this.Id = json.get("ns:Id").toString();
        this.TxId = json.get("ns:TxId").toString();
        this.CreDtTm = json.get("ns:CreDtTm").toString();
        this.Desc = json.get("ns:Desc").toString();
        this.Sgntr = json.get("ns:Sgntr").toString();
    }

    public FunTransferRspnInfResponse() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getTxId() {
        return TxId;
    }

    public void setTxId(String TxId) {
        this.TxId = TxId;
    }

    public String getCreDtTm() {
        return CreDtTm;
    }

    public void setCreDtTm(String CreDtTm) {
        this.CreDtTm = CreDtTm;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public String getSgntr() {
        return Sgntr;
    }

    public void setSgntr(String Sgntr) {
        this.Sgntr = Sgntr;
    }
}
