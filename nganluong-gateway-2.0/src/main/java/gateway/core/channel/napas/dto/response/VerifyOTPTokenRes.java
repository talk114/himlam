package gateway.core.channel.napas.dto.response;

import gateway.core.channel.napas.dto.obj.MerchantOrderInfo;
import gateway.core.channel.napas.dto.obj.MerchantOrderResponse;
import gateway.core.channel.napas.dto.obj.NapasTransInfo;
import gateway.core.channel.napas.dto.obj.SourceOfFund;

public class VerifyOTPTokenRes extends BaseResponse {

    private MerchantOrderInfo order;

    private MerchantOrderResponse response;

    private SourceOfFund sourceOfFunds;

    private NapasTransInfo transaction;

    private String channel;

    private String version;

    private String dataKey;

    private String napasKey;

    public VerifyOTPTokenRes() {
    }

    public MerchantOrderInfo getOrder() {
        return order;
    }

    public void setOrder(MerchantOrderInfo order) {
        this.order = order;
    }

    public MerchantOrderResponse getResponse() {
        return response;
    }

    public void setResponse(MerchantOrderResponse response) {
        this.response = response;
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
}