package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationResponse {
    private String cardSecurityCodeError;
    @JsonProperty("date")
    private String date;
    @JsonProperty("financialNetworkDate")
    private String financialNetworkDate;
    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("returnAci")
    private String returnAci;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("time")
    private String time;

    public String getCardSecurityCodeError() {
        return cardSecurityCodeError;
    }

    public void setCardSecurityCodeError(String cardSecurityCodeError) {
        this.cardSecurityCodeError = cardSecurityCodeError;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFinancialNetworkDate() {
        return financialNetworkDate;
    }

    public void setFinancialNetworkDate(String financialNetworkDate) {
        this.financialNetworkDate = financialNetworkDate;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getReturnAci() {
        return returnAci;
    }

    public void setReturnAci(String returnAci) {
        this.returnAci = returnAci;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
