package gateway.core.channel.msb_onus.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetTransactionRes implements Serializable {
    private String id;
    private String transDate;
    private String transId;
    private String amount;
    private String billNumber;
    private String currency;
    private String status;
    private String payDateStr;
}
