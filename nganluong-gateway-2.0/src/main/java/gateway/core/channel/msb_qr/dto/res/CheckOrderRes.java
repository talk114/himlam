package gateway.core.channel.msb_qr.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckOrderRes implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private String errorCode;
    private String message;
    private String merchantId;
    private String terminalId;
    private String payType;
    private String billNumber;
    private String sessionTime;
    private String qrTrace;
    private String settledTime;
    private boolean isSettle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public boolean isSettle() {
        return isSettle;
    }

    public void setSettle(boolean isSettle) {
        this.isSettle = isSettle;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getQrTrace() {
        return qrTrace;
    }

    public void setQrTrace(String qrTrace) {
        this.qrTrace = qrTrace;
    }

    public String getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(String settledTime) {
        this.settledTime = settledTime;
    }
}
