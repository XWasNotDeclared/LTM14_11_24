package model;

import java.io.Serializable;

public class UserActive implements Serializable{
	private User user;
	private String status;
	public User getUser() {
		return user;
	}
	public UserActive(User user, String status) {
		super();
		this.user = user;
		this.status = status;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	

}
