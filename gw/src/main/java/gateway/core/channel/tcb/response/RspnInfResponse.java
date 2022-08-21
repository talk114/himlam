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
public class RspnInfResponse implements Serializable {

    private String CreDtTm;
    private String TxId;
    private String Desc;
    private String Sgntr;
    private String Id;

    public RspnInfResponse(JSONObject json) {
        this.CreDtTm = json.get("v1:CreDtTm").toString();
        this.TxId = json.get("v1:TxId").toString();
        this.Desc = json.get("v1:Desc").toString();
        this.Sgntr = json.get("v1:Sgntr").toString();
        this.Id = json.get("v1:Id").toString();
    }

    public RspnInfResponse() {
    }

    public String getCreDtTm() {
        return CreDtTm;
    }

    public void setCreDtTm(String CreDtTm) {
        this.CreDtTm = CreDtTm;
    }

    public String getTxId() {
        return TxId;
    }

    public void setTxId(String TxId) {
        this.TxId = TxId;
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

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
}
