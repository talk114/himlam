package gateway.core.channel.vccb.dto.req;

import java.io.Serializable;

/**
 * @author nbthanh
 */

public class RootRequest implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String requestID;
    
    private String clientCode;
    
    private String clientUserID;
    
    private String time;
    
    private String data;
    
    private String signature;
    
    private String checksum;

    public RootRequest(String requestID, String clientCode, String clientUserID, String time, String data,
                       String signature) {
		super();
		this.requestID = requestID;
		this.clientCode = clientCode;
		this.clientUserID = clientUserID;
		this.time = time;
		this.data = data;
		this.signature = signature;
	}

    
    
	public String getChecksum() {
		return checksum;
	}



	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}



	public String getData() {
		return data;
	}



	public void setData(String data) {
		this.data = data;
	}



	public String getSignature() {
		return signature;
	}



	public void setSignature(String signature) {
		this.signature = signature;
	}



	public RootRequest() {
    }

    

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientUserID() {
        return clientUserID;
    }

    public void setClientUserID(String clientUserID) {
        this.clientUserID = clientUserID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

