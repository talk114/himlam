package gateway.core.channel.mb_qrcode.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateQRCodeRequest extends BaseRequest implements Serializable {

    @JsonProperty(value = "terminalID")
    private String terminalID;

    @JsonProperty(value = "qrcodeType")
    private int qrcodeType;

    @JsonProperty(value = "initMethod")
    private int initMethod;

    @JsonProperty(value = "transactionAmount")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double transactionAmount;

    @JsonProperty(value = "billNumber")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String billNumber;

    @JsonProperty(value = "additionalAddress")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int additionalAddress;

    @JsonProperty(value = "additionalMobile")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int additionalMobile;

    @JsonProperty(value = "additionalEmail")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int additionalEmail;

    @JsonProperty(value = "consumerLabel")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String consumerLabel;

    @JsonProperty(value = "term")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String term;

    @JsonProperty(value = "referenceLabelTime")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String referenceLabelTime;

    @JsonProperty(value = "referenceLabelCode")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String referenceLabelCode;

    @JsonProperty(value = "trasactionPurpose")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String transactionPurpose;

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public int getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(int qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public int getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(int initMethod) {
        this.initMethod = initMethod;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public int getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(int additionalAddress) {
        this.additionalAddress = additionalAddress;
    }

    public int getAdditionalMobile() {
        return additionalMobile;
    }

    public void setAdditionalMobile(int additionalMobile) {
        this.additionalMobile = additionalMobile;
    }

    public int getAdditionalEmail() {
        return additionalEmail;
    }

    public void setAdditionalEmail(int additionalEmail) {
        this.additionalEmail = additionalEmail;
    }

    public String getConsumerLabel() {
        return consumerLabel;
    }

    public void setConsumerLabel(String consumerLabel) {
        this.consumerLabel = consumerLabel;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getReferenceLabelTime() {
        return referenceLabelTime;
    }

    public void setReferenceLabelTime(String referenceLabelTime) {
        this.referenceLabelTime = referenceLabelTime;
    }

    public String getReferenceLabelCode() {
        return referenceLabelCode;
    }

    public void setReferenceLabelCode(String referenceLabelCode) {
        this.referenceLabelCode = referenceLabelCode;
    }

    public String getTransactionPurpose() {
        return transactionPurpose;
    }

    public void setTransactionPurpose(String transactionPurpose) {
        this.transactionPurpose = transactionPurpose;
    }
}
