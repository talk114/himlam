package gateway.core.channel.anbinhbank.dto.req;

import gateway.core.channel.anbinhbank.dto.req.body.BaseRequest;

public class RootRequest {

	private HeaderRequest headerReq;
	
	private BaseRequest bodyReq;

	public HeaderRequest getHeaderReq() {
		return headerReq;
	}

	public void setHeaderReq(HeaderRequest headerReq) {
		this.headerReq = headerReq;
	}

	public BaseRequest getBodyReq() {
		return bodyReq;
	}

	public void setBodyReq(BaseRequest bodyReq) {
		this.bodyReq = bodyReq;
	}
	
	
}
