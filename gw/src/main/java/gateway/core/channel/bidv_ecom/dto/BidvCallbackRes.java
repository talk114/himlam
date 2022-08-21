package gateway.core.channel.bidv_ecom.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author sonln@nganluong.vn
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidvCallbackRes {
    @JsonProperty("Response_Code")
    private String responseCode;

    @JsonProperty("Response_Desc")
    private String responseDesc;

    @JsonProperty("Redirect_Url")
    private String redirectUrl;

    @JsonProperty("More_Info")
    private String moreInfo;

    @JsonProperty("Secure_Code")
    private String secureCode;

}
