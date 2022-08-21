package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import vn.nganluong.naba.dto.ResponseJson;

public class GetAccountBalanceGWResponse extends ResponseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2992057539749975705L;
	
	private List<BalanceInfo> listBalance;

	public List<BalanceInfo> getListBalance() {
		return listBalance;
	}

	public void setListBalance(List<BalanceInfo> listBalance) {
		this.listBalance = listBalance;
	}



}
