package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "return", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetTransactionHistoryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -612579503189389228L;

	@XmlElement(name = "errorObject", namespace = "http://object.vn.vib.fastpay.erp/xsd", type=ErrorObject.class)
	private ErrorObject errorObject;
	
	
	@XmlElement(name = "listTrans", namespace = "http://object.vn.vib.fastpay.erp/xsd", type=ListTransactionHistoryResponse.class)
	private List<ListTransactionHistoryResponse> listTrans;


	public ErrorObject getErrorObject() {
		return errorObject;
	}


	public void setErrorObject(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}


	public List<ListTransactionHistoryResponse> getListTrans() {
		return listTrans;
	}


	public void setListTrans(List<ListTransactionHistoryResponse> listTrans) {
		this.listTrans = listTrans;
	}
	
	
}
