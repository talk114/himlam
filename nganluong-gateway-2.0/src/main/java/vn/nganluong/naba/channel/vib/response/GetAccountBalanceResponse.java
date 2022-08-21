package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "return", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAccountBalanceResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -612579503188389228L;

	@XmlElement(name = "errorObject", namespace = "http://object.vn.vib.fastpay.erp/xsd", type=ErrorObject.class)
	private ErrorObject errorObject;
	
	
	@XmlElement(name = "acctRec", namespace = "http://object.vn.vib.fastpay.erp/xsd", type=AcctRec.class)
	private AcctRec acctRec;


	public ErrorObject getErrorObject() {
		return errorObject;
	}


	public void setErrorObject(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}


	public AcctRec getAcctRec() {
		return acctRec;
	}


	public void setAcctRec(AcctRec acctRec) {
		this.acctRec = acctRec;
	}
	
}
