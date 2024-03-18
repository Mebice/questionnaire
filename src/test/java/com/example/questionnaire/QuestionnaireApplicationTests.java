package com.example.questionnaire;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.questionnaire.entity.User;
import com.example.questionnaire.service.ifs.UserService;
import com.example.questionnaire.vo.UserRequest;
import com.example.questionnaire.vo.UserResponse;

@SpringBootTest
class QuestionnaireApplicationTests {
	
	@Autowired
	UserService userService;

	@Test
	public void userCreateTest() {
		User user = new User();
		user.setPhoneNumber("0987-654-321");
		user.setName("AAA");
		user.setEmail("AAA@gmail.com");
		user.setAge(20);
		user.setQuestionnaireId( 42);
		user.setQuestionId( 42);
		user.setAns("¦^µª´ú¸Õ");
//		user.setDateTime();
		UserResponse response = userService.create(new UserRequest(user));
	}
	
	@Test
	public void userSearchTest() {
		UserResponse response = userService.search();
		List<User> userList = response.getUserList();
		for(User user : userList) {
			System.out.println("Name : " + user.getName());
			System.out.println("Phone Number : " + user.getPhoneNumber());
			System.out.println("Age:" + user.getAge());
			System.out.println("Questionnaire ID : " + user.getQuestionnaireId());
			System.out.println("Question ID : " + user.getQuestionId());
			System.out.println("Answer : " + user.getAns());
			System.out.println("DateTime : " + user.getDateTime());
			System.out.println("================================================");
		}
	}

	@Test
	public void userSearchByIdTest() {
		UserResponse response = userService.searchByQuestionnaireId(0);
		List<User> userList = response.getUserList();
		
		for(User user : userList) {
			System.out.println("User ID : " + user.getNum());
			System.out.println("Name : " + user.getName());
			System.out.println("Phone Number: " + user.getPhoneNumber());
			System.out.println("Age : " + user.getAge());
			System.out.println("Answer : " + user.getAns());
			System.out.println("Answer Time : " + user.getDateTime());
			System.out.println("================================================");
		}
	}
}
