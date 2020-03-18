package com.kep.surveyServer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kep.surveyServer.model.SurveyHistory;
import com.kep.surveyServer.model.Surveys;
import com.kep.surveyServer.repository.SurveyHistoryRepository;
import com.kep.surveyServer.repository.SurveysRepository;

@Service
public class SurveyServerService {
	
	@Autowired
	private SurveysRepository surveysRepository;
	private SurveyHistoryRepository surveyHistoryRepository;
	
	/* 설문 배포 :: 설문 환영/완료 메시지 저장*/
	public String setSurveyInfo(Long surveyId, String welcomeMsg, String completeMsg) {
		JsonObject res = new JsonObject();
		
		try {
			Optional<Surveys> survey = surveysRepository.findById(surveyId); 
			Surveys entity = survey.get();
			entity.setWelcomeMsg(welcomeMsg);
			entity.setCompleteMsg(completeMsg);
			
			surveysRepository.save(entity);
			
			res.addProperty("result", "true");
			res.addProperty("msg", "Message updated successfully");
		} catch(Exception e) {
			res.addProperty("result", "false");
			res.addProperty("msg", "Message update failed");
		}
				
		return res.toString();
	}
	
	/* 설문 배포 :: 설문 상태 관리 */
	public String openSurvey(Long surveyId, Boolean open) {
		JsonObject res = new JsonObject();
		
		try {
			Optional<Surveys> survey = surveysRepository.findById(surveyId); 
			Surveys entity = survey.get();
			entity.setOpen(open);
			
			surveysRepository.save(entity);
			
			res.addProperty("result", "true");
			res.addProperty("msg", "Deployment finished successfully");
		} catch(Exception e) {
			res.addProperty("result", "false");
			res.addProperty("msg", "Deployment failed");
		}
				
		return res.toString();
	}
	
	/* 설문 현황 :: 설문 현황 리스트 가져오기 */
	public String getSurveyStatus() {
		JsonObject res = new JsonObject();
		JsonArray statusList = new JsonArray();
		
		List<Surveys> surveys = surveysRepository.findByOpenTrue();
		
		for (int index = 0; index < surveys.size(); index++) {
			JsonObject item = new JsonObject();
			Surveys survey = surveys.get(index);
			
			List<SurveyHistory> surveyHistory = surveyHistoryRepository.findBySurveyHistoryPKSurveyId(survey.getId());
			
			item.addProperty("surveyId", survey.getId());
			item.addProperty("surveyName", survey.getTitle());
			item.addProperty("responseNum", surveyHistory.size());
			item.addProperty("surveyStartDate", survey.getStartDatetime().toString());
			item.addProperty("surveyEndTime", survey.getEndDatetime().toString());
			
			statusList.add(item);
		}
		
		res.add("statusList", statusList);
		
		return res.toString();
	}
}
