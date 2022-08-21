/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.response;

import java.util.List;

/**
 *
 * @author taind
 */
public class InqListBankInfoResponse {

    private RspnInfResponse infResponse;
    private RspnStsResponse stsResponse;
    private List<BkInfRcrdResponse> lsDatas;

    public InqListBankInfoResponse(RspnInfResponse infResponse, RspnStsResponse stsResponse, List<BkInfRcrdResponse> lsDatas) {
        this.infResponse = infResponse;
        this.stsResponse = stsResponse;
        this.lsDatas = lsDatas;
    }

    public InqListBankInfoResponse() {
    }

    public RspnInfResponse getInfResponse() {
        return infResponse;
    }

    public void setInfResponse(RspnInfResponse infResponse) {
        this.infResponse = infResponse;
    }

    public RspnStsResponse getStsResponse() {
        return stsResponse;
    }

    public void setStsResponse(RspnStsResponse stsResponse) {
        this.stsResponse = stsResponse;
    }

    public List<BkInfRcrdResponse> getLsDatas() {
        return lsDatas;
    }

    public void setLsDatas(List<BkInfRcrdResponse> lsDatas) {
        this.lsDatas = lsDatas;
    }
}
