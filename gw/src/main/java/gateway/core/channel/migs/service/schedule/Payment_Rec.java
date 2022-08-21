package gateway.core.channel.migs.service.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sonln
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Payment_Rec implements Serializable {
    private String merchantId;
    private String terminalId;
    private String transType;
    private String requestCategory;
    private String transDatetime;
    private String authCode;
    private String accountNumber;
    private String cardType;
    private String currency;
    private String transAmount;
    private String fee;
    private String vat;
    private String retRefNumber;
    private String postingStatus;
    private String transCondition;
    private String contractName;
    private String orderId;

    public Payment_Rec(String merchantId, String terminalId, String transType, String requestCategory, String transDatetime, String authCode, String accontNumber, String cardType, String currency, String transAmount, String fee, String vat, String retRefNumber, String postingStatus, String transCondition, String contractName, String orderId) {
        this.merchantId = merchantId;
        this.terminalId = terminalId;
        this.transType = transType;
        this.requestCategory = requestCategory;
        this.transDatetime = transDatetime;
        this.authCode = authCode;
        this.accountNumber = accontNumber;
        this.cardType = cardType;
        this.currency = currency;
        this.transAmount = transAmount;
        this.fee = fee;
        this.vat = vat;
        this.retRefNumber = retRefNumber;
        this.postingStatus = postingStatus;
        this.transCondition = transCondition;
        this.contractName = contractName;
        this.orderId = orderId;
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

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getRequestCategory() {
        return requestCategory;
    }

    public void setRequestCategory(String requestCategory) {
        this.requestCategory = requestCategory;
    }

    public String getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(String transDatetime) {
        this.transDatetime = transDatetime;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getRetRefNumber() {
        return retRefNumber;
    }

    public void setRetRefNumber(String retRefNumber) {
        this.retRefNumber = retRefNumber;
    }

    public String getPostingStatus() {
        return postingStatus;
    }

    public void setPostingStatus(String postingStatus) {
        this.postingStatus = postingStatus;
    }

    public String getTransCondition() {
        return transCondition;
    }

    public void setTransCondition(String transCondition) {
        this.transCondition = transCondition;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Payment_Rec) {
            Payment_Rec payment_rec = (Payment_Rec) o;
            return payment_rec.getTransDatetime().equals(this.transDatetime)
                    && payment_rec.getAuthCode().equals(this.authCode)
                    && payment_rec.getAccountNumber().equals(this.accountNumber)
                    && payment_rec.getTransAmount().equals(this.transAmount)
                    && payment_rec.getOrderId().equals(this.orderId)
                    && payment_rec.getRetRefNumber().equals(this.retRefNumber);
        }
        return false;
    }

    public Payment_Rec() {
    }

    public Payment_Rec(String transDatetime, String authCode, String accontNumber, String transAmount, String retRefNumber, String orderId) {
        this.transDatetime = transDatetime;
        this.authCode = authCode;
        this.accountNumber = accontNumber;
        this.transAmount = transAmount;
        this.retRefNumber = retRefNumber;
        this.orderId = orderId;
    }

    public static class PaymentRecBuilder {
        private String merchantId;
        private String terminalId;
        private String transType;
        private String requestCategory;
        private String transDatetime;
        private String authCode;
        private String accountNumber;
        private String cardType;
        private String currency;
        private String amount;
        private String fee;
        private String vat;
        private String retRefNumber;
        private String postingStatus;
        private String transCondition;
        private String contractName;
        private String orderId;

        public PaymentRecBuilder() {
        }

        public PaymentRecBuilder merchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public PaymentRecBuilder terminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public PaymentRecBuilder transType(String transType) {
            this.transType = transType;
            return this;
        }

        public PaymentRecBuilder requestCategory(String requestCategory) {
            this.requestCategory = requestCategory;
            return this;
        }

        public PaymentRecBuilder transDatetime(String transDatetime) {
            this.transDatetime = transDatetime;
            return this;
        }

        public PaymentRecBuilder authCode(String authCode) {
            this.authCode = authCode;
            return this;
        }

        public PaymentRecBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public PaymentRecBuilder cardType(String cardType) {
            this.cardType = cardType;
            return this;
        }

        public PaymentRecBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public PaymentRecBuilder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public PaymentRecBuilder fee(String fee) {
            this.fee = fee;
            return this;
        }

        public PaymentRecBuilder vat(String vat) {
            this.vat = vat;
            return this;
        }

        public PaymentRecBuilder retRefNumber(String retRefNumber) {
            this.retRefNumber = retRefNumber;
            return this;
        }

        public PaymentRecBuilder postingStatus(String postingStatus) {
            this.postingStatus = postingStatus;
            return this;
        }

        public PaymentRecBuilder transCondition(String transCondition) {
            this.transCondition = transCondition;
            return this;
        }

        public PaymentRecBuilder contractName(String contractName) {
            this.contractName = contractName;
            return this;
        }

        public PaymentRecBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Payment_Rec build(){
            Payment_Rec payment = new Payment_Rec(
                    this.merchantId,
                    this.terminalId,
                    this.transType,
                    this.requestCategory,
                    this.transDatetime,
                    this.authCode,
                    this.accountNumber,
                    this.cardType,
                    this.currency,
                    this.amount,
                    this.fee,
                    this.vat,
                    this.retRefNumber,
                    this.postingStatus,
                    this.transCondition,
                    this.contractName,
                    this.orderId
            );

            return payment;
        }
    }

}
