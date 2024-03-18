package com.example.questionnaire.vo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Id;

public class QnQuVo {

	private int id;	
	
	private String title;
		
	private String description;	
	
	private boolean published;	
	
	private LocalDate startDate;	
	
	private LocalDate endDate;	

	private int quId;
	
	private String qTitle;
	
	private String optionType;
	
	private boolean necessary;
	
	private String option;

	public QnQuVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QnQuVo(int id, String title, String description, boolean published, LocalDate startDate, LocalDate endDate,
			int quId, String qTitle, String optionType, boolean necessary, String option) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.published = published;
		this.startDate = startDate;
		this.endDate = endDate;
		this.quId = quId;
		this.qTitle = qTitle;
		this.optionType = optionType;
		this.necessary = necessary;
		this.option = option;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getQuId() {
		return quId;
	}

	public void setQuId(int quId) {
		this.quId = quId;
	}

	public String getqTitle() {
		return qTitle;
	}

	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public boolean isNecessary() {
		return necessary;
	}

	public void setNecessary(boolean necessary) {
		this.necessary = necessary;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
