package gateway.core.channel.migs.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionResponse {
    private String result;
    private String gatewayCode;
    private String orderId;
    private String orderAmount;
    private String orderCurrency;
    private String acquirerCode;
    private String acquirerMessage;
    private String transDatetime;
    private String authCode;
    private String accountNumber;
    private String retRefNumber;
    private String acsEci;
    private String response;
}
