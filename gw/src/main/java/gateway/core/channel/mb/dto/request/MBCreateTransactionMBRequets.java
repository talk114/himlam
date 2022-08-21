package gateway.core.channel.mb.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MBCreateTransactionMBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6248511207676707211L;

	private String client_transaction_id;
	private String card_name;
	private String card_no;
	private String amount;
	private String ccy;
	private String date_open;
	private String fee;
	private String merchant;
	private String mobile;
	private String payment_description;
	private String service_type;

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getDate_open() {
		return date_open;
	}

	public void setDate_open(String date_open) {
		this.date_open = date_open;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPayment_description() {
		return payment_description;
	}

	public void setPayment_description(String payment_description) {
		this.payment_description = payment_description;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

}
