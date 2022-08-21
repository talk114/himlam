package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class GetTransactionHistoryVIBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6652549413849922543L;

	private String account_no;
	private String c_d;
	private String page_num;
	private String page_size;

	private String from_date;
	private String to_date;

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getC_d() {
		return c_d;
	}

	public void setC_d(String c_d) {
		this.c_d = c_d;
	}

	public String getPage_num() {
		return page_num;
	}

	public void setPage_num(String page_num) {
		this.page_num = page_num;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

}
