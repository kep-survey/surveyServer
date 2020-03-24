package com.kep.surveyServer.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="SURVEY_HISTORY")
public class SurveyHistory {

	@EmbeddedId
	private SurveyHistoryPK id;
	
	@Column(nullable=false)
	private int surveyOrder;
	
	@MapsId("surveyId")
	@ManyToOne
	@JoinColumn(name="survey_id", referencedColumnName = "id", nullable = false)
	private Surveys surveys;
	
	@MapsId("botUserId")
	@ManyToOne
	@JoinColumn(name="bot_user_id", referencedColumnName = "botUserId", nullable = false)
	private Users users;
	
	@Column
	private Timestamp participationTime;
	
	public SurveyHistory(SurveyHistoryPK surveyHistoryPK, int surveyOrder) {
		this.id = surveyHistoryPK;
		this.surveyOrder = surveyOrder;
	}
}