package gateway.core.channel.cybersouce.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author TaiND
 */
@Data
public class Response implements Serializable {

    private boolean success;
    private String decision;
    private String responseCode;
    private String paymentTransactionId;
    private String transactionOperation;
    private String providerId;
    private String authorizationCode;
    private String eci;
    private String enviroment;

    private Map<String, String> data;

    public Response(String providerId, String enviroment) {
        super();
        this.providerId = providerId;
        this.enviroment = enviroment;
    }

    public Response(boolean success, String decision, String responseCode, String paymentTransactionId, String authorizationCode, String eci,
            String transactionOperation, String providerId, String enviroment, Map<String, String> data) {
        super();
        this.success = success;
        this.decision = decision;
        this.responseCode = responseCode;
        this.paymentTransactionId = paymentTransactionId;
        this.transactionOperation = transactionOperation;
        this.authorizationCode = authorizationCode;
        this.eci = eci;
        this.providerId = providerId;
        this.enviroment = enviroment;
        this.data = data;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getTransactionOperation() {
        return transactionOperation;
    }

    public void setTransactionOperation(String transactionOperation) {
        this.transactionOperation = transactionOperation;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getEnviroment() {
        return enviroment;
    }

    public void setEnviroment(String enviroment) {
        this.enviroment = enviroment;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    @Override
    public String toString() {
        return "{" + "success=" + success + ", decision=" + decision + ", responseCode=" + responseCode + ",eci=" + eci + ", paymentTransactionId=" + paymentTransactionId + ", transactionOperation=" + transactionOperation + ", providerId=" + providerId + ", enviroment=" + enviroment + ", data=" + data + '}';
    }
}
