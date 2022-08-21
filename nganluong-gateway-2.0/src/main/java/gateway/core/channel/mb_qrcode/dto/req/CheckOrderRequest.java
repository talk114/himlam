package gateway.core.channel.mb_qrcode.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant;
import gateway.core.util.PGSecurity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CheckOrderRequest extends BaseRequest implements Serializable {

    @JsonProperty(value = "traceTransfer")
    private String traceTransfer;
    @JsonProperty(value = "billNumber")
    private String billNumber;
    @JsonProperty(value = "consumerLabelTerm")
    private String consumerLabelTerm;
    @JsonProperty(value = "referenceLabel")
    private String referenceLabel;
    @JsonProperty(value = "checkSum")
    private String checkSum;

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getConsumerLabelTerm() {
        return consumerLabelTerm;
    }

    public void setConsumerLabelTerm(String consumerLabelTerm) {
        this.consumerLabelTerm = consumerLabelTerm;
    }

    public String getReferenceLabel() {
        return referenceLabel;
    }

    public void setReferenceLabel(String referenceLabel) {
        this.referenceLabel = referenceLabel;
    }

    public String getTraceTransfer() {
        return traceTransfer;
    }

    public void setTraceTransfer(String traceTransfer) {
        this.traceTransfer = traceTransfer;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public void buildDataChecksum() throws NoSuchAlgorithmException {
        StringBuilder dataRaw = new StringBuilder();
        if (!Strings.isNullOrEmpty(traceTransfer)) {
            dataRaw.append(traceTransfer);
        }
        if (!Strings.isNullOrEmpty(billNumber)) {
            dataRaw.append(billNumber);
        }
        if (!Strings.isNullOrEmpty(consumerLabelTerm)) {
            dataRaw.append(consumerLabelTerm);
        }
        if (!Strings.isNullOrEmpty(referenceLabel)) {
            dataRaw.append(referenceLabel);
        }
        dataRaw.append(MBBankVNPayOffUsConstant.MERCHANT_SHORT_NAME);
        dataRaw.append(MBBankVNPayOffUsConstant.ACCESS_KEY);
        this.setCheckSum(PGSecurity.md5(dataRaw.toString()));
    }
}
