package vn.nganluong.naba.dto;

import java.io.Serializable;

public class ScheduleSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8260280607754953101L;
	private String channelId;
	private String functionId;
	private String cron_expression;

	private Integer pageOfList;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getCron_expression() {
		return cron_expression;
	}

	public void setCron_expression(String cron_expression) {
		this.cron_expression = cron_expression;
	}

	public Integer getPageOfList() {
		return pageOfList;
	}

	public void setPageOfList(Integer pageOfList) {
		this.pageOfList = pageOfList;
	}

}
