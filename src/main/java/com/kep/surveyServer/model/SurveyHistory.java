package com.kep.surveyServer.model;

import javax.persistence.CascadeType;
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
@ToString
@Entity
@Table(name="SURVEY_HISTORY")
public class SurveyHistory {

	@EmbeddedId
	private SurveyHistoryPK surveyHistoryPK;
	
	@Column(nullable=false)
	private int surveyOrder;
	
	@MapsId("surveyId")
	@ManyToOne
	@JoinColumn(name = "surveyId", referencedColumnName = "id", nullable = false)
	private Surveys surveys;
	
	@MapsId("botUserId")
	@ManyToOne
	@JoinColumn(name = "botUserId", referencedColumnName = "botUserId", nullable = false)
	private Users users;
	
	public SurveyHistory(SurveyHistoryPK surveyHistoryPK, int surveyOrder) {
		this.surveyHistoryPK = surveyHistoryPK;
		this.surveyOrder = surveyOrder;
	}
}