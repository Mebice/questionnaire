package com.example.questionnaire.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questionnaire.entity.Questionnaire;

public interface QuestionnaireDao extends JpaRepository<Questionnaire, Integer> {

	/**
	 * ���o�̷s�@����� : ����������ƫ�˧ǡA�̷s��������Ʒ|�ܦ��Ĥ@��
	 **/

	public List<Questionnaire> findByIdIn(List<Integer> idList);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual
	(String title,LocalDate startDate ,LocalDate endDate);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue
	(String title,LocalDate startDate ,LocalDate endDate);
}
