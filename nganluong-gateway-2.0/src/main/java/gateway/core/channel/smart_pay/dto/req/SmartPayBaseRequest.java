package gateway.core.channel.smart_pay.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SmartPayBaseRequest implements Serializable {

    @JsonProperty(value = "channel")
    protected String channel;
    @JsonProperty(value = "subChannel")
    protected String subChannel;
    @JsonProperty(value = "signature")
    protected String signature;

    public SmartPayBaseRequest() {
    }

    public SmartPayBaseRequest(String channel, String subChannel, String signature) {
        this.channel = channel;
        this.subChannel = subChannel;
        this.signature = signature;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
