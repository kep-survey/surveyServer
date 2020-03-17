package com.kep.surveyServer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="USERS")
public class Users {
	
	@Id
	private String botUserId;
	
	@Column
	private int age;
	
	@Column
	// Male(1), Female(2), Unknown(3)
	private int gender;
	
	@OneToMany(mappedBy="users")
	private List<SurveyHistory> surveyHistories = new ArrayList<SurveyHistory>();
	
	@OneToMany(mappedBy="users")
	private List<Answers> answers = new ArrayList<Answers>();
}