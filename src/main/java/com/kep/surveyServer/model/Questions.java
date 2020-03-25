package com.kep.surveyServer.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
@Entity
@Table(name="QUESTIONS")
public class Questions {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="surveyId")
	private Surveys surveys;
	
	@Column
	private String type;
	
	@Column
	private int questionOrder;
	
	@Column
	private String description;
	
	@Column
	@CreationTimestamp
	private Timestamp createdDatetime;
	
	@Column
	@UpdateTimestamp
	private Timestamp updatedDatetime;
	
	@OneToMany(mappedBy="questions")
	private List<Answers> answers = new ArrayList<Answers>();
	
	@OneToMany(mappedBy="questions")
	private List<Options> options = new ArrayList<Options>();
}