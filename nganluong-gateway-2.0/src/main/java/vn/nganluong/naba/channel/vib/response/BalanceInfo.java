package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listAcct", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceInfo implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3902299264918582549L;

	@XmlElement(name = "ballType", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String ballType;
	
	@XmlElement(name = "curAmt", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String curAmt;

	public String getBallType() {
		return ballType;
	}

	public void setBallType(String ballType) {
		this.ballType = ballType;
	}

	public String getCurAmt() {
		return curAmt;
	}

	public void setCurAmt(String curAmt) {
		this.curAmt = curAmt;
	} 
	
	
}
