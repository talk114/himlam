package vn.nganluong.naba.channel.vib.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ReconciliationMonthDataCompareDTO {

	private String recordType;
	private String sourceAcct;
	private String tranType;
	private Date fromDate;
	private Date toDate;

	private String fromDateString;
	private String toDateString;

	private String channelTotalItems;
	private String channelTotalFee;
	private String channelTotalAmt;

	private long pgTotalItems;
	private BigDecimal pgTotalAmt;
	private String pgTotalAmtString;
	private String pgTotalItemsString;

	private BigDecimal chargeAmt;
	private String chargeAmtString;
	private String system;

	private boolean isDiffAmt;
	private boolean isDiffTotal;

	private String totalRecord;
	private String creator;
	private String tranDateString;
	private String tranHourString;
	private String binBank;
	private String reconResult;

	private String checksum;

	public ReconciliationMonthDataCompareDTO() {

	}

	public ReconciliationMonthDataCompareDTO(long totalItems, BigDecimal pgTotalAmt) {
		this.pgTotalItems = totalItems;
		this.pgTotalAmt = pgTotalAmt;
	}

	public ReconciliationMonthDataCompareDTO(String sourceAcct, String tranType, long totalItems,
			BigDecimal pgTotalAmt) {
		this.sourceAcct = sourceAcct;
		this.tranType = tranType;
		this.pgTotalItems = totalItems;
		this.pgTotalAmt = pgTotalAmt;

	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getSourceAcct() {
		return sourceAcct;
	}

	public void setSourceAcct(String sourceAcct) {
		this.sourceAcct = sourceAcct;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getFromDateString() {
		return fromDateString;
	}

	public void setFromDateString(String fromDateString) {
		this.fromDateString = fromDateString;
	}

	public String getToDateString() {
		return toDateString;
	}

	public void setToDateString(String toDateString) {
		this.toDateString = toDateString;
	}

	public String getChannelTotalItems() {
		return channelTotalItems;
	}

	public void setChannelTotalItems(String channelTotalItems) {
		this.channelTotalItems = channelTotalItems;
	}

	public String getChannelTotalFee() {
		return channelTotalFee;
	}

	public void setChannelTotalFee(String channelTotalFee) {
		this.channelTotalFee = channelTotalFee;
	}

	public String getChannelTotalAmt() {
		return channelTotalAmt;
	}

	public void setChannelTotalAmt(String channelTotalAmt) {
		this.channelTotalAmt = channelTotalAmt;
	}

	public long getPgTotalItems() {
		return pgTotalItems;
	}

	public void setPgTotalItems(long pgTotalItems) {
		this.pgTotalItems = pgTotalItems;
	}

	public BigDecimal getPgTotalAmt() {
		return pgTotalAmt;
	}

	public void setPgTotalAmt(BigDecimal pgTotalAmt) {
		this.pgTotalAmt = pgTotalAmt;
	}

	public BigDecimal getChargeAmt() {
		return chargeAmt;
	}

	public void setChargeAmt(BigDecimal chargeAmt) {
		this.chargeAmt = chargeAmt;
	}

	public String getChargeAmtString() {
		return chargeAmtString;
	}

	public void setChargeAmtString(String chargeAmtString) {
		this.chargeAmtString = chargeAmtString;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public boolean isDiffAmt() {
		return isDiffAmt;
	}

	public void setDiffAmt(boolean isDiffAmt) {
		this.isDiffAmt = isDiffAmt;
	}

	public boolean isDiffTotal() {
		return isDiffTotal;
	}

	public void setDiffTotal(boolean isDiffTotal) {
		this.isDiffTotal = isDiffTotal;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getPgTotalAmtString() {
		return pgTotalAmtString;
	}

	public void setPgTotalAmtString(String pgTotalAmtString) {
		this.pgTotalAmtString = pgTotalAmtString;
	}

	public String getPgTotalItemsString() {
		return pgTotalItemsString;
	}

	public void setPgTotalItemsString(String pgTotalItemsString) {
		this.pgTotalItemsString = pgTotalItemsString;
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

	public String getTranDateString() {
		return tranDateString;
	}

	public void setTranDateString(String tranDateString) {
		this.tranDateString = tranDateString;
	}

	public String getBinBank() {
		return binBank;
	}

	public void setBinBank(String binBank) {
		this.binBank = binBank;
	}

	public String getTranHourString() {
		return tranHourString;
	}

	public void setTranHourString(String tranHourString) {
		this.tranHourString = tranHourString;
	}

	public String getReconResult() {
		return reconResult;
	}

	public void setReconResult(String reconResult) {
		this.reconResult = reconResult;
	}

}
