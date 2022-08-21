package gateway.core.channel.cybersouce.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class Order implements Serializable {
    private String orderNumber;
    private long amount;
    private String currencyCode;
}
