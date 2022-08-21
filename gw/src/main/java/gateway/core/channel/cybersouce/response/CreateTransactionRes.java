package gateway.core.channel.cybersouce.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CreateTransactionRes {
    @JsonProperty(value = "resultCode")
    private String resultCode;

    @JsonProperty(value = "resultMessage")
    private String resultMessage;

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "href")
    private String href;

    @JsonProperty(value = "encInfo")
    private EncInfo encInfo;
}
