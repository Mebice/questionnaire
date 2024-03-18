package com.example.questionnaire.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.repository.UserDao;
import com.example.questionnaire.service.ifs.UserService;
import com.example.questionnaire.vo.UserRequest;
import com.example.questionnaire.vo.UserResponse;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public UserResponse create(UserRequest req) {
		
		User user = req.getUser();
		user.setDateTime(LocalDateTime.now());
		User saveUser = userDao.save(user);
		return new UserResponse(saveUser, RtnCode.SUCCESSFUL);
	}

	@Override
	public UserResponse search() {
		List<User> userList = userDao.findAll();

		return new UserResponse(userList, RtnCode.SUCCESSFUL);
	}

	@Override
	public UserResponse searchByQuestionnaireId(int questionnaireId) {
		List<User> userList = userDao.findByQuestionnaireId(questionnaireId);

		return new UserResponse(userList, RtnCode.SUCCESSFUL);
	}

}
