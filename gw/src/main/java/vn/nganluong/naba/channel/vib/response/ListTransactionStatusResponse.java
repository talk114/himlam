package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listTrans", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListTransactionStatusResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4279809233638343855L;

	@XmlElement(name = "amount", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String amount;

	@XmlElement(name = "bank_status", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String bank_status;

	@XmlElement(name = "customer_status", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String customer_status;

	@XmlElement(name = "erp_trans_id", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String client_transaction_id;

	@XmlElement(name = "fromaccount", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String fromaccount;
	
	@XmlElement(name = "toaccount", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String toaccount;

	@XmlElement(name = "servicetype", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String servicetype;

	@XmlElement(name = "transid", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String transaction_id;

	@XmlElement(name = "ccy", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String ccy;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBank_status() {
		return bank_status;
	}

	public void setBank_status(String bank_status) {
		this.bank_status = bank_status;
	}

	public String getCustomer_status() {
		return customer_status;
	}

	public void setCustomer_status(String customer_status) {
		this.customer_status = customer_status;
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public String getClient_transaction_id() {
		return client_transaction_id;
	}

	public void setClient_transaction_id(String client_transaction_id) {
		this.client_transaction_id = client_transaction_id;
	}

	public String getToaccount() {
		return toaccount;
	}

	public void setToaccount(String toaccount) {
		this.toaccount = toaccount;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getFromaccount() {
		return fromaccount;
	}

	public void setFromaccount(String fromaccount) {
		this.fromaccount = fromaccount;
	}

}
