package gateway.core.channel.bidv.dto.req;


import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author vinhnt <b>Param confirm link card call Api VIMO<b/>
 *
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VimoConfirmLinkCardReq implements Serializable {

    @JsonProperty(value = "bank_code")
    private String bankCode;

    @JsonProperty(value = "bank_name")
    private String bankName;

    @JsonProperty(value = "bank_trans_id")
    private String bankTransId;

    @JsonProperty(value = "transaction_id")
    private String transactionId;

    @JsonProperty(value = "response_code")
    private String resultCode;

    private String description;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "bank_acc_no")
    private String bankAccNumber;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankTransId() {
        return bankTransId;
    }

    public void setBankTransId(String bankTransId) {
        this.bankTransId = bankTransId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankAccNumber() {
        return bankAccNumber;
    }

    public void setBankAccNumber(String bankAccNumber) {
        this.bankAccNumber = bankAccNumber;
    }

}
