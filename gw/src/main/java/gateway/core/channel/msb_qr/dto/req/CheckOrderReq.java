package gateway.core.channel.msb_qr.dto.req;

import java.io.Serializable;

public class CheckOrderReq extends BaseRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String merchantId;
    private String terminalId;
    private String payType;
    private String billNumber;

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

}
