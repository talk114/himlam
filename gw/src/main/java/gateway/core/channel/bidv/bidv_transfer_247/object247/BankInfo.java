package gateway.core.channel.bidv.bidv_transfer_247.object247;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankInfo {

    private String bankCode;
    private String bankNameVN;
    private String bankNameENG;
    private String binCode;

    public BankInfo() {
    }

    public BankInfo(String bankCode, String bankNameVN, String bankNameENG, String binCode) {
        this.bankCode = bankCode;
        this.bankNameVN = bankNameVN;
        this.bankNameENG = bankNameENG;
        this.binCode = binCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankNameVN() {
        return bankNameVN;
    }

    public void setBankNameVN(String bankNameVN) {
        this.bankNameVN = bankNameVN;
    }

    public String getBankNameENG() {
        return bankNameENG;
    }

    public void setBankNameENG(String bankNameENG) {
        this.bankNameENG = bankNameENG;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }
}
