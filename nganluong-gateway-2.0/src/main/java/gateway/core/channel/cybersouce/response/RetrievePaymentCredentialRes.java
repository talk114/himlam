package gateway.core.channel.cybersouce.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RetrievePaymentCredentialRes {
    @JsonProperty(value = "resultCode")
    private String resultCode;

    @JsonProperty(value = "resultMessage")
    private String resultMessage;

    @JsonProperty(value = "features")
    private String[] features;

    @JsonProperty(value = "card_brand")
    private String card_brand;

    @JsonProperty(value = "card_last4digits")
    private String card_last4digits;

    @JsonProperty(value = "3DS")
    private _3DS _3DS;

    @JsonProperty(value = "wallet_dm_id")
    private String wallet_dm_id;

}
