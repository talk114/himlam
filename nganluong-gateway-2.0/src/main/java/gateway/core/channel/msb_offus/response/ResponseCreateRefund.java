package gateway.core.channel.msb_offus.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCreateRefund implements Serializable {

    private String code;
    private Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private String reversalTransactionCode;
        private String transactionCode;

        public String getReversalTransactionCode() {
            return reversalTransactionCode;
        }

        public void setReversalTransactionCode(String reversalTransactionCode) {
            this.reversalTransactionCode = reversalTransactionCode;
        }

        public String getTransactionCode() {
            return transactionCode;
        }

        public void setTransactionCode(String transactionCode) {
            this.transactionCode = transactionCode;
        }
    }
}
