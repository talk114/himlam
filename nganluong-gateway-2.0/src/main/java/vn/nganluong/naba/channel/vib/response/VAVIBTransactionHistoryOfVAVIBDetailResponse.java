package vn.nganluong.naba.channel.vib.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VAVIBTransactionHistoryOfVAVIBDetailResponse {

	@JsonProperty("SEQNO")
	private String seq_no;

	@JsonProperty("TRANSEQ")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String tran_seq;
	
	@JsonProperty("TRANTYPE")
	private String transaction_type;

	@JsonProperty("CRDR")
	private String crdr;

	@JsonProperty("STATUS")
	private String status;
	
	@JsonProperty("TRANDATE")
	private String transaction_date;
	
	@JsonProperty("ACCTNAME")
	private String account_name;
	
	@JsonProperty("ACCTNO")
	private String account_no;
	
	@JsonProperty("TRANAMT")
	private String transaction_amount;

	@JsonProperty("TRANAMOUNT")
	private String tran_amount;

	@JsonProperty("ACTUALAMOUNT")
	private String actual_amount;
	
	@JsonProperty("CCY")
	private String ccy;

	@JsonProperty("TRANCCY")
	private String tran_ccy;
	
	@JsonProperty("VARESULT")
	private String va_result;
	
	@JsonProperty("VACODE")
	private String va_code;
	
	@JsonProperty("VAERRCODE")
	private String va_error_code;
	
	@JsonProperty("VAERRDESC")
	private String va_error_desc;
	
	@JsonProperty("VAACCOUNT")
	private String virtual_account_code;
	
	@JsonProperty("BRANCH")
	private String branch;
	
	@JsonProperty("NARRATIVE")
	private String narrative;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("TRANMODE")
	private String tran_mode;

	@JsonProperty("CIFNO")
	private String cif_no;

	public String getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(String transaction_date) {
		this.transaction_date = transaction_date;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getTransaction_amount() {
		return transaction_amount;
	}

	public void setTransaction_amount(String transaction_amount) {
		this.transaction_amount = transaction_amount;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getVa_result() {
		return va_result;
	}

	public void setVa_result(String va_result) {
		this.va_result = va_result;
	}

	public String getVa_code() {
		return va_code;
	}

	public void setVa_code(String va_code) {
		this.va_code = va_code;
	}

	public String getVa_error_code() {
		return va_error_code;
	}

	public void setVa_error_code(String va_error_code) {
		this.va_error_code = va_error_code;
	}

	public String getVa_error_desc() {
		return va_error_desc;
	}

	public void setVa_error_desc(String va_error_desc) {
		this.va_error_desc = va_error_desc;
	}

	public String getVirtual_account_code() {
		return virtual_account_code;
	}

	public void setVirtual_account_code(String virtual_account_code) {
		this.virtual_account_code = virtual_account_code;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getTran_seq() {
		return tran_seq;
	}

	public void setTran_seq(String tran_seq) {
		this.tran_seq = tran_seq;
	}

	public String getTran_mode() {
		return tran_mode;
	}

	public void setTran_mode(String tran_mode) {
		this.tran_mode = tran_mode;
	}

	public String getCrdr() {
		return crdr;
	}

	public void setCrdr(String crdr) {
		this.crdr = crdr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTran_amount() {
		return tran_amount;
	}

	public void setTran_amount(String tran_amount) {
		this.tran_amount = tran_amount;
	}

	public String getActual_amount() {
		return actual_amount;
	}

	public void setActual_amount(String actual_amount) {
		this.actual_amount = actual_amount;
	}

	public String getTran_ccy() {
		return tran_ccy;
	}

	public void setTran_ccy(String tran_ccy) {
		this.tran_ccy = tran_ccy;
	}

	public String getCif_no() {
		return cif_no;
	}

	public void setCif_no(String cif_no) {
		this.cif_no = cif_no;
	}
}
