package gateway.core.channel.stb_ecom.dto.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 
 * @author vinhnt
 *
 */


@SuppressWarnings("serial")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkCardReq{
	@JsonProperty("ProfileID")
	private String profileID;

	@JsonProperty("AccessKey")
	private String accessKey;

	@JsonProperty("TransactionID")
	private String transactionID;

	@JsonProperty("TransactionDateTime")
	private String transactionDateTime;

	@JsonProperty("Language")
	private String language;

	@JsonProperty("SubscribeWithMin")
	private String subscribeWithMin;

	@JsonProperty("IsTokenRequest")
	private String isTokenRequest;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("TotalAmount")
	private String totalAmount;

	@JsonProperty("DomesticFee")
	private String domesticFee;

	@JsonProperty("InternationalFee")
	private String internationalFee;

	@JsonProperty("SSN")
	private String ssn;

	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("FirstName")
	private String firstName;

	@JsonProperty("LastName")
	private String lastName;

	@JsonProperty("Gender")
	private String gender;

	@JsonProperty("Address")
	private String address;

	@JsonProperty("District")
	private String district;

	@JsonProperty("City")
	private String city;

	@JsonProperty("PostalCode")
	private String postalCode;

	@JsonProperty("Country")
	private String country;

	@JsonProperty("Email")
	private String email;

	@JsonProperty("Mobile")
	private String mobile;

	@JsonProperty("ReturnUrl")
	private String returnUrl;

	@JsonProperty("CancelUrl")
	private String cancelUrl;

	@JsonProperty("Signature")
	private String signature;





}
