package gateway.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PGRequest {

    @NotNull(message = "The fnc paramter cannot be null")
    private String fnc;

    @JsonProperty(value = "channel_id")
    private Integer channelId;

    @JsonProperty(value = "channel_name")
    @NotNull(message = "The channel_name paramter cannot be null")
    private String channelName;

    @NotNull(message = "The data paramter cannot be null")
    private String data;

    @NotNull(message = "The checksum paramter cannot be null")
    private String checksum;

    @NotNull(message = "The pg_user_code paramter cannot be null")
    @JsonProperty(value = "pg_user_code")
    private String pgUserCode;

    public String getFnc() {
        return fnc;
    }

    public void setFnc(String fnc) {
        this.fnc = fnc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getPgUserCode() {
        return pgUserCode;
    }

    public void setPgUserCode(String pgUserCode) {
        this.pgUserCode = pgUserCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "PGRequest{" + "fnc=" + fnc + ", channelId=" + channelId + ", data=" + data + ", checksum=" + checksum
                + ", pgUserCode=" + pgUserCode + '}';
    }

}
