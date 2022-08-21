package gateway.core.channel.cybersouce.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NotifyPaymentResultReq {
    private String reference;
    private String status;
    private String service;
    private String provider;
    private String merchantName;
    private String merchantReference;
    private String timestamp;
}
