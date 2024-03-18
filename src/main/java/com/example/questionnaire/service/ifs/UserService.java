package com.example.questionnaire.service.ifs;

import com.example.questionnaire.vo.UserRequest;
import com.example.questionnaire.vo.UserResponse;



public interface UserService {

	public UserResponse create(UserRequest req);

	public UserResponse search();

	public UserResponse searchByQuestionnaireId(int questionnaireId);
	
	

}
