package gateway.core.channel.cybersouce.response;

import lombok.Data;

@Data
public class SubResponse {
    private boolean success;
    private String decision;
    private String responseCode;
    private String paymentTransactionId;
    private String transactionOperation;
    private String providerId;
    private String authorizationCode;
    private String eci;
    private String environment;
    private SubData data;
}
