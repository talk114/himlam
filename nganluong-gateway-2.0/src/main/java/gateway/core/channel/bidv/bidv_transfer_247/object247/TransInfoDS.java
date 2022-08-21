package gateway.core.channel.bidv.bidv_transfer_247.object247;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransInfoDS {
    public static final String CHARACTER_DEFAULT = "|";
    public static final String CHARACTER_DEFAULT_2 = "\n";

    private String chanelId;// Mã kênh giao dịch
    private String serviceId;// Mã dịch vụ/Mã xử lý
    private String serviceProfile;// Mã dịch vụ chi tiết
    private String orderCode;// Mã hóa đơn
    private String customerId;// Mã khách hàng tại đối tác
    private String amount;// Số tiền giao dịch
    private String currencyCode;// Mã tiền tệ giao dịch
    private String traceNumber;// Số Trace hệ thống(bên NCC gửi)
    private String createTime;// Giờ khởi tạo giao dịch (giờ ghi nhận giao dịch được tạo trong hệ thống BIDV)
    private String createDate;// Ngày khởi tạo giao dịch (giờ ghi nhận giao dịch được tạo trong hệ thống BIDV)
    private String paymentTime;// Giờ thanh toán giao dịch (giờ ghi nhận giao dịch được gạch nợ sang hệ thống của đối tác)
    private String paymentDate;// Ngày thanh toán giao dịch (giờ ghi nhận giao dịch được gạch nợ sang hệ thống của đối tác)
    private String accountOut;// Tài khoản ghi nợ
    private String accountIn;// Tài khoản ghi có
    private String cardNumber;// Số thẻ
    private String deviceCodeAcceptsCard;// Mã số thiết bị chấp nhận thẻ
    private String resultsDS;// Kết quả đối soát
    private String requestDS;// Yêu cầu của Đối tác
    private String typeTran;// Loại giao dịch(TTHD/Rút ví điện tử/Nạp ví điện tử)
    private String moreInfo;// Thông tin bổ sung
    private String tranIdBIDV;// ID giao dịch do hệ thống BIDV sinh

    public TransInfoDS() {
    }

    public TransInfoDS(String chanelId, String serviceId, String serviceProfile, String orderCode, String customerId, String amount,
                       String currencyCode, String traceNumber, String createTime, String createDate, String paymentTime, String paymentDate,
                       String accountOut, String accountIn, String cardNumber, String deviceCodeAcceptsCard, String resultsDS, String requestDS,
                       String typeTran, String moreInfo, String tranIdBIDV) {
        this.chanelId = chanelId;
        this.serviceId = serviceId;
        this.serviceProfile = serviceProfile;
        this.orderCode = orderCode;
        this.customerId = customerId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.traceNumber = traceNumber;
        this.createTime = createTime;
        this.createDate = createDate;
        this.paymentTime = paymentTime;
        this.paymentDate = paymentDate;
        this.accountOut = accountOut;
        this.accountIn = accountIn;
        this.cardNumber = cardNumber;
        this.deviceCodeAcceptsCard = deviceCodeAcceptsCard;
        this.resultsDS = resultsDS;
        this.requestDS = requestDS;
        this.typeTran = typeTran;
        this.moreInfo = moreInfo;
        this.tranIdBIDV = tranIdBIDV;
    }

    public String getChanelId() {
        return chanelId;
    }

    public void setChanelId(String chanelId) {
        this.chanelId = chanelId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceProfile() {
        return serviceProfile;
    }

    public void setServiceProfile(String serviceProfile) {
        this.serviceProfile = serviceProfile;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAccountOut() {
        return accountOut;
    }

    public void setAccountOut(String accountOut) {
        this.accountOut = accountOut;
    }

    public String getAccountIn() {
        return accountIn;
    }

    public void setAccountIn(String accountIn) {
        this.accountIn = accountIn;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDeviceCodeAcceptsCard() {
        return deviceCodeAcceptsCard;
    }

    public void setDeviceCodeAcceptsCard(String deviceCodeAcceptsCard) {
        this.deviceCodeAcceptsCard = deviceCodeAcceptsCard;
    }

    public String getResultsDS() {
        return resultsDS;
    }

    public void setResultsDS(String resultsDS) {
        this.resultsDS = resultsDS;
    }

    public String getRequestDS() {
        return requestDS;
    }

    public void setRequestDS(String requestDS) {
        this.requestDS = requestDS;
    }

    public String getTypeTran() {
        return typeTran;
    }

    public void setTypeTran(String typeTran) {
        this.typeTran = typeTran;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getTranIdBIDV() {
        return tranIdBIDV;
    }

    public void setTranIdBIDV(String tranIdBIDV) {
        this.tranIdBIDV = tranIdBIDV;
    }

    public String dataraw() {
        return StringUtils.defaultString(chanelId, "") + CHARACTER_DEFAULT + StringUtils.defaultString(serviceId, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(serviceProfile, "") + CHARACTER_DEFAULT + StringUtils.defaultString(orderCode, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(customerId, "") + CHARACTER_DEFAULT + StringUtils.defaultString(amount, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(currencyCode, "") + CHARACTER_DEFAULT + StringUtils.defaultString(traceNumber, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(createTime, "") + CHARACTER_DEFAULT + StringUtils.defaultString(createDate, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(paymentTime, "") + CHARACTER_DEFAULT + StringUtils.defaultString(paymentDate, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(accountOut, "") + CHARACTER_DEFAULT + StringUtils.defaultString(accountIn, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(cardNumber, "") + CHARACTER_DEFAULT + StringUtils.defaultString(deviceCodeAcceptsCard, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(resultsDS, "") + CHARACTER_DEFAULT + StringUtils.defaultString(requestDS, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(typeTran, "") + CHARACTER_DEFAULT + StringUtils.defaultString(moreInfo, "") + CHARACTER_DEFAULT +
                StringUtils.defaultString(tranIdBIDV, "") + CHARACTER_DEFAULT + CHARACTER_DEFAULT_2 ;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TransInfoDS)) {
            return false;
        }
        TransInfoDS other = (TransInfoDS) obj;
        return this.tranIdBIDV.equals(other.getTranIdBIDV());
    }
}
