package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "acctRec", namespace = "http://object.vn.vib.fastpay.erp/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcctRec implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6989323497618916939L;

	
	@XmlElement(name = "curCode", namespace = "http://object.vn.vib.fastpay.erp/xsd")
	private String curCode;
	
	@XmlElement(name = "listAcct", namespace = "http://object.vn.vib.fastpay.erp/xsd", type=BalanceInfo.class)
	private List<BalanceInfo> listAcct;

	public String getCurCode() {
		return curCode;
	}

	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}

	public List<BalanceInfo> getListAcct() {
		return listAcct;
	}

	public void setListAcct(List<BalanceInfo> listAcct) {
		this.listAcct = listAcct;
	}
	
}
