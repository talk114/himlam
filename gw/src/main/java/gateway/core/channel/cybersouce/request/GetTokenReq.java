package gateway.core.channel.cybersouce.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.HashedMap;

import java.io.IOException;
import java.util.Map;

public class GetTokenReq {

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("unchecked")
    public String formatGetReq() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map  = mapper.readValue(mapper.writeValueAsBytes(this), HashedMap.class);
        return getParameterString(map);
    }

    public String getParameterString(Map<String, String> map) {
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return paramString.toString().substring(0, paramString.length() - 1);
    }

}
