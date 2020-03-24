package com.kep.surveyServer.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SurveyHistoryPK implements Serializable {
	
	private String botUserId;
	
	private Long surveyId;
}
