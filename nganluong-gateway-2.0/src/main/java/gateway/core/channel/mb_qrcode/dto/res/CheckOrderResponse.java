package gateway.core.channel.mb_qrcode.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CheckOrderResponse extends BaseResponse implements Serializable {

    @JsonProperty(value = "traceTransfer")
    private String traceTransfer;
    @JsonProperty(value = "storeLabel")
    private String storeLabel;
    @JsonProperty(value = "terminalLabel")
    private String terminalLabel;
    @JsonProperty(value = "billNumber")
    private List<String> billNumber;
    @JsonProperty(value = "debitAmount")
    private String debitAmount;
    @JsonProperty(value = "realAmount")
    private String realAmount;
    @JsonProperty(value = "payDate")
    private String payDate;
    @JsonProperty(value = "respCode")
    private String respCode;
    @JsonProperty(value = "respDesc")
    private String respDesc;
    @JsonProperty(value = "checkSum")
    private String checkSum;
    @JsonProperty(value = "rate")
    private String rate;

    public String getTraceTransfer() {
        return traceTransfer;
    }

    public void setTraceTransfer(String traceTransfer) {
        this.traceTransfer = traceTransfer;
    }

    public String getStoreLabel() {
        return storeLabel;
    }

    public void setStoreLabel(String storeLabel) {
        this.storeLabel = storeLabel;
    }

    public String getTerminalLabel() {
        return terminalLabel;
    }

    public void setTerminalLabel(String terminalLabel) {
        this.terminalLabel = terminalLabel;
    }

    public List<String> getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(List<String> billNumber) {
        this.billNumber = billNumber;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
