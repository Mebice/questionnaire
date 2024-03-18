package com.example.questionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questionnaire.entity.User;

public interface UserDao extends JpaRepository<User, Integer>{

	List<User> findByQuestionnaireId(int QuestionnaireId);
	
	
}
