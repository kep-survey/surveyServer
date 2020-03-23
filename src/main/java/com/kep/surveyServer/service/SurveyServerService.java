package com.kep.surveyServer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kep.surveyServer.model.Answers;
import com.kep.surveyServer.model.Options;
import com.kep.surveyServer.model.Questions;
import com.kep.surveyServer.model.SurveyHistory;
import com.kep.surveyServer.model.Surveys;
import com.kep.surveyServer.repository.AnswersRepository;
import com.kep.surveyServer.repository.OptionsRepository;
import com.kep.surveyServer.repository.QuestionsRepository;
import com.kep.surveyServer.repository.SurveyHistoryRepository;
import com.kep.surveyServer.repository.SurveysRepository;

import lombok.AllArgsConstructor;

@Service
public class SurveyServerService {
	
	@Autowired
	private SurveysRepository surveysRepository;
	private SurveyHistoryRepository surveyHistoryRepository;
	private QuestionsRepository questionsRepository;
	private OptionsRepository optionsRepository;
	private AnswersRepository answersRepository;
	
	/* 설문 배포 :: 설문 환영/완료 메시지 저장 */
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
	public String getSurveyStatus(Long registerId) {
		JsonObject res = new JsonObject();
		JsonArray statusList = new JsonArray();
		
		List<Surveys> surveys = surveysRepository.findByOpenTrueAndRegisters(registerId);
		
		for (int index = 0; index < surveys.size(); index++) {
			JsonObject item = new JsonObject();
			Surveys survey = surveys.get(index);
			
			List<SurveyHistory> surveyHistory = surveyHistoryRepository.findBySurveyHistoryPKSurveyId(survey.getId());
			
			item.addProperty("surveyId", survey.getId());
			item.addProperty("surveyName", survey.getTitle());
			item.addProperty("count", surveyHistory.size());
			item.addProperty("startDate", survey.getStartDatetime().toString());
			item.addProperty("endTime", survey.getEndDatetime().toString());
			
			statusList.add(item);
		}
		
		res.add("statusList", statusList);
		
		return res.toString();
	}
	
	/* 설문 결과 :: 결과 상세 가져오기 */
	public String getSurveyResult(Long surveyId) {
		JsonObject res = new JsonObject();
		JsonArray resultList = new JsonArray();
		
		List<SurveyHistory> surveyHistoryList = surveyHistoryRepository.findBySurveyHistoryPKSurveyId(surveyId);
		List<Questions> questionList = questionsRepository.findBySurveysOrderBySurveyOrder(surveyId);
		
		for (int index = 0; index < surveyHistoryList.size(); index++) {
			JsonObject result = new JsonObject();
			JsonArray resultItemList = new JsonArray();
			JsonArray options = new JsonArray();
			Questions questionItem = questionList.get(index);
			
			String botUserId = surveyHistoryList.get(index).getSurveyHistoryPK().getBotUserId();
			String questionTitle = questionItem.getDescription();
			
			List<Options> optionList = optionsRepository.findByQuestionsOrderByOptionOrder(questionItem.getId());
			Answers answer = answersRepository.findByUsersAndQuestions(botUserId, questionItem.getId());
			
			if (questionItem.getType().equals("choice")) {
				JsonObject resultItem = new JsonObject();
				
				for (int second_index = 0; second_index < optionList.size(); second_index++) {
					JsonObject option = new JsonObject();
					Options optionItem = optionList.get(second_index);
					
					option.addProperty("id", optionItem.getId());
					option.addProperty("content", optionItem.getOption());
					
					options.add(option);
				}
				
				resultItem.addProperty("question", questionTitle);
				resultItem.add("options", options);
				resultItem.addProperty("answer", answer.getAnswer());
				
				resultItemList.add(resultItem);
			} else {
				JsonObject resultItem = new JsonObject();
				
				resultItem.addProperty("question", questionTitle);
				resultItem.addProperty("answer", answer.getAnswer());
				
				resultItemList.add(resultItem);
			}
			
			result.addProperty("botUserId", botUserId);
			result.add("items", resultItemList);
			
			resultList.add(result);
		}
		
		res.add("resultList", resultList);
		
		return res.toString();
	}
}
