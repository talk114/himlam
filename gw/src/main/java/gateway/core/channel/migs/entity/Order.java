package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order implements Serializable {
    private String authenticationStatus;
    private String creationTime;
    private String currency;
    private String id;
    private String lastUpdatedTime;
    private String merchantCategoryCode;
    private String status;
    private String totalAuthorizedAmount;
    private String totalCapturedAmount;
    private String totalRefundedAmount;
    private String amount;
    private String description;
    private String reference;
    private ValueTransfer valueTransfer;
}

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
 class ValueTransfer  {
    private String accountType;
}