package gateway.core.channel.mb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.json.JSONObject;

import java.util.Arrays;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MBRefundResponse {

    private String clientMessageId;

    public MBRefundResponseData data;

    private String errorCode;

    private String[] errorDesc;

    public String getDataTransactionRefundId() {
        return data.getTransactionRefundId();
    }

    public String getDataTransactionId() {
        return data.getTransactionId();
    }

    public String getDataAccountNumber() {
        return data.getAccountNumber();
    }

    public long getDataAmount() {
        return data.getAmount();
    }

    public String getDataFtNumber() {
        return data.getFtNumber();
    }

    @Data
    class MBRefundResponseData {
        private String transactionRefundId;

        private String transactionId;

        private String accountNumber;

        private long amount;

        private String ftNumber;

        @Override
        public String toString() {
            return "MBRefundResponseData{" +
                    "transactionRefundId='" + transactionRefundId + '\'' +
                    ", transactionId='" + transactionId + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    ", amount=" + amount +
                    ", ftNumber='" + ftNumber + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MBRefundResponse{" +
                "clientMessageId='" + clientMessageId + '\'' +
                ", data=" + data +
                ", errorCode='" + errorCode + '\'' +
                ", errorDesc=" + Arrays.toString(errorDesc) +
                '}';
    }
}

