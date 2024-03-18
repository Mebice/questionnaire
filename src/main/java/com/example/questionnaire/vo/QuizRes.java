package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;


public class QuizRes {

	private List<QuizVo> quizVo;
	
	private List<QnQuVo> qnquVo;

	private RtnCode rtnCode;

	public QuizRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuizRes( RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}
	public QuizRes(List<QuizVo> quizVo, RtnCode rtnCode) {
		super();
		this.quizVo = quizVo;
		this.rtnCode = rtnCode;
	}

	public QuizRes(List<QuizVo> quizVo, List<QnQuVo> qnquVo, RtnCode rtnCode) {
		super();
		this.quizVo = quizVo;
		this.qnquVo = qnquVo;
		this.rtnCode = rtnCode;
	}

	public List<QuizVo> getQuizVo() {
		return quizVo;
	}

	public void setQuizVo(List<QuizVo> quizVo) {
		this.quizVo = quizVo;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}

	public List<QnQuVo> getQnquVo() {
		return qnquVo;
	}

	public void setQnquVo(List<QnQuVo> qnquVo) {
		this.qnquVo = qnquVo;
	}

	
}
