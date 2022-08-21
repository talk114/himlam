package gateway.core.channel.napas.dto.request;


import gateway.core.channel.napas.dto.obj.InputParam;
import gateway.core.channel.napas.dto.obj.Order;
import gateway.core.channel.napas.dto.obj.SourceOfFundBase;

public class VerifyOTPTokenReq {
    private Order order;
    private SourceOfFundBase sourceOfFunds;
    private InputParam inputParameters;
    private String apiOperation;
    private String channel;

    public VerifyOTPTokenReq() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public SourceOfFundBase getSourceOfFunds() {
        return sourceOfFunds;
    }

    public void setSourceOfFunds(SourceOfFundBase sourceOfFunds) {
        this.sourceOfFunds = sourceOfFunds;
    }

    public InputParam getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(InputParam inputParameters) {
        this.inputParameters = inputParameters;
    }

    public String getApiOperation() {
        return apiOperation;
    }

    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
