package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */

@SuppressWarnings("serial")
public class BaseTransferBankAccReq implements Serializable {

	@JsonProperty("AccountNo")
	private String accountNumber;

	@JsonProperty("BankCode")
	private String bankCode;

	@JsonProperty("Amount")
	private String amount;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("RefNumber")
	private String refNumber;
	
	@JsonProperty("InqRefNumber")
	private String inqRefNumber;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getInqRefNumber() {
		return inqRefNumber;
	}

	public void setInqRefNumber(String inqRefNumber) {
		this.inqRefNumber = inqRefNumber;
	}

}
