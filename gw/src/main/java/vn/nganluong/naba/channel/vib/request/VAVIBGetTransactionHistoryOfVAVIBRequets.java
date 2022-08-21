package vn.nganluong.naba.channel.vib.request;

import java.io.Serializable;

public class VAVIBGetTransactionHistoryOfVAVIBRequets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1804633844443481010L;
	private String va_result;
	private String page_num;
	private String page_size;

	private String from_date;
	private String to_date;

	public String getVa_result() {
		return va_result;
	}

	public void setVa_result(String va_result) {
		this.va_result = va_result;
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
