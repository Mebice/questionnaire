package com.example.questionnaire.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;

import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizDeleteQuestionReq;
import com.example.questionnaire.vo.QuizDeleteQuestionnaireReq;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizSearchQuestionListReq;
import com.example.questionnaire.vo.QuizSearchQuestionnaireListReq;
import com.example.questionnaire.vo.QuizSearchReq;

@RestController
@CrossOrigin
public class QuizController {

	@Autowired
	private QuizService service;

	@PostMapping(value = "api/quiz/create")
	public QuizRes create(@RequestBody QuizReq req) {
		return service.create(req);
	}

	@GetMapping(value = "api/quiz/search")
//	public QuizRes search(@RequestBody QuizSearchReq req) {
//		String title = StringUtils.hasText(req.getTitle()) ? req.getTitle() : ""; 
//		LocalDate startDate = req.getStartDate() != null ? req.getStartDate() : LocalDate.of(1971, 1, 1);
//		LocalDate endDate = req.getEndDate() != null ? req.getEndDate() : LocalDate.of(2099, 12, 31);
//		return service.search(title,startDate,endDate);		
//	}
	public QuizRes search(@RequestBody(required = false) QuizSearchReq req,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

// 如果有傳遞 QuizSearchReq，使用其中的屬性進行搜尋
		if (req != null) {
			title = StringUtils.hasText(req.getTitle()) ? req.getTitle() : "";
			startDate = req.getStartDate() != null ? req.getStartDate() : LocalDate.of(1971, 1, 1);
			endDate = req.getEndDate() != null ? req.getEndDate() : LocalDate.of(2099, 12, 31);
		} else {
// 如果沒有傳遞 QuizSearchReq，則使用 @RequestParam 中的值進行搜尋
			title = StringUtils.hasText(title) ? title : "";
			startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
			endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		}

		System.out.println("Calling service.search with title: " + title + ", start_date: " + startDate + ", end_date: "
				+ endDate);
		return service.search(title, startDate, endDate);
	}

	@PostMapping(value = "api/quiz/update") // 可修改和新增和刪除問題
	public QuizRes update(@RequestBody QuizReq req) {
		
		return service.update(req);
	}

	@PostMapping(value = "api/quiz/deleteQuestionnaire") // 刪除問卷
	public QuizRes deleteQuestionnaire(@RequestBody QuizDeleteQuestionnaireReq req) {
		System.out.println(req.getQnIdList().get(0));
		return service.deleteQuestionnaire(req.getQnIdList());
	}

	@PostMapping(value = "api/quiz/deleteQuestion")
	public QuizRes deleteQuestion(@RequestBody QuizDeleteQuestionReq req) {
		return service.deleteQuestion(req.getQnid(), req.getQuIdList());
	}

	@GetMapping(value = "api/quiz/searchQuestionnaireList")
	public QuestionnaireRes searchQuestionnaireList(@RequestBody QuizSearchQuestionnaireListReq req) {
		return service.searchQuestionnaireList(req.getTitle(), req.getStartDate(), req.getEndDate(), req.isAll());
	}

	@GetMapping(value = "api/quiz/searchQuestionList")
	public QuestionRes searchQuestionList(@RequestBody QuizSearchQuestionListReq req) {
		return service.searchQuestionList(req.getQnid());
	}

}
