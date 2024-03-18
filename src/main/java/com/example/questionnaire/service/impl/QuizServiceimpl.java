package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@EnableScheduling
@Service
public class QuizServiceimpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	@Transactional // @Transactional:只有全部成功或失敗，不能寫在私人private
	@Override
	public QuizRes create(QuizReq req) {
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		int quId = qnDao.save(req.getQuestionnaire()).getId(); // 保存問卷信息
		List<Question> quList = req.getQuestionList(); // 設置問題的問卷ID
		if (quList.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		for (Question qu : quList) {
			qu.setQnId(quId);
		}
		quDao.saveAll(quList); // 保存問題
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();
		if (!StringUtils.hasText(qn.getTitle()) || !StringUtils.hasText(qn.getDescription())
				|| qn.getStartDate() == null || qn.getEndDate() == null || qn.getStartDate().isAfter(qn.getEndDate())) { // 開始時間之後是結束時間
			return new QuizRes(RtnCode.QUESTIONNAIRE_PARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQuId() <= 0 || !StringUtils.hasText(qu.getqTitle()) || !StringUtils.hasText(qu.getOptionType())
					|| !StringUtils.hasText(qu.getOption())) {
				return new QuizRes(RtnCode.QUESTION_PARAM_ERROR);
			}
		}
		return null;
	}

	@Transactional
	@Override
	public QuizRes update(QuizReq req) {
		// 檢查參數:檢查輸入的 QuizReq 物件是否符合要求，例如問卷標題、描述、開始和結束時間等
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) { // 如果不符合條件
			return checkResult; // 返回相應的錯誤碼
		}
		// 檢查問卷ID:檢查是針對新建問卷還是更新現有問卷，以及刪除問題的操作是否指向正確的問卷ID
		checkResult = checkQuestionnaireId(req);
		if (checkResult != null) { // 如果不符合條件
			return checkResult; // 返回相應的錯誤碼
		}
		// 查詢問卷: 根據給定的問卷ID查詢該問卷是否存在。如果問卷不存在，返回問卷ID未找到的錯誤碼
		Optional<Questionnaire> qnOp = qnDao.findById(req.getQuestionnaire().getId());
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
		}
		// collect delete_question_id // 收集要刪除的問題ID
		List<Integer> deletedQuIdList = new ArrayList<>();
		
		System.out.println(req.getDeleteQuestionList().size());
		for (Question qu : req.getDeleteQuestionList()) {
			deletedQuIdList.add(qu.getQuId());
			System.out.println(qu.getQuId());
		}
		Questionnaire qn = qnOp.get();
		// 可以修改的條件 :
		// 1. 尚未發布: is_published == false,可以修改
		// 2. 已發布但尚未開始進行: is_published == true + 當前時間必須小於 start_date，可以修改
		if (!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {
			qnDao.save(req.getQuestionnaire()); // 保存問卷
			quDao.saveAll(req.getQuestionList()); // 保存問題
			if (!deletedQuIdList.isEmpty()) {// 如果有要刪除的問題（deletedQuIdList 不為空），則也刪除這些問題
				System.out.println("==============================");
				quDao.deleteAllByQnIdAndQuIdIn(qn.getId(), deletedQuIdList);
			}
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		return new QuizRes(RtnCode.UPDATE_ERROR);
	}

	private QuizRes checkQuestionnaireId(QuizReq req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		// check new or update question_id
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}
		// check delete question_id
		List<Question> quDelList = req.getDeleteQuestionList();
		for (Question qu : quDelList) {
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}
		return null;

	}

	@Transactional
	@Override
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList) {
		// 根據問卷ID列表查詢問卷
		List<Questionnaire> qnList = qnDao.findByIdIn(qnIdList);
		// 用於存儲可刪除的問卷ID列表
		List<Integer> idList = new ArrayList<>();
		// 迭代查詢到的問卷列表
//		for (Questionnaire qn : qnList) {			
//			// 檢查問卷是否未發布或已發布但開始日期在當前日期之前
//			if (!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {
//				// 將符合條件的問卷ID添加到可刪除的列表中
//				idList.add(qn.getId());
//			}
//		}
		// 遍历问卷列表，获取所有问卷的 ID，不添加條件，因為想要問卷都能夠被刪除
		for (Questionnaire qn : qnList) {
		    idList.add(qn.getId());
		}
		
		if (!idList.isEmpty()) {
			qnDao.deleteAllById(idList);
			quDao.deleteAllByQnIdIn(idList);
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes deleteQuestion(int qnid, List<Integer> quIdList) {
		// 刪除問卷裡的題目
		Optional<Questionnaire> qnOp = qnDao.findById(qnid);
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		Questionnaire qn = qnOp.get();
		if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
			quDao.deleteAllByQnIdIn(quIdList);
			quDao.deleteAllByQnIdAndQuIdIn(qnid, quIdList);
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

//	@Cacheable(cacheNames = "search", 
//			key = "#title.concat('_').concat(#startDate.toString()).concat('_').concat(#endDate.toString())",
//			unless= "#result.rtnCode.code !=200")
	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		title = StringUtils.hasText(title) ? title : ""; // 三元式:變數 = 判斷式?true:false
//		if (!StringUtils.hasText(title)) {
//			title = "";
//		}
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
//		if(startDate == null) {
//			startDate = LocalDate.of(1971, 1, 1);
//		}
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
//		if(endDate == null) {
//			endDate = LocalDate.of(2099, 12, 31); 
//		}
		// 找符合搜尋條件的問卷有X筆:qnList //只有問卷列表清單不含題目
		List<Questionnaire> qnList = qnDao
				.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);

		List<Integer> qnIds = new ArrayList<>();
		for (Questionnaire qu : qnList) {
			qnIds.add(qu.getId()); // 題目的Id添加到多個題目List // List 才有add
		}
		// 找已符合問卷的題目y筆:quList
		List<Question> quList = quDao.findAllByQnIdIn(qnIds);

		// 問卷和題目做配對
		List<QuizVo> quizVoList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo(); // vo放問卷和題目
			vo.setQuestionnaire(qn);
			List<Question> questionList = new ArrayList<>(); // 找到符合的所有的配對放在questionList
			for (Question qu : quList) {
				if (qu.getQnId() == qn.getId()) { // 題目qnid 是否等於問卷Id
					questionList.add(qu); // 如果符合，放在questionList
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}
		return new QuizRes(quizVoList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,
			boolean isAll) {
		title = StringUtils.hasText(title) ? title : ""; // 三元式:變數 = 判斷式?true:false
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		// 找符合搜尋條件的問卷有X筆:qnList //只有問卷列表清單不含題目
		List<Questionnaire> qnList = new ArrayList<>();
		if (!isAll) {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate,
					endDate);
		} else {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(
					title, startDate, endDate);
		}

		return new QuestionnaireRes(qnList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionRes searchQuestionList(int qnid) {
		if (qnid <= 0) {
			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnid));
		return new QuestionRes(quList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes searchFuzzy(String title, LocalDate startDate, LocalDate endDate) {
		List<QnQuVo> res = qnDao.selectFuzzy(title, startDate, endDate);
		return new QuizRes(null, res, RtnCode.SUCCESSFUL);

	}

	//               //秒 分時 日 月週
	@Scheduled(cron = "0 * 15 * * *")
	public void schedule() {
		System.out.println(LocalDate.now());
	}

	//問卷的時間狀態自動更改
//  				 //秒 分時 日 月週
	@Scheduled(cron = "0 * 15 * * *")
	public void updateQnStatue() {
		LocalDate today = LocalDate.now();
		int res = qnDao.updateQnStatus(today);
		System.out.println(today);
		System.out.println(res);
	}

}
