package vn.nganluong.naba.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ReconciliationDayDataCompareDTO {

    private String recordType;
    private String binBank;
    private Date tranTime;
    private String tranTimeString;

    private String reconCode;
    private String tranType;
    private String fromAcct;
    private String toAcct;
    private BigDecimal tranAmt;
    private String tranAmtString;
    private String ccy;

    private String tranHourString;
    private String tranDateString;
    private String tranSequence; // Mã giao dịch tại ngân hàng

    private String clientTranId;
    private String channelTranId;
    private String channelTranSeq;

    private BigDecimal tranFee;
    private String tranFeeString;

    private String reconResult;
    private String checksum;

    private String status;

    private String drAmtString;
    private String crAmtString;

    private String description;
    private String balance;

    private String totalRecord;
    private String creator;

    private String cardId;
    private String retRefNumber; // W4 system of MB
    private String virtualAccountNo;

    private String napasResult;
    private Integer cardType;

    public ReconciliationDayDataCompareDTO() {
    }

    public ReconciliationDayDataCompareDTO(String toAcct, BigDecimal tranAmt, String description, String clientTranId,
                                           Date tranTime, String sourceAcct, String tranType) {
        this.tranTime = tranTime;
        this.clientTranId = clientTranId;
        this.toAcct = toAcct;
        this.tranAmt = tranAmt;
        this.description = description;
        // this.tranAmtString = tranAmt.toString();
        // this.drAmtString = this.tranAmtString; // Trường hợp tạo file csv set Dr = amount sổ phụ
        // this.tranSequence = "";
        // this.crAmtString = "";
        // this.balance = "";
        this.fromAcct = sourceAcct;
        this.tranType = tranType;
    }

    /**
     * Dùng cho VIB VA
     * @param toAcct
     * @param tranAmt
     * @param description
     * @param clientTranId
     * @param tranTime
     * @param sourceAcct
     * @param tranType
     */
    public ReconciliationDayDataCompareDTO(String toAcct, BigDecimal tranAmt, String description, String clientTranId,
                                           Date tranTime, String sourceAcct, String tranType, String virtualAccountNo) {
        this.tranTime = tranTime;
        this.clientTranId = clientTranId;
        this.toAcct = toAcct;
        this.tranAmt = tranAmt;
        this.description = description;
        // this.tranAmtString = tranAmt.toString();
        // this.drAmtString = this.tranAmtString; // Trường hợp tạo file csv set Dr = amount sổ phụ
        // this.tranSequence = "";
        // this.crAmtString = "";
        // this.balance = "";
        this.fromAcct = sourceAcct;
        this.tranType = tranType;
        this.virtualAccountNo = virtualAccountNo;
    }

    public ReconciliationDayDataCompareDTO(String toAcct, BigDecimal tranAmt, String description, String clientTranId,
                                           Date tranTime, String sourceAcct, String tranType, String channelTranId,
                                           String channelTranSeq, Integer cardType) {
        this.tranTime = tranTime;
        this.clientTranId = clientTranId;
        this.toAcct = toAcct;
        this.tranAmt = tranAmt;
        this.description = description;
        this.fromAcct = sourceAcct;
        this.tranType = tranType;
        this.channelTranId = channelTranId;
        this.channelTranSeq = channelTranSeq;
        this.cardType = cardType;
    }

    public Date getTranTime() {
        return tranTime;
    }

    public void setTranTime(Date tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getClientTranId() {
        return clientTranId;
    }

    public void setClientTranId(String clientTranId) {
        this.clientTranId = clientTranId;
    }

    public String getTranSequence() {
        return tranSequence;
    }

    public void setTranSequence(String tranSequence) {
        this.tranSequence = tranSequence;
    }

    public String getToAcct() {
        return toAcct;
    }

    public void setToAcct(String toAcct) {
        this.toAcct = toAcct;
    }

    public String getFromAcct() {
        return fromAcct;
    }

    public void setFromAcct(String fromAcct) {
        this.fromAcct = fromAcct;
    }

    public BigDecimal getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(BigDecimal tranAmt) {
        this.tranAmt = tranAmt;
    }

    public BigDecimal getTranFee() {
        return tranFee;
    }

    public void setTranFee(BigDecimal tranFee) {
        this.tranFee = tranFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranAmtString() {
        return tranAmtString;
    }

    public void setTranAmtString(String tranAmtString) {
        this.tranAmtString = tranAmtString;
    }

    public String getDrAmtString() {
        return drAmtString;
    }

    public void setDrAmtString(String drAmtString) {
        this.drAmtString = drAmtString;
    }

    public String getCrAmtString() {
        return crAmtString;
    }

    public void setCrAmtString(String crAmtString) {
        this.crAmtString = crAmtString;
    }

    public String getTranFeeString() {
        return tranFeeString;
    }

    public void setTranFeeString(String tranFeeString) {
        this.tranFeeString = tranFeeString;
    }

    public String getTranTimeString() {
        return tranTimeString;
    }

    public void setTranTimeString(String tranTimeString) {
        this.tranTimeString = tranTimeString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getBinBank() {
        return binBank;
    }

    public void setBinBank(String binBank) {
        this.binBank = binBank;
    }

    public String getReconCode() {
        return reconCode;
    }

    public void setReconCode(String reconCode) {
        this.reconCode = reconCode;
    }

    public String getTranHourString() {
        return tranHourString;
    }

    public void setTranHourString(String tranHourString) {
        this.tranHourString = tranHourString;
    }

    public String getTranDateString() {
        return tranDateString;
    }

    public void setTranDateString(String tranDateString) {
        this.tranDateString = tranDateString;
    }

    public String getReconResult() {
        return reconResult;
    }

    public void setReconResult(String reconResult) {
        this.reconResult = reconResult;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getNapasResult() {
        return napasResult;
    }

    public void setNapasResult(String napasResult) {
        this.napasResult = napasResult;
    }

    public String getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(String totalRecord) {
        this.totalRecord = totalRecord;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getChannelTranId() {
        return channelTranId;
    }

    public void setChannelTranId(String channelTranId) {
        this.channelTranId = channelTranId;
    }

    public String getChannelTranSeq() {
        return channelTranSeq;
    }

    public void setChannelTranSeq(String channelTranSeq) {
        this.channelTranSeq = channelTranSeq;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getRetRefNumber() {
        return retRefNumber;
    }

    public void setRetRefNumber(String retRefNumber) {
        this.retRefNumber = retRefNumber;
    }

    public String getVirtualAccountNo() {
        return virtualAccountNo;
    }

    public void setVirtualAccountNo(String virtualAccountNo) {
        this.virtualAccountNo = virtualAccountNo;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
}
