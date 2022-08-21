package gateway.core.channel.migs.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.channel.migs.entity.Order;
import gateway.core.channel.migs.entity.SourceOfFunds;
import gateway.core.channel.migs.entity.TransactionInitAuthRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenResponse {
    private AuthenticationRes authentication;
    private String correlationId;
    private Order order;
    private SourceOfFunds sourceOfFunds;
    private String merchant;
    private ResponseInitAuth response;
    private String result;
    private String timeOfLastUpdate;
    private String timeOfRecord;
    private TransactionInitAuthRes transaction;
    private String version;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class AuthenticationRes {
    @JsonProperty("3ds1")
    private _3ds1 _3ds1;
    @JsonProperty("3ds")
    private Authent3DS _3ds;
    @JsonProperty("3ds2")
    private Authent3DS2 _3ds2;
    private String acceptVersions;
    private String channel;
    private String purpose;
    private Redirect redirect;
    private String redirectHtml;
    private String version;
    private String payerInteraction;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseInitAuth {
    private String gatewayCode;
    private String gatewayRecommendation;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class _3ds1 {
    private String veResEnrolled;
    private String paResStatus;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class Redirect {
    private Customized customized;
    private String domainName;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class Customized {
    @JsonProperty("3DS")
    private _3DS _3ds;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class _3DS {
    private String methodPostData;
    private String methodUrl;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class Authent3DS {
    private String acsEci;
    private String authenticationToken;
    private String transactionId;
}
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class Authent3DS2 {
    private String transactionStatus;
}

