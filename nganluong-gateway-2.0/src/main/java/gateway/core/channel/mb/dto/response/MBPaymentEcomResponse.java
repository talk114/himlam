package gateway.core.channel.mb.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class MBPaymentEcomResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2606628613776235097L;

	@JsonProperty("clientMessageId")
	private String client_message_id;

	@JsonProperty("data")
	private MBPaymentEcomDataResponse data;

	@JsonProperty("errorCode")
	private String error_code;

	@JsonProperty("errorDesc")
	private String[] error_desc;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("transactionRefundId")
	private String transactionRefundId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("transactionId")
	private String transactionId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("accountNumber")
	private String accountNumber;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("amount")
	private String amount;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("ftNumber")
	private String ftNumber;
}
