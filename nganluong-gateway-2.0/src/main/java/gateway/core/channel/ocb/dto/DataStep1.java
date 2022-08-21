package gateway.core.channel.ocb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataStep1 {
    private String linkId;
    private String cardNumber;
    private String issueDate;
    private String fullName;
    private String accountNumber;
    private String accountType;
    private long transferAmount;
    private String transferDescription;
    private String requiredLink;
    private String partnerCode;
    private String merchantName;
}
