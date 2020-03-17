package com.kep.surveyServer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.kep.surveyServer.model.Surveys;
import com.kep.surveyServer.repository.SurveysRepository;

@Service
public class SurveyServerService {
	
	@Autowired
	private SurveysRepository surveysRepository;
	
	public String setSurveyInfo(Long surveyId, String welcomeMsg, String completeMsg) {
		JsonObject res = new JsonObject();
		
		try {
			Optional<Surveys> survey = surveysRepository.findById(surveyId); 
			Surveys entity = survey.get();
			entity.setWelcomeMsg(welcomeMsg);
			entity.setCompleteMsg(completeMsg);
			
			surveysRepository.save(entity);
			
			res.addProperty("result", "TRUE");
			res.addProperty("msg", "Message updated successfully");
		} catch(Exception e) {
			res.addProperty("result", "FALSE");
			res.addProperty("msg", "Message update failed");
		}
				
		return res.toString();
	}
}
