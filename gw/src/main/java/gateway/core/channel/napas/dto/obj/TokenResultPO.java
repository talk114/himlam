package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResultPO {
    @JsonProperty("result")
    private String result;

    @JsonProperty("response")
    private TokenResponsePO response;

    @JsonProperty("token")
    private String token;

    @JsonProperty("card")
    private CardInfo3 card;

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("error")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TokenResponsePO getResponse() {
        return response;
    }

    public void setResponse(TokenResponsePO response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CardInfo3 getCard() {
        return card;
    }

    public void setCard(CardInfo3 card) {
        this.card = card;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
