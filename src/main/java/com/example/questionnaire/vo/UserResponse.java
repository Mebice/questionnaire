package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.User;

public class UserResponse {
	
	private List<User> userList;
	
	private User user;
	
	private RtnCode rtnCode;

	public UserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserResponse(User user, RtnCode rtnCode) {
		super();
		this.user = user;
		this.rtnCode = rtnCode;
	}

	public UserResponse(List<User> userList, RtnCode rtnCode) {
		super();
		this.userList = userList;
		this.rtnCode = rtnCode;
	}

	public UserResponse(List<User> userList, User user, RtnCode rtnCode) {
		super();
		this.userList = userList;
		this.user = user;
		this.rtnCode = rtnCode;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}

}
