package gateway.core.channel.cybersouce.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class PurchaseTotals implements Serializable {
    private String currency;
    private String grandTotalAmount;
}
