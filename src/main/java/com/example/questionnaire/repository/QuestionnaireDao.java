package com.example.questionnaire.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questionnaire.entity.Questionnaire;

public interface QuestionnaireDao extends JpaRepository<Questionnaire, Integer> {

	/**
	 * 取得最新一筆資料 : 撈取全部資料後倒序，最新的那筆資料會變成第一筆
	 **/

	public List<Questionnaire> findByIdIn(List<Integer> idList);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual
	(String title,LocalDate startDate ,LocalDate endDate);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue
	(String title,LocalDate startDate ,LocalDate endDate);
}
