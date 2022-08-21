package gateway.core.channel.bidv.dto.req;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author vinhnt <b>Param request GET_INFO, TOPUP call Api VIMO</b>
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VimoTopupReq implements Serializable {

    @JsonProperty(value = "trans_id")
    private String transId;					// bank trans id

    @JsonProperty(value = "user_id")
    private String customerId;

    @JsonProperty(value = "date_time")
    private String requestDateTime;

    @JsonProperty(value = "amount")
    private String amount;

    @JsonProperty(value = "request_trans_id")
    private String billId;					// vimo bill id

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }


}
