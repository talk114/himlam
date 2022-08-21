package gateway.core.channel.bidv.dto.req;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vinhnt
 *	<b>Parse request confirm link card from BIDV</b>
 *
 */
@XmlRootElement(name = "ConfirmLinkCardReq")
public class BIDVConfirmLinkCardReq {

    private String bankCode;
    private String bankName;
    private String serviceId;
    private String merchantId;
    private String bankTransId;
    private String transactionId;
    private String resultCode;
    private String secureCode;
    private String userId;
    private String bankAccNumber;

    public String getBankCode() {
        return bankCode;
    }

    @XmlElement(name = "Bank_Code")
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    @XmlElement(name = "Bank_Name")
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getServiceId() {
        return serviceId;
    }

    @XmlElement(name = "Service_Id")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    @XmlElement(name = "Merchant_Id")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getBankTransId() {
        return bankTransId;
    }

    @XmlElement(name = "Bank_trans_id")
    public void setBankTransId(String bankTransId) {
        this.bankTransId = bankTransId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @XmlElement(name = "Trans_Id")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResultCode() {
        return resultCode;
    }

    @XmlElement(name = "Result_Code")
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSecureCode() {
        return secureCode;
    }

    @XmlElement(name = "Secure_Code")
    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getUserId() {
        return userId;
    }

    @XmlElement(name = "Payer_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankAccNumber() {
        return bankAccNumber;
    }

    @XmlElement(name = "Account_Number")
    public void setBankAccNumber(String bankAccNumber) {
        this.bankAccNumber = bankAccNumber;
    }

    public String dataBeforeSignature(){
        return bankCode + "|" + bankName + "|" + serviceId + "|" + merchantId + "|" + transactionId + "|" + resultCode + "|" + bankTransId;
    }

}
