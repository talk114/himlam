package vn.nganluong.naba.dto;

public class CacheRequestDto {

	private String[] entitiesSelect;
	private String[] entitiesList;
	private String requestType;

	private String resultMessage;

	public String[] getEntitiesSelect() {
		return entitiesSelect;
	}

	public void setEntitiesSelect(String[] entitiesSelect) {
		this.entitiesSelect = entitiesSelect;
	}

	public String[] getEntitiesList() {
		return entitiesList;
	}

	public void setEntitiesList(String[] entitiesList) {
		this.entitiesList = entitiesList;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

}
