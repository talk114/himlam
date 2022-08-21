/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.response;

import java.io.Serializable;

/**
 *
 * @author taind
 */
public class FunTransferResponse implements Serializable {
    
    private FunTransferRspnStsResponse rspnSts;
    private FunTransferTxInfResponse txInf;
    private FunTransferRspnInfResponse rspnInf;

    public FunTransferResponse() {
    }

    public FunTransferResponse(FunTransferRspnStsResponse rspnSts, FunTransferTxInfResponse txInf, FunTransferRspnInfResponse rspnInf) {
        this.rspnSts = rspnSts;
        this.txInf = txInf;
        this.rspnInf = rspnInf;
    }

    public FunTransferRspnStsResponse getRspnSts() {
        return rspnSts;
    }

    public void setRspnSts(FunTransferRspnStsResponse rspnSts) {
        this.rspnSts = rspnSts;
    }

    public FunTransferTxInfResponse getTxInf() {
        return txInf;
    }

    public void setTxInf(FunTransferTxInfResponse txInf) {
        this.txInf = txInf;
    }

    public FunTransferRspnInfResponse getRspnInf() {
        return rspnInf;
    }

    public void setRspnInf(FunTransferRspnInfResponse rspnInf) {
        this.rspnInf = rspnInf;
    }
}
