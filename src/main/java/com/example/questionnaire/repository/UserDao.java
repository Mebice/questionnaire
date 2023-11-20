package com.example.questionnaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questionnaire.entity.User;

public interface UserDao extends JpaRepository<User, String>{

}
