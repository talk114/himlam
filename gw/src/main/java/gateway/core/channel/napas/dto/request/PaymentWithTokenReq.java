package gateway.core.channel.napas.dto.request;

import gateway.core.channel.napas.dto.obj.InputParam;
import gateway.core.channel.napas.dto.obj.Order;
import gateway.core.channel.napas.dto.obj.SourceOfFund;

import java.io.Serializable;

public class PaymentWithTokenReq implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String apiOperation;

    private Order order;

    private SourceOfFund sourceOfFunds;

    private String channel;

    private InputParam inputParameters;


    public PaymentWithTokenReq() {
    }

    public PaymentWithTokenReq(String apiOperation, Order order, SourceOfFund sourceOfFunds, String channel, InputParam inputParameters) {
        this.apiOperation = apiOperation;
        this.order = order;
        this.sourceOfFunds = sourceOfFunds;
        this.channel = channel;
        this.inputParameters = inputParameters;
    }

    public String getApiOperation() {
        return apiOperation;
    }

    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public InputParam getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(InputParam inputParameters) {
        this.inputParameters = inputParameters;
    }

}
