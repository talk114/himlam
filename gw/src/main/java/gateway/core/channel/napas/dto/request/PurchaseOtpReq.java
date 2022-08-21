package gateway.core.channel.napas.dto.request;

import gateway.core.channel.napas.dto.obj.InputParam;
import gateway.core.channel.napas.dto.obj.Order;
import gateway.core.channel.napas.dto.obj.SourceOfFund;
import gateway.core.channel.napas.dto.obj.Submerchant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOtpReq {
    private String transaction_id;
    private String order_id;
    private String api_operation;
    private String order_amount;
    private String order_currency;
    private String order_reference;
    private String fund_type;
    private String card_number;
    private String issue_date;
    private String name_on_card;
    private String channel;
    private String client_ip;
    private String device_id;
    private String environment;
    private String card_scheme;
    private boolean enable_3d_secure;
    private Submerchant submerchant;
    private String merchantCode;

//    private Order order;
//    private SourceOfFund sourceOfFunds;
//    private String channel;
//    private InputParam inputParameters;


}
