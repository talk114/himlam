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
public class FunTransferRspnStsResponse implements Serializable {
    
    private String AddtlStsRsnInf;
    private String Sts;

    public FunTransferRspnStsResponse(JSONObject json) {
        this.AddtlStsRsnInf = json.get("ns:AddtlStsRsnInf").toString();
        this.Sts = json.get("ns:Sts").toString();
    }

    public FunTransferRspnStsResponse() {
    }

    public String getAddtlStsRsnInf() {
        return AddtlStsRsnInf;
    }

    public void setAddtlStsRsnInf(String AddtlStsRsnInf) {
        this.AddtlStsRsnInf = AddtlStsRsnInf;
    }

    public String getSts() {
        return Sts;
    }

    public void setSts(String Sts) {
        this.Sts = Sts;
    }
}
