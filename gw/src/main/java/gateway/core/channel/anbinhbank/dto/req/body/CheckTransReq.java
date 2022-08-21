package gateway.core.channel.anbinhbank.dto.req.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckTransReq extends BaseRequest {

	private String originTransId;

	private String queryType;

	public CheckTransReq() {

	}

	public CheckTransReq(String partnerId, String originTransId, String queryType) {
		super();
		this.partnerId = partnerId;
		this.originTransId = originTransId;
		this.queryType = queryType;
	}

	@JsonProperty("PartnerRef")
	public String getOriginTransId() {
		return originTransId;
	}

	@JsonProperty("query_trans_id")
	public void setOriginTransId(String originTransId) {
		this.originTransId = originTransId;
	}

	@JsonProperty("TypeId")
	public String getQueryType() {
		return queryType;
	}

	@JsonProperty("query_type")
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	@Override
	public String rawData() {
		return CHARACTER_SEPERATE + partnerId + CHARACTER_SEPERATE + originTransId + CHARACTER_SEPERATE
				+ (queryType == null ? "" : (queryType  + CHARACTER_SEPERATE));
	}
}
