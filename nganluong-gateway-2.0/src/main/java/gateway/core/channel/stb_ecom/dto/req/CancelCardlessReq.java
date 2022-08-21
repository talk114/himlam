package gateway.core.channel.stb_ecom.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class CancelCardlessReq extends BaseCardlessReq implements Serializable {

	@JsonProperty("OrgRefNumber")
	private String orgRefNumber;

	public String getOrgRefNumber() {
		return orgRefNumber;
	}

	public void setOrgRefNumber(String orgRefNumber) {
		this.orgRefNumber = orgRefNumber;
	}
	
}
