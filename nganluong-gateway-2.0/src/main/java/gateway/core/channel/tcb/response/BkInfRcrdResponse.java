/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.response;

/**
 *
 * @author taind
 */
public class BkInfRcrdResponse {

    private String CITAD;
    private String BkCd;
    private String BkNm;
    private String BrnchNm;
    private String StatPrvc;
    private String ActvSts;
    private String IsCentralizedBank;

    public String getCITAD() {
        return CITAD;
    }

    public void setCITAD(String CITAD) {
        this.CITAD = CITAD;
    }

    public String getBkCd() {
        return BkCd;
    }

    public void setBkCd(String BkCd) {
        this.BkCd = BkCd;
    }

    public String getBkNm() {
        return BkNm;
    }

    public void setBkNm(String BkNm) {
        this.BkNm = BkNm;
    }

    public String getBrnchNm() {
        return BrnchNm;
    }

    public void setBrnchNm(String BrnchNm) {
        this.BrnchNm = BrnchNm;
    }

    public String getStatPrvc() {
        return StatPrvc;
    }

    public void setStatPrvc(String StatPrvc) {
        this.StatPrvc = StatPrvc;
    }

    public String getActvSts() {
        return ActvSts;
    }

    public void setActvSts(String ActvSts) {
        this.ActvSts = ActvSts;
    }

    public String getIsCentralizedBank() {
        return IsCentralizedBank;
    }

    public void setIsCentralizedBank(String IsCentralizedBank) {
        this.IsCentralizedBank = IsCentralizedBank;
    }
}
