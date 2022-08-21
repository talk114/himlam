package gateway.core.channel.msb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.msb_ecom.dto.MSBEcomConstant;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentRequest extends BaseRequest implements Serializable {

    // tham số tĩnh
    @JsonProperty(value = "currency")
    private String currency;

    @JsonProperty(value = "paymentType") // áp dụng paymentType = ATM
    private String paymentType;

    @JsonProperty(value = "cardName")
    private String cardName;

    @JsonProperty(value = "cardNumber")
    private String cardNumber;

    @JsonProperty(value = "releaseMonth")
    private long releaseMonth;

    @JsonProperty(value = "releaseYear")
    private long releaseYear;

    @JsonProperty(value = "merchantID")
    private String merchantID;

    // tham số thanh toán
    @JsonProperty(value = "merchTxnRef")
    private String merchTxnRef;

    @JsonProperty(value = "orderInfo")
    private String orderInfo;

    @JsonProperty(value = "amount")
    private String amount;

    //tham số chuỗi mã hóa
    @JsonProperty(value = "secureHash")
    private String secureHash;


    public CreatePaymentRequest() {

    }

    // build tham số chuỗi mã hóa
    public String buildDataRaw(String accessCode) {
        return accessCode + this.merchantID + this.merchTxnRef + this.orderInfo + this.amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(long releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public long getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(long releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getMerchTxnRef() {
        return merchTxnRef;
    }

    public void setMerchTxnRef(String merchTxnRef) {
        this.merchTxnRef = merchTxnRef;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSecureHash() {
        return secureHash;
    }

    public void setSecureHash(String secureHash) {
        this.secureHash = secureHash;
    }
}
