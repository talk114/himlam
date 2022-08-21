package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listTrans", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListTransactionHistoryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 773421427621602957L;

	@XmlElement(name = "BEN_NAME", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String ben_name;

	@XmlElement(name = "BEN_NO", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String ben_no;

	@XmlElement(name = "CCY", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String ccy;

	@XmlElement(name = "CR_DR", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String c_d;

	@XmlElement(name = "RECIPROCAL_ACCOUNT", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String reciprocal_account;

//	@XmlElement(name = "RECIPROCAL_BANK", namespace = "http://object.vn.vib.fastpay.erp/xsd")
//	private String reciprocal_bank;

	@XmlElement(name = "TIME_STAMP", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String time_stamp;

	@XmlElement(name = "acctNo", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String account_no;

	@XmlElement(name = "acct_ccy", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String account_ccy;

	@XmlElement(name = "actualAMT", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String actual_amt;

	@XmlElement(name = "channel_Type", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String channel_type;

	@XmlElement(name = "narrative", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String narrative;

	@XmlElement(name = "reference", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String reference;

	@XmlElement(name = "requestID", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String request_id;

	@XmlElement(name = "tranType", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String tran_type;

	@XmlElement(name = "transAMT", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String trans_amt;

	@XmlElement(name = "transDate", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String trans_date;

	@XmlElement(name = "transSequence", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String trans_sequence;

	@XmlElement(name = "transStatus", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String trans_status;

	@XmlElement(name = "transTime", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String trans_time;

	@XmlElement(name = "transactionType", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String transaction_type;

	@XmlElement(name = "vourcherNo", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String vourcher_no;

	public String getBen_name() {
		return ben_name;
	}

	public void setBen_name(String ben_name) {
		this.ben_name = ben_name;
	}

	public String getBen_no() {
		return ben_no;
	}

	public void setBen_no(String ben_no) {
		this.ben_no = ben_no;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getC_d() {
		return c_d;
	}

	public void setC_d(String c_d) {
		this.c_d = c_d;
	}

	public String getReciprocal_account() {
		return reciprocal_account;
	}

	public void setReciprocal_account(String reciprocal_account) {
		this.reciprocal_account = reciprocal_account;
	}

//	public String getReciprocal_bank() {
//		return reciprocal_bank;
//	}
//
//	public void setReciprocal_bank(String reciprocal_bank) {
//		this.reciprocal_bank = reciprocal_bank;
//	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getAccount_ccy() {
		return account_ccy;
	}

	public void setAccount_ccy(String account_ccy) {
		this.account_ccy = account_ccy;
	}

	public String getActual_amt() {
		return actual_amt;
	}

	public void setActual_amt(String actual_amt) {
		this.actual_amt = actual_amt;
	}

	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getTran_type() {
		return tran_type;
	}

	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}

	public String getTrans_amt() {
		return trans_amt;
	}

	public void setTrans_amt(String trans_amt) {
		this.trans_amt = trans_amt;
	}

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}

	public String getTrans_sequence() {
		return trans_sequence;
	}

	public void setTrans_sequence(String trans_sequence) {
		this.trans_sequence = trans_sequence;
	}

	public String getTrans_status() {
		return trans_status;
	}

	public void setTrans_status(String trans_status) {
		this.trans_status = trans_status;
	}

	public String getTrans_time() {
		return trans_time;
	}

	public void setTrans_time(String trans_time) {
		this.trans_time = trans_time;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getVourcher_no() {
		return vourcher_no;
	}

	public void setVourcher_no(String vourcher_no) {
		this.vourcher_no = vourcher_no;
	}

}
