package gateway.core.channel.msb_qr.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckTerminalVimoReq {

    public CheckTerminalVimoReq(String channelName, String terminalId) {
        super();
        this.channelName = channelName;
        this.terminalId = terminalId;
    }

    @JsonProperty("channel_name")
    private String channelName;

    @JsonProperty("terminal_id")
    private String terminalId;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

}
