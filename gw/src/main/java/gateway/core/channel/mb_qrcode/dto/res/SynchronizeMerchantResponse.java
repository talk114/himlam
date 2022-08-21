package gateway.core.channel.mb_qrcode.dto.res;

import java.io.Serializable;

public class SynchronizeMerchantResponse extends BaseResponse implements Serializable {

    private String merchantName;
    private String shortMerchantName;
    private String syncError;
    private String message;
    private String syncStatus;

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

    public String getSyncError() {
        return syncError;
    }

    public void setSyncError(String syncError) {
        this.syncError = syncError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
}
