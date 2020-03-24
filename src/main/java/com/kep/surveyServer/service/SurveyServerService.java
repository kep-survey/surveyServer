package com.kep.surveyServer.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

@Service
public class SurveyServerService {
	
	@Autowired
	private SurveysRepository surveysRepository;
	
	@Autowired
	private SurveyHistoryRepository surveyHistoryRepository;
	
	@Autowired
	private QuestionsRepository questionsRepository;
	
	@Autowired
	private OptionsRepository optionsRepository;
	
	@Autowired
	private AnswersRepository answersRepository;
	
	/* 설문 배포 :: 설문 상태 조회 */
	public String getSurveyInfo(@RequestParam Long surveyId) {
		JsonObject res = new JsonObject();
		
		try {
			Optional<Surveys> survey = surveysRepository.findById(surveyId);
			Surveys entity = survey.get();
			
			res.addProperty("welcomeMsg", entity.getWelcomeMsg());
			res.addProperty("completeMsg", entity.getCompleteMsg());
			res.addProperty("status", entity.getStatus());
		} catch (Exception e ) {
			res.addProperty("result", "false");
			res.addProperty("msg", "Message update failed");
		}
		
		return res.toString();
	}
	
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
	public String openSurvey(Long surveyId, int status) {
		JsonObject res = new JsonObject();
		
		try {
			SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
			Date time = new Date();
			
			Optional<Surveys> survey = surveysRepository.findById(surveyId); 
			Surveys entity = survey.get();

			if (status == 2) {
				entity.setStatus(status);
				
				String startDate = format.format(time);
				entity.setStartDatetime(Timestamp.valueOf(startDate));
				entity.setEndDatetime(null);
			} else if (status == 3){
				entity.setStatus(status);
				
				String endDate = format.format(time);
				entity.setEndDatetime(Timestamp.valueOf(endDate));
			} else {
				throw new Exception();
			}
			
			surveysRepository.save(entity);
			
			res.addProperty("result", "true");
			res.addProperty("msg", "Deployment finished successfully");
		} catch(Exception e) {
			e.printStackTrace();
			res.addProperty("result", "false");
			res.addProperty("msg", "Deployment failed");
		}
				
		return res.toString();
	}
	
	/* 설문 현황 :: 설문 현황 리스트 가져오기 */
	public String getSurveyStatus(Long registerId) {
		JsonObject res = new JsonObject();
		JsonArray statusList = new JsonArray();
		
		List<Surveys> surveys = surveysRepository.findByStatusNotAndRegistersId(1, registerId);
		
		for (int index = 0; index < surveys.size(); index++) {
			JsonObject item = new JsonObject();
			Surveys survey = surveys.get(index);
			Long surveyId = survey.getId();

			try {
				List<SurveyHistory> surveyHistory = surveyHistoryRepository.findByIdSurveyId(surveyId);
				
				item.addProperty("surveyId", surveyId);
				item.addProperty("surveyName", survey.getTitle());
				item.addProperty("count", surveyHistory.size());
				item.addProperty("startDate", survey.getStartDatetime().toString());
				
				try {
					item.addProperty("endDate", survey.getEndDatetime().toString());
				} catch (NullPointerException e) {
					item.addProperty("endDate", "");
				}
				
				statusList.add(item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		res.add("statusList", statusList);
		
		return res.toString();
	}
	
	/* 설문 결과 :: 응답 유저 리스트 가져오기 */
	public String getSurveyResultList(Long surveyId) {
		JsonObject res = new JsonObject();
		JsonArray resultList = new JsonArray();
		
		List<SurveyHistory> surveyHistoryList = surveyHistoryRepository.findByIdSurveyId(surveyId);
		
		for (int index = 0; index < surveyHistoryList.size(); index++) {
			JsonObject result = new JsonObject();
			SurveyHistory entity = surveyHistoryList.get(index);
			
			String botUserId = entity.getId().getBotUserId();
			String participationTime = entity.getParticipationTime().toString();
			
			result.addProperty("botUserId", botUserId);
			result.addProperty("participationTime", participationTime);
			
			resultList.add(result);
		}
		
		res.add("resultList", resultList);
		
		return res.toString();
	}
	
	/* 설문 결과 :: 상세 결과 조회 */
	public String getSurveyResultDetail(@RequestParam Long surveyId, @RequestParam String botUserId) {
		JsonObject res = new JsonObject();
		JsonArray result = new JsonArray();
		
		List<Questions> questionList = questionsRepository.findBySurveysIdOrderBySurveyOrder(surveyId);
		
		for (int index = 0; index < questionList.size(); index++) {			
			JsonArray options = new JsonArray();
			Questions questionItem = questionList.get(index);
			
			String questionTitle = questionItem.getDescription();
			
			List<Options> optionList = optionsRepository.findByQuestionsIdOrderByOptionOrder(questionItem.getId());
			Answers answer = answersRepository.findByUsersBotUserIdAndQuestionsId(botUserId, questionItem.getId());
			
			if (questionItem.getType().equals("choice")) {
				JsonObject resultItem = new JsonObject();
				
				for (int second_index = 0; second_index < optionList.size(); second_index++) {
					JsonObject option = new JsonObject();
					Options optionItem = optionList.get(second_index);
					
					option.addProperty("id", optionItem.getId());
					option.addProperty("content", optionItem.getOption());
					
					options.add(option);
				}
				
				resultItem.addProperty("type", "choice");
				resultItem.addProperty("question", questionTitle);
				resultItem.add("options", options);
				resultItem.addProperty("answer", answer.getAnswer());
				
				result.add(resultItem);
			} else {
				JsonObject resultItem = new JsonObject();
				
				resultItem.addProperty("type", "text");
				resultItem.addProperty("question", questionTitle);
				resultItem.addProperty("answer", answer.getAnswer());
				
				result.add(resultItem);
			}
		}
		
		res.add("result", result);
		
		return res.toString();
	}
}
