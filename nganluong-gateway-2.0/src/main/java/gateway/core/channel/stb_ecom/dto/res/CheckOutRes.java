package gateway.core.channel.stb_ecom.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckOutRes {
    @JsonProperty("Url")
    private String url;


    public CheckOutRes(String url) {
        this.url = url;
    }
}
