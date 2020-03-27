package com.kep.surveyServer.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name="SURVEYS")
public class Surveys {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="registerId")
	Registers registers;
	
	@Column(nullable=false)
	@ColumnDefault("0")
	private int sumQuestions;
	
	@Column(length=20, nullable=false)
	private String title;
	
	@Column(length=255, nullable=false)
	private String description;
	
	@Column
	@CreationTimestamp
	private Timestamp createdDatetime;
	
	@Column
	private Timestamp startDatetime;
	
	@Column
	private Timestamp endDatetime;
	
	@Column(length=255)
	private String welcomeMsg = "";
	
	@Column(length=255)
	private String completeMsg = "";
	
	/* 1 : 설문 배포전 
	 * 2 : 설문 배포중
	 * 3 : 설문 종료
	 */
	@Column
	@ColumnDefault("1")
	private int status = 1;
	
	@OneToMany(mappedBy="surveys")
	private List<SurveyHistory> surveyHistorys = new ArrayList<SurveyHistory>();
	
	@OneToMany(mappedBy="surveys")
	private List<Questions> questions = new ArrayList<Questions>();
	
	@OneToMany(mappedBy="surveys")
	private List<Answers> answers = new ArrayList<Answers>();
	
	@OneToMany(mappedBy="surveys")
	private List<Options> options = new ArrayList<Options>();
}
