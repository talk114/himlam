package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
public class TransferVisaReq extends BaseTransferVisaReq implements Serializable {

	@JsonProperty("InqRefNumber")
	private String inqRefNumber;
	
	@JsonProperty("CardToken")
	private String cardToken;

	public String getInqRefNumber() {
		return inqRefNumber;
	}

	public void setInqRefNumber(String inqRefNumber) {
		this.inqRefNumber = inqRefNumber;
	}

	public String getCardToken() {
		return cardToken;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}
	
	
	
	
}
