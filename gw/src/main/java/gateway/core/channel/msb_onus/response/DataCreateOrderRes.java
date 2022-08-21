package gateway.core.channel.msb_onus.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataCreateOrderRes implements Serializable {
    private String id;
    private String transDate;
    private String transId;
    private String amount;
    private String billNumber;
    private String currency;
    private String merchantName;
    private String status;
    private String createAt;
}
