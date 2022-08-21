package gateway.core.channel.mb_qrcode.dto.req;

import java.io.Serializable;
import java.util.List;

public class MBBankCallBack implements Serializable {

    private String traceTransfer;
    private String storeLabel;
    private String terminalLabel;
    private String billNumber;
    private String debitAmount;
    private String realAmount;
    private String payDate;
    private String respCode;
    private String respDesc;
    private String checkSum;
    private String rate;
    private String consumerLabelTerm;
    private List<String> referenceLabel;
    private String userName; // ten day du cua khach hang
    private String ftCode; ///Trả về mã FT nếu giao dịch thành, công ( phục vụ đối soát sau này)

    public List<String> getReferenceLabel() {
        return referenceLabel;
    }

    public void setReferenceLabel(List<String> referenceLabel) {
        this.referenceLabel = referenceLabel;
    }

    public String getFtCode() {
        return ftCode;
    }

    public void setFtCode(String ftCode) {
        this.ftCode = ftCode;
    }

    public String getConsumerLabelTerm() {
        return consumerLabelTerm;
    }

    public void setConsumerLabelTerm(String consumerLabelTerm) {
        this.consumerLabelTerm = consumerLabelTerm;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
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
