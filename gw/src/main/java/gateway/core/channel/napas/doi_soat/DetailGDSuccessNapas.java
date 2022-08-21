package gateway.core.channel.napas.doi_soat;

import java.io.Serializable;

public class DetailGDSuccessNapas implements Serializable {

    private String detailsRecord;
    private String mIT;
    private String cardNumber;
    private String processingCode;
    private String serviceCode;
    private String transactionChannelCode;  //6-Mã định danh trường mã kênh thực hiện giao dịch
    private String transAmount; //7- So tien giao dich
    private String realTransAmount; //8- so tien thuc te giao dich
    private String currencyCode; //9: Mã 3 số của loại tiền tệ
    private String amountSettlement; //10: so tien quyet toan
    private String currencyCodeSettlement; //11: Mã tiền tệ đồng tiền quyết toán
    private String exRateCurrCodeSettlement; //12: Tỷ giá chuyển đổi sang đồng tiền quyết toán
    private String cardHolderAmount; //13:Số tiền chủ thẻ
    private String realCardHolderAmount; //14:Số tiền chủ thẻ thực tế bị trừ
    private String currCodeCardHolderAmount; //15: mã đồng tiền chủ thẻ
    private String exRateCurrCodeCardHolderAmount; //16: Tỷ giá chuyển đổi sang đồng tiền chủ thẻ
    private String traceNumber; //17
    private String transTime; //18- gio giao dich
    private String transDate; // 19
    private String settlementDate; // 20- ngay quyet toan
    private String agentTypeCode; //21- ma loai dai ly
    private String panCode;// 22
    private String conditionalCodeAtAccessPointService;// 23- Mã điều kiện tại điểm truy cập  dịch vụ
    private String deviceCodeAcceptsCard; // 24-Mã số thiết bị chấp nhận thẻ
    private String acquirerIdentify; // 25- Mã số của ngân hàng đăng ký tại Napas
    private String issuerIdentify; //26- Mã tổ chức phát hành thẻ
    private String identifyCodeAcceptsCard; //27-Mã xác định đơn vị chấp nhận thẻ
    private String beneficiaryIdentify; //28-Mã Ngân hàng thụ hưởng
    private String accountNumberMaster; //29 Số tài khoản nguồn
    private String destinationAccountNumberOrCardNumber;  // 30-Số thẻ/ số tài khoản đích
    private String feeISSForNapas; //31- Phí ISS (phí dịch vụ của ISS dành cho NAPAS)
    private String feeShareOfISSForACQ; // 32 -Phí ISS (phí chia sẻ của ISS dành cho ACQ)
    private String feeShareOfISSForBNB; // 33
    private String feeServiceOfAQCForNapas; // 34Phí ACQ (phí dịch vụ của ACQ dành cho NAPAS)
    private String feeShareOfACQForISS; //35: Phí ACQ (phí  chia sẻ của ACQ dành cho ISS)
    private String feeShareOfACQForBNB; //36: Phí ACQ (phí  chia sẻ của ACQ dành cho BNB)
    private String feeServiceOfBNBForNapas; //37: Phí ACQ (phí  chia sẻ của ACQ dành cho BNB)
    private String feeShareOfBNBForISS; //38-Phí BNB (phí  chia sẻ của BNB dành cho ISS)
    private String feeShareOfBNBForACQ; //39-(phí chia sẻ của BNB dành cho ACQ
    private String retrievalReferenceNumber; //40-Đối với dịch vụ ECOM : điền mã iao dịch (transaction code)
    private String authorizationNumber; //41-Số cấp phép giao  dịch
    private String transactionReferenceNumber; //42-Số định danh  giao dịch
    private String reconciliationResponseCode; //43Mã đố i soát
    private String reserveInformation1; //Thông tin dự trữ 1
    private String reserveInformation2;//Thông tin dự trữ 2
    private String reserveInformation3;//Thông tin dự trữ 3
    private String checksumLine; //

    public String getDetailsRecord() {
        return detailsRecord;
    }

    public void setDetailsRecord(String detailsRecord) {
        this.detailsRecord = detailsRecord;
    }

    public String getmIT() {
        return mIT;
    }

    public void setmIT(String mIT) {
        this.mIT = mIT;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getTransactionChannelCode() {
        return transactionChannelCode;
    }

    public void setTransactionChannelCode(String transactionChannelCode) {
        this.transactionChannelCode = transactionChannelCode;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getRealTransAmount() {
        return realTransAmount;
    }

    public void setRealTransAmount(String realTransAmount) {
        this.realTransAmount = realTransAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmountSettlement() {
        return amountSettlement;
    }

    public void setAmountSettlement(String amountSettlement) {
        this.amountSettlement = amountSettlement;
    }

    public String getCurrencyCodeSettlement() {
        return currencyCodeSettlement;
    }

    public void setCurrencyCodeSettlement(String currencyCodeSettlement) {
        this.currencyCodeSettlement = currencyCodeSettlement;
    }

    public String getExRateCurrCodeSettlement() {
        return exRateCurrCodeSettlement;
    }

    public void setExRateCurrCodeSettlement(String exRateCurrCodeSettlement) {
        this.exRateCurrCodeSettlement = exRateCurrCodeSettlement;
    }

    public String getCardHolderAmount() {
        return cardHolderAmount;
    }

    public void setCardHolderAmount(String cardHolderAmount) {
        this.cardHolderAmount = cardHolderAmount;
    }

    public String getRealCardHolderAmount() {
        return realCardHolderAmount;
    }

    public void setRealCardHolderAmount(String realCardHolderAmount) {
        this.realCardHolderAmount = realCardHolderAmount;
    }

    public String getCurrCodeCardHolderAmount() {
        return currCodeCardHolderAmount;
    }

    public void setCurrCodeCardHolderAmount(String currCodeCardHolderAmount) {
        this.currCodeCardHolderAmount = currCodeCardHolderAmount;
    }

    public String getExRateCurrCodeCardHolderAmount() {
        return exRateCurrCodeCardHolderAmount;
    }

    public void setExRateCurrCodeCardHolderAmount(String exRateCurrCodeCardHolderAmount) {
        this.exRateCurrCodeCardHolderAmount = exRateCurrCodeCardHolderAmount;
    }

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getAgentTypeCode() {
        return agentTypeCode;
    }

    public void setAgentTypeCode(String agentTypeCode) {
        this.agentTypeCode = agentTypeCode;
    }

    public String getPanCode() {
        return panCode;
    }

    public void setPanCode(String panCode) {
        this.panCode = panCode;
    }

    public String getConditionalCodeAtAccessPointService() {
        return conditionalCodeAtAccessPointService;
    }

    public void setConditionalCodeAtAccessPointService(String conditionalCodeAtAccessPointService) {
        this.conditionalCodeAtAccessPointService = conditionalCodeAtAccessPointService;
    }

    public String getDeviceCodeAcceptsCard() {
        return deviceCodeAcceptsCard;
    }

    public void setDeviceCodeAcceptsCard(String deviceCodeAcceptsCard) {
        this.deviceCodeAcceptsCard = deviceCodeAcceptsCard;
    }

    public String getAcquirerIdentify() {
        return acquirerIdentify;
    }

    public void setAcquirerIdentify(String acquirerIdentify) {
        this.acquirerIdentify = acquirerIdentify;
    }

    public String getIssuerIdentify() {
        return issuerIdentify;
    }

    public void setIssuerIdentify(String issuerIdentify) {
        this.issuerIdentify = issuerIdentify;
    }

    public String getIdentifyCodeAcceptsCard() {
        return identifyCodeAcceptsCard;
    }

    public void setIdentifyCodeAcceptsCard(String identifyCodeAcceptsCard) {
        this.identifyCodeAcceptsCard = identifyCodeAcceptsCard;
    }

    public String getBeneficiaryIdentify() {
        return beneficiaryIdentify;
    }

    public void setBeneficiaryIdentify(String beneficiaryIdentify) {
        this.beneficiaryIdentify = beneficiaryIdentify;
    }

    public String getAccountNumberMaster() {
        return accountNumberMaster;
    }

    public void setAccountNumberMaster(String accountNumberMaster) {
        this.accountNumberMaster = accountNumberMaster;
    }

    public String getDestinationAccountNumberOrCardNumber() {
        return destinationAccountNumberOrCardNumber;
    }

    public void setDestinationAccountNumberOrCardNumber(String destinationAccountNumberOrCardNumber) {
        this.destinationAccountNumberOrCardNumber = destinationAccountNumberOrCardNumber;
    }

    public String getFeeISSForNapas() {
        return feeISSForNapas;
    }

    public void setFeeISSForNapas(String feeISSForNapas) {
        this.feeISSForNapas = feeISSForNapas;
    }

    public String getFeeShareOfISSForACQ() {
        return feeShareOfISSForACQ;
    }

    public void setFeeShareOfISSForACQ(String feeShareOfISSForACQ) {
        this.feeShareOfISSForACQ = feeShareOfISSForACQ;
    }

    public String getFeeShareOfISSForBNB() {
        return feeShareOfISSForBNB;
    }

    public void setFeeShareOfISSForBNB(String feeShareOfISSForBNB) {
        this.feeShareOfISSForBNB = feeShareOfISSForBNB;
    }

    public String getFeeServiceOfAQCForNapas() {
        return feeServiceOfAQCForNapas;
    }

    public void setFeeServiceOfAQCForNapas(String feeServiceOfAQCForNapas) {
        this.feeServiceOfAQCForNapas = feeServiceOfAQCForNapas;
    }

    public String getFeeShareOfACQForISS() {
        return feeShareOfACQForISS;
    }

    public void setFeeShareOfACQForISS(String feeShareOfACQForISS) {
        this.feeShareOfACQForISS = feeShareOfACQForISS;
    }

    public String getFeeShareOfACQForBNB() {
        return feeShareOfACQForBNB;
    }

    public void setFeeShareOfACQForBNB(String feeShareOfACQForBNB) {
        this.feeShareOfACQForBNB = feeShareOfACQForBNB;
    }

    public String getFeeServiceOfBNBForNapas() {
        return feeServiceOfBNBForNapas;
    }

    public void setFeeServiceOfBNBForNapas(String feeServiceOfBNBForNapas) {
        this.feeServiceOfBNBForNapas = feeServiceOfBNBForNapas;
    }

    public String getFeeShareOfBNBForISS() {
        return feeShareOfBNBForISS;
    }

    public void setFeeShareOfBNBForISS(String feeShareOfBNBForISS) {
        this.feeShareOfBNBForISS = feeShareOfBNBForISS;
    }

    public String getFeeShareOfBNBForACQ() {
        return feeShareOfBNBForACQ;
    }

    public void setFeeShareOfBNBForACQ(String feeShareOfBNBForACQ) {
        this.feeShareOfBNBForACQ = feeShareOfBNBForACQ;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getReconciliationResponseCode() {
        return reconciliationResponseCode;
    }

    public void setReconciliationResponseCode(String reconciliationResponseCode) {
        this.reconciliationResponseCode = reconciliationResponseCode;
    }

    public String getReserveInformation1() {
        return reserveInformation1;
    }

    public void setReserveInformation1(String reserveInformation1) {
        this.reserveInformation1 = reserveInformation1;
    }

    public String getReserveInformation2() {
        return reserveInformation2;
    }

    public void setReserveInformation2(String reserveInformation2) {
        this.reserveInformation2 = reserveInformation2;
    }

    public String getReserveInformation3() {
        return reserveInformation3;
    }

    public void setReserveInformation3(String reserveInformation3) {
        this.reserveInformation3 = reserveInformation3;
    }

    public String getChecksumLine() {
        return checksumLine;
    }

    public void setChecksumLine(String checksumLine) {
        this.checksumLine = checksumLine;
    }
}
