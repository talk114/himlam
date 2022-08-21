package gateway.core.channel.napas.dto.obj;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResultPO {
    @JsonProperty("apiOperation")
    private String apiOperation;

    @JsonProperty("authorizationResponse")
    private AuthorizationResponse authorizationResponse;

    @JsonProperty("merchantId")
    private String merchantId;

    @JsonProperty("order")
    private Order2 order;

    @JsonProperty("response")
    private PaymentResultPORes response;

    @JsonProperty("result")
    private String result;

    @JsonProperty("sourceOfFunds")
    private SourceOfFundsPO sourceOfFunds;

    @JsonProperty("timeOfRecord")
    private String timeOfRecord;

    @JsonProperty("transaction")
    private PaymentTransaction transaction;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("version")
    private String version;

    @JsonProperty("error")
    private Error error;

    public PaymentResultPORes getResponse() {
        return response;
    }

    public void setResponse(PaymentResultPORes response) {
        this.response = response;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getApiOperation() {
        return apiOperation;
    }

    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
    }

    public AuthorizationResponse getAuthorizationResponse() {
        return authorizationResponse;
    }

    public void setAuthorizationResponse(AuthorizationResponse authorizationResponse) {
        this.authorizationResponse = authorizationResponse;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Order2 getOrder() {
        return order;
    }

    public void setOrder(Order2 order) {
        this.order = order;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public SourceOfFundsPO getSourceOfFunds() {
        return sourceOfFunds;
    }

    public void setSourceOfFunds(SourceOfFundsPO sourceOfFunds) {
        this.sourceOfFunds = sourceOfFunds;
    }

    public String getTimeOfRecord() {
        return timeOfRecord;
    }

    public void setTimeOfRecord(String timeOfRecord) {
        this.timeOfRecord = timeOfRecord;
    }

    public PaymentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

        @Getter
        @Setter
        static class CardSecurityCode {
            private String acquirerCode;
            private String gatewayCode;
        }
    }

