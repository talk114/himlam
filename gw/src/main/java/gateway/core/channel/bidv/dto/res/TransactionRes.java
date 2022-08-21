package gateway.core.channel.bidv.dto.res;


import java.io.Serializable;

/**
 *
 * @author vinhnt
 * <b>RESPONSE FOR API: INIT_TRANS, VERIFY_CARD, QUERY_TRANSACTION, REFUND_TRANSACTION</b>
 */
@SuppressWarnings("serial")
public class TransactionRes implements Serializable {

    private String serviceId;

    private String merchantId;

    private String transDate;

    private String transId;

    private String responseCode;

    private String bankTransactionId;

    private String list;

    private String redirectUrl;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


}
