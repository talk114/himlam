package vn.nganluong.naba.channel.vib.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateVirtualAccountVIBRequets implements Serializable {

	private static final long serialVersionUID = 591349775895314695L;

	@JsonProperty("merchant_code")
	private String merchantCode;
	@JsonProperty("account_number")
	private String accountNumber;
	@JsonProperty("account_name")
	private String accountName;
	private String signedData;

	public void setSigned_data(String signed_data) {
		this.signedData = signed_data;
	}

}
