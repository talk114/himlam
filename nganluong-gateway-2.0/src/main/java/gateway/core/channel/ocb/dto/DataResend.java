package gateway.core.channel.ocb.dto;

import lombok.Data;

@Data
public class DataResend {

    {
        this.transactionType = "GATEWAYPAYMENT";
        this.partnerCode = "NGANLUONGGATE";
    }
    private String transactionType;
    private String bankRefNo;
    private String partnerCode;
}
