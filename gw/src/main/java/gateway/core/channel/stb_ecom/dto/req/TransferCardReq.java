package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class TransferCardReq extends BaseTransferCardReq implements Serializable {

	@JsonProperty("InqRefNumber")
	private String inqRefNumber;

	public String getInqRefNumber() {
		return inqRefNumber;
	}

	public void setInqRefNumber(String inqRefNumber) {
		this.inqRefNumber = inqRefNumber;
	}

}
