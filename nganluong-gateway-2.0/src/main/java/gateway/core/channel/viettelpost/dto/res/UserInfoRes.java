package gateway.core.channel.viettelpost.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoRes {

	private String name;

	private String birthday;

	private String pid;

	public UserInfoRes() {
	}

	public UserInfoRes(String name, String birthday, String pid) {
		this.name = name;
		this.birthday = birthday;
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
