package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2811748480272980257L;
	private String STATUSCODE;
	public String getSTATUSCODE() {
		return STATUSCODE;
	}
	public void setSTATUSCODE(String sTATUSCODE) {
		STATUSCODE = sTATUSCODE;
	}
	
	
	
}
