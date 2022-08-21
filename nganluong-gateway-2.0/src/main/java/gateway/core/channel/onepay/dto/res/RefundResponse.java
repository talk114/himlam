package gateway.core.channel.onepay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RefundResponse extends BaseResponse implements Serializable {
    @JsonProperty(value = "vpc_AccessCode")
    private String vpcAccessCode;
    @JsonProperty(value = "vpc_OrgMerchTxnRef")
    private String vpcOrgMerchTxnRef;
    @JsonProperty(value = "vpc_Operator")
    private String vpcOperator;
    @JsonProperty(value = "vpc_SecureHash")
    private String vpcSecureHash;
    private String vpcSecureHashCompare;

    public String getVpcSecureHashCompare() {
        return vpcSecureHashCompare;
    }

    public void setVpcSecureHashCompare(String vpcSecureHashCompare) {
        this.vpcSecureHashCompare = vpcSecureHashCompare;
    }

    public String getVpcAccessCode() {
        return vpcAccessCode;
    }

    public void setVpcAccessCode(String vpcAccessCode) {
        this.vpcAccessCode = vpcAccessCode;
    }

    public String getVpcOrgMerchTxnRef() {
        return vpcOrgMerchTxnRef;
    }

    public void setVpcOrgMerchTxnRef(String vpcOrgMerchTxnRef) {
        this.vpcOrgMerchTxnRef = vpcOrgMerchTxnRef;
    }

    public String getVpcOperator() {
        return vpcOperator;
    }

    public void setVpcOperator(String vpcOperator) {
        this.vpcOperator = vpcOperator;
    }

    public String getVpcSecureHash() {
        return vpcSecureHash;
    }

    public void setVpcSecureHash(String vpcSecureHash) {
        this.vpcSecureHash = vpcSecureHash;
    }
}
