package gateway.core.channel.napas.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.napas.dto.obj.NapasTransInfo;
import gateway.core.channel.napas.dto.obj.Order;
import gateway.core.channel.napas.dto.obj.SourceOfFund;
import gateway.core.channel.napas.dto.obj.TokenResponse;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PaymentWithTokenRes extends BaseResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private TokenResponse response;
    private String merchantId;
    private String apiOperation;
    private String result;
    private Order order;
    private SourceOfFund sourceOfFunds;
    private NapasTransInfo transaction;
    private String version;
    private String channel;
    private String dataKey;
    private String napasKey;
    private Object error;

    public PaymentWithTokenRes() {
    }

    public PaymentWithTokenRes(TokenResponse response, String merchantId, String apiOperation, String result, Order order, SourceOfFund sourceOfFunds, NapasTransInfo transaction, String version, String channel, String dataKey, String napasKey) {
        this.response = response;
        this.merchantId = merchantId;
        this.apiOperation = apiOperation;
        this.result = result;
        this.order = order;
        this.sourceOfFunds = sourceOfFunds;
        this.transaction = transaction;
        this.version = version;
        this.channel = channel;
        this.dataKey = dataKey;
        this.napasKey = napasKey;
    }

    public TokenResponse getResponse() {
        return response;
    }

    public void setResponse(TokenResponse response) {
        this.response = response;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getApiOperation() {
        return apiOperation;
    }

    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public SourceOfFund getSourceOfFunds() {
        return sourceOfFunds;
    }

    public void setSourceOfFunds(SourceOfFund sourceOfFunds) {
        this.sourceOfFunds = sourceOfFunds;
    }

    public NapasTransInfo getTransaction() {
        return transaction;
    }

    public void setTransaction(NapasTransInfo transaction) {
        this.transaction = transaction;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getNapasKey() {
        return napasKey;
    }

    public void setNapasKey(String napasKey) {
        this.napasKey = napasKey;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
