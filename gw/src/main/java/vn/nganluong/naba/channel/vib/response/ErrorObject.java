package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errorObject", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8029164335414781716L;
	
	
	@XmlElement(name = "errorCode", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String errorCode;
	
	@XmlElement(name = "message", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String message;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
