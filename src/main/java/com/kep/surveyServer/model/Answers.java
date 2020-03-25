package com.kep.surveyServer.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="ANSWERS")
public class Answers {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="botUserId")
	private Users users;
	
	@ManyToOne
	@JoinColumn(name="surveyId")
	private Surveys surveys;
	
	@ManyToOne
	@JoinColumn(name="questionId")
	private Questions questions;
	
	@Column(nullable=false, updatable=false)
	@CreationTimestamp
	private Timestamp answeredDatetime;
	
	@Column
	private String answer;
	
	public Answers(String answer) {
		this.answer = answer;
	}
}