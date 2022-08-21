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
public class RspnStsResponse implements Serializable {

    private String Sts;
    private String AddtlStsRsnInf;

    public RspnStsResponse(JSONObject json) {
        this.Sts = json.get("v1:Sts").toString();
        this.AddtlStsRsnInf = json.get("v1:AddtlStsRsnInf").toString();
    }

    public RspnStsResponse() {
    }

    public String getSts() {
        return Sts;
    }

    public void setSts(String Sts) {
        this.Sts = Sts;
    }

    public String getAddtlStsRsnInf() {
        return AddtlStsRsnInf;
    }

    public void setAddtlStsRsnInf(String AddtlStsRsnInf) {
        this.AddtlStsRsnInf = AddtlStsRsnInf;
    }
}
