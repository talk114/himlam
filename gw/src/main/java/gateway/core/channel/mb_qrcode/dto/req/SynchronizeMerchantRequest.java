package gateway.core.channel.mb_qrcode.dto.req;

import java.io.Serializable;

public class SynchronizeMerchantRequest extends BaseRequest implements Serializable {

    private String merchantName;
    private String shortMerchantName;
    private String merchantAddress;
    private String merchantIdentity;
    private String provinceCode;
    private String districtCode;
    private String wardsCode;
    private int paymentType;
    private int fee;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String bankCode;
    private String bankCodeBranch;
    private String bankAcountNumber;
    private String bankAcountName;
    private String bankCurrencyCode;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getShortMerchantName() {
        return shortMerchantName;
    }

    public void setShortMerchantName(String shortMerchantName) {
        this.shortMerchantName = shortMerchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantIdentity() {
        return merchantIdentity;
    }

    public void setMerchantIdentity(String merchantIdentity) {
        this.merchantIdentity = merchantIdentity;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getWardsCode() {
        return wardsCode;
    }

    public void setWardsCode(String wardsCode) {
        this.wardsCode = wardsCode;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCodeBranch() {
        return bankCodeBranch;
    }

    public void setBankCodeBranch(String bankCodeBranch) {
        this.bankCodeBranch = bankCodeBranch;
    }

    public String getBankAcountNumber() {
        return bankAcountNumber;
    }

    public void setBankAcountNumber(String bankAcountNumber) {
        this.bankAcountNumber = bankAcountNumber;
    }

    public String getBankAcountName() {
        return bankAcountName;
    }

    public void setBankAcountName(String bankAcountName) {
        this.bankAcountName = bankAcountName;
    }

    public String getBankCurrencyCode() {
        return bankCurrencyCode;
    }

    public void setBankCurrencyCode(String bankCurrencyCode) {
        this.bankCurrencyCode = bankCurrencyCode;
    }
}
