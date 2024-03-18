package com.example.questionnaire.vo;

import com.example.questionnaire.entity.User;

public class UserRequest {
	
	private User user;

	public UserRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRequest(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
