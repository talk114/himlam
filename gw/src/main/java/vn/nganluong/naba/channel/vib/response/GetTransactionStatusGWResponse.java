package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import vn.nganluong.naba.dto.ResponseJson;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GetTransactionStatusGWResponse extends ResponseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -612579503188389228L;

	@JsonProperty(value = "list_transaction")
	private List<ListTransactionStatusResponse> listTrans;


	public List<ListTransactionStatusResponse> getListTrans() {
		return listTrans;
	}


	public void setListTrans(List<ListTransactionStatusResponse> listTrans) {
		this.listTrans = listTrans;
	}
	
	
}
