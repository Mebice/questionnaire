package com.example.questionnaire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.service.ifs.UserService;
import com.example.questionnaire.vo.UserRequest;
import com.example.questionnaire.vo.UserResponse;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "api/user/create")
	public UserResponse create(@RequestBody UserRequest req) {
		return userService.create(req);
	}

//	@PostMapping(value = "api/user/searchByQuestionnaireId")
//	public UserResponse searchByQuestionnaireId(@RequestBody int questionnaireId) {
//		return userService.searchByQuestionnaireId(questionnaireId);
//	}

	@GetMapping(value = "api/user/searchByQuestionnaireId")
	public UserResponse searchByQuestionnaireId(@RequestParam int questionnaireId) {
	    return userService.searchByQuestionnaireId(questionnaireId);
	}

	@GetMapping(value = "api/user/searchAll")
	public UserResponse searchAll() {
		return userService.search();
	}

}
