package gateway.core.channel.bidv.dto.res;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author vinhnt
 * <b>RESPONSE FOR API: LINK/UNLINK/CHECK CARD, CASH_IN, CASH_OUT</b>
 */
@SuppressWarnings("serial")
public class WalletRes implements Serializable{

    @JsonProperty(value = "service_id")
    private String serviceId;

    @JsonProperty(value = "merchant_id")
    private String merchantId;

    @JsonProperty(value = "transaction_date")
    private String transactionDate;

    @JsonProperty(value = "transaction_id")
    private String transactionId;

    @JsonProperty(value = "response_code")
    private String responseCode;

    @JsonProperty(value = "bank_transaction_id")
    private String bankTransactionId;

    private String list;

    @JsonProperty(value = "more_info")
    private String moreInfo;

    @JsonProperty(value = "redirect_url")
    private String redirectUrl;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


}
