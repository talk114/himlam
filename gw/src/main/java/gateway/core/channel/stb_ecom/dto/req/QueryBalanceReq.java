package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class QueryBalanceReq implements Serializable {

	@JsonProperty("RefNumber")
	private String refNumber;

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	
	
}
