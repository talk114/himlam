package gateway.core.channel.msb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonRootName(value = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RootRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "currency_code")
    protected String currencyCode;

//    @JsonProperty(value = "payment_type") // ATM
//    private String paymentType;

    @JsonProperty(value = "card_name")
    private String cardName;

    @JsonProperty(value = "card_number")
    protected String cardNumber;

    @JsonProperty(value = "release_month")
    protected double releaseMonth;

    @JsonProperty(value = "release_year")
    protected double releaseYear;

//    @JsonProperty(value = "merchant_id")
//    protected String merchantId;

    @JsonProperty(value = "trans_id") //Mã giao dịch của merchant
    protected String transId;

    @JsonProperty(value = "request_trans_id") //Mã giao dịch MSB trả về
    protected String requestTransId;

    @JsonProperty(value = "order_info")
    protected String orderInfo;

    @JsonProperty(value = "amount")
    protected double amount;

    @JsonProperty(value = "otp")
    protected String otp;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public double getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(double releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public double getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(double releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRequestTransId() {
        return requestTransId;
    }

    public void setRequestTransId(String requestTransId) {
        this.requestTransId = requestTransId;
    }
}
