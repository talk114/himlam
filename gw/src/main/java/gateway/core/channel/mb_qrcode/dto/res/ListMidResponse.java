package gateway.core.channel.mb_qrcode.dto.res;

import java.io.Serializable;

public class ListMidResponse extends BaseResponse implements Serializable {

    private String id;
    private String merchantName;
    private String shortMerchantName;
    private String username;
    private String merchantAddress;
    private String merchantIdentity;
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String wardsCode;
    private String wardsName;
    private int paymentType;
    private int fee;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String bankCode;
    private String bankName;
    private String bankBranchCode;
    private String bankBranchName;
    private String bankAcountNumber;
    private String bankAcountName;
    private String bankCurrencyCode;
    private String bankCurrencyName;
    private int Status; //Trạng thái của merchant  active = 1, deactive = 0.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardsCode() {
        return wardsCode;
    }

    public void setWardsCode(String wardsCode) {
        this.wardsCode = wardsCode;
    }

    public String getWardsName() {
        return wardsName;
    }

    public void setWardsName(String wardsName) {
        this.wardsName = wardsName;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
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

    public String getBankCurrencyName() {
        return bankCurrencyName;
    }

    public void setBankCurrencyName(String bankCurrencyName) {
        this.bankCurrencyName = bankCurrencyName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
