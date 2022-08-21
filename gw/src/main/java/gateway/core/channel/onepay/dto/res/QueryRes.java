package gateway.core.channel.onepay.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @author anhnq
 *
 */
@SuppressWarnings("serial")
public class QueryRes extends BaseResponse implements Serializable {

	@JsonProperty("vpc_DRExists")
	private String vpcDRExists;

	@JsonProperty("vpc_AuthenticationDate")
	private String vpcAuthenticationDate;

	@JsonProperty("vpc_AdditionData")
	private String vpcAdditionData;

	@JsonProperty("vpc_CurrencyCode")
	private String vpcCurrencyCode;

	public String getVpcDRExists() {
		return vpcDRExists;
	}

	public void setVpcDRExists(String vpcDRExists) {
		this.vpcDRExists = vpcDRExists;
	}

	public String getVpcAuthenticationDate() {
		return vpcAuthenticationDate;
	}

	public void setVpcAuthenticationDate(String vpcAuthenticationDate) {
		this.vpcAuthenticationDate = vpcAuthenticationDate;
	}

	public String getVpcAdditionData() {
		return vpcAdditionData;
	}

	public void setVpcAdditionData(String vpcAdditionData) {
		this.vpcAdditionData = vpcAdditionData;
	}

	public String getVpcCurrencyCode() {
		return vpcCurrencyCode;
	}

	public void setVpcCurrencyCode(String vpcCurrencyCode) {
		this.vpcCurrencyCode = vpcCurrencyCode;
	}

}
