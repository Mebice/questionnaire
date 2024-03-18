package com.example.questionnaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;

import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;

@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService service;
	
	@Autowired
	private QuestionnaireDao qnDao;

	@Test
	public void createTest() {
		Questionnaire questionnaire = new Questionnaire("test1", "test", false, LocalDate.of(2023, 11, 17),
				LocalDate.of(2023, 11, 30));

		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1,1, "test_question_1", "single", false, "AAA;BBB;CCC");
		Question q2 = new Question(2,1, "test_question_2", "multi", false, "10;20;30;40");
		Question q3 = new Question(3,1, "test_question_3", "text", false, "ABC");
		questionList.addAll(Arrays.asList(q1, q2, q3));
		
		QuizReq req = new QuizReq(questionnaire,questionList);
		QuizRes res = service.create(req);
		System.out.println(res.getRtnCode().getCode());
		Assert.isTrue(res.getRtnCode().getCode() == 200, "create error!");
	}
	
	@Test
	public void insertTest() {
		int res = qnDao.insert("qa_02", "qa_01 test", false, LocalDate.of(2023, 11, 24), LocalDate.of(2024, 01, 02));
		System.out.println(res);
	}
	@Test
	public void updateTest() {
		int res = qnDao.update(20, "qn_007", "qn_007 test");
		System.out.println(res);
	}
	@Test
	public void updateDataTest() {
		int res = qnDao.updateData(20, "qn_005", "qn_005 test",LocalDate.of(2023, 11, 20));
		System.out.println(res);
	}
	@Test
	public void selectTest1() {
//		List<Questionnaire> res = qnDao.findByStartDate(LocalDate.of(2023, 11, 19));
//		List<Questionnaire> res = qnDao.findByStartDate1(LocalDate.of(2023, 11, 19));
//		List<Questionnaire> res = qnDao.findByStartDate2(LocalDate.of(2023, 11, 19));
//		List<Questionnaire> res = qnDao.findByStartDate3(LocalDate.of(2023, 11, 19), true);
//		List<Questionnaire> res = qnDao.findByStartDate4(LocalDate.of(2023, 11, 19), true);
		List<Questionnaire> res = qnDao.findByStartDate5(LocalDate.of(2023, 11, 19), true,1);
		System.out.println(res.size());
	}
	
	@Test
	public void limitTest() {
		List<Questionnaire> res = qnDao.findWithLimitAndStartIndex(1, 3);
		res.forEach(item -> {
			System.out.println(item.getId());
		});
	}
	
	@Test
	public void likeTest() {
		List<Questionnaire> res = qnDao.searchTitleLike2("test");
		System.out.println(res.size());
	}
	
	@Test
	public void regexpTest() {
		List<Questionnaire> res = qnDao.searchDescriptionContaining("qa", "qn");
		for(Questionnaire item : res) {
			System.out.println(item.getDescription());
		}
		
	}
	
	@Test
	public void joinTest() {
		List<QnQuVo> res = qnDao.selectJoinQnQu();
		for( QnQuVo item : res) {
			System.out.printf("id: %d, title: %s, qu_id: %d \n",
					item.getId(), item.getqTitle(),item.getQuId());
		}
	}
	
	@Test
	public void selectFuzzyTest() {
		 List<QnQuVo> res = qnDao.selectFuzzy("test", LocalDate.of(1971, 1, 1),LocalDate.of(2099, 1, 1));
		//QuizRes res = service.searchFuzzy("test", LocalDate.of(1971, 1, 1),LocalDate.of(2099, 1, 1));
		System.out.println(res.size());
	}
	
//	@Test
//	public void errorTest() {
//		try (Scanner scan = new Scanner(System.in)){
//			String str = scan.next();
//			System.out.println(str);
//		}
//	}
	
	 
}
