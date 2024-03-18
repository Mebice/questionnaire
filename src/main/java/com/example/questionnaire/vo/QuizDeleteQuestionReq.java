package com.example.questionnaire.vo;

import java.util.List;

public class QuizDeleteQuestionReq {

	private int qnid;
	
	private List<Integer> quIdList;
	
	
	public int getQnid() {
		return qnid;
	}
	public void setQnid(int qnid) {
		this.qnid = qnid;
	}
	public List<Integer> getQuIdList() {
		return quIdList;
	}
	public void setQuIdList(List<Integer> quIdList) {
		this.quIdList = quIdList;
	}
}
