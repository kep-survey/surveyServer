package com.kep.surveyServer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kep.surveyServer.model.Options;
import com.kep.surveyServer.model.Questions;
import com.kep.surveyServer.model.Registers;
import com.kep.surveyServer.model.Surveys;
import com.kep.surveyServer.repository.AnswersRepository;
import com.kep.surveyServer.repository.OptionsRepository;
import com.kep.surveyServer.repository.QuestionsRepository;
import com.kep.surveyServer.repository.RegistersRepository;
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
	
	@Autowired
	private RegistersRepository registerRepository;
	
	public String createSurvey(String surveyTitle, String surveyDesc) {
		JsonObject res = new JsonObject();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			Surveys survey = new Surveys();
//			Surveys survey = Surveys.builder().title(surveyTitle).description(surveyDesc).registers(register).build();
			survey.setTitle(surveyTitle);
			survey.setDescription(surveyDesc);
			survey.setRegisters(register);
			
			surveysRepository.save(survey);
			
			res.addProperty("result", true);
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Create Survey Faild");
		}

		return res.toString();
	}
	
	public String getSurveyList() {
		JsonObject res = new JsonObject();
		JsonArray list = new JsonArray();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			List<Surveys> surveyList = surveysRepository.findByRegisters(register);
			
			for(Surveys survey : surveyList) {
				JsonObject surveyObject = new JsonObject();
				
				surveyObject.addProperty("id", survey.getId());
				surveyObject.addProperty("title", survey.getTitle());
				surveyObject.addProperty("description", survey.getDescription());
				surveyObject.addProperty("sumAnswer", 0);
				
				list.add(surveyObject);
			}
			res.add("list", list);
			res.addProperty("result", true);
			
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Get Survey List Faild");
		}
		return res.toString();
	}
	
	public String getSurveyInfo(Long surveyId) {
		JsonObject res = new JsonObject();
		JsonObject info = new JsonObject();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			Surveys survey = surveysRepository.findByIdAndRegisters(surveyId, register).get();
			
			info.addProperty("title", survey.getTitle());
			info.addProperty("description", survey.getDescription());
			
			res.addProperty("result", true);
			res.add("info", info);
			
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Get Survey Info Faild");
		}
		return res.toString();
	}
	
	public String setSurveyInfo(Long surveyId, String title, String description) {
		JsonObject res = new JsonObject();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			Surveys survey = surveysRepository.findByIdAndRegisters(surveyId, register).get();
			survey.setTitle(title);
			survey.setDescription(description);
			
			surveysRepository.save(survey);
			
			res.addProperty("result", true);
			
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Set Survey Info Faild");
		}
		return res.toString();
	}
	
	public String deleteSurvey(Long surveyId) {
		JsonObject res = new JsonObject();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			// 설문 삭제
			surveysRepository.deleteByIdAndRegisters(surveyId, register);
			res.addProperty("result", true);
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Delete Survey Faild");
		}

		return res.toString();
	}

	
	public String getSurvey(Long surveyId) {
		JsonObject res = new JsonObject();
		JsonArray list = new JsonArray();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			Surveys survey = surveysRepository.findByIdAndRegisters(surveyId, register).get();
			
			// 질문 리스트 불러오기
			List<Questions> questions = questionsRepository.findBySurveysOrderByQuestionOrder(survey);
			
			for(Questions question : questions) {
				JsonObject questionObject = new JsonObject();
				
				questionObject.addProperty("id", question.getId());
				questionObject.addProperty("type", question.getType());
				questionObject.addProperty("questionOrder", question.getQuestionOrder());
				questionObject.addProperty("description", question.getDescription());
				
				// 옵션 불러오기
				// 객관식일 경우
				if(question.getType().equals("choice")) {
					List<Options> options = question.getOptions();
					
					JsonArray optionArray = new JsonArray();
					
					for(Options option : options) {
						JsonObject optionObject = new JsonObject();
						
						optionObject.addProperty("id", option.getId());
						optionObject.addProperty("option", option.getOption());
						optionObject.addProperty("optionOrder", option.getOptionOrder());
						
						optionArray.add(optionObject);
					}
					
					questionObject.add("options", optionArray);
				}
				
				list.add(questionObject);
			}
			res.add("list", list);
			res.addProperty("result", true);
			
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Get Survey Project Faild");
		}
		return res.toString();
	}
	
	public String saveSurvey(Long surveyId, List<Questions> questions) {
		JsonObject res = new JsonObject();
		
		try {
			Registers register = registerRepository.findById((long) 1).get();
			
			Surveys survey = surveysRepository.findByIdAndRegisters(surveyId, register).get();
			
			// 설문 질문 일괄 삭제
			questionsRepository.deleteBySurveys(survey);
			
			// 데이터 저장하기
			for(Questions question : questions) {
				question.setId(null);
				question.setSurveys(survey);
				questionsRepository.save(question);
				
				// 객관식일 경우
				if(question.getType().equals("choice") && question.getOptions() != null) {
					for(Options option : question.getOptions()) {
						option.setId(null);
						option.setSurveys(survey);
						option.setQuestions(question);
						
						optionsRepository.save(option);
					}
				}
			}
			res.addProperty("result", true);
			
		} catch (Exception e ) {
			e.printStackTrace();
			res.addProperty("result", false);
			res.addProperty("msg", "Save Survey Project Faild");
		}
		return res.toString();
	}
	
//	public String saveSurvey(Long surveyId, List<Questions> questions) {
//		JsonObject res = new JsonObject();
//		JsonArray list = new JsonArray();
//		
//		try {
//			Registers register = registerRepository.findById((long) 1).get();
//			
//			Surveys survey = surveysRepository.findByIdAndRegisters(surveyId, register).get();
//			
//			// 실제 db에 담겨있는 질문 리스트
//			List<Questions> questionsEntity = questionsRepository.findBySurveysOrderByQuestionOrder(survey);
//			
//			// 기존의 데이터와의 무결성 검사를 위함
//			HashMap<Long, String> entityQuestionsIdTypeList = new HashMap<Long, String>();
//			
//			for(Questions question : questionsEntity) {
//				entityQuestionsIdTypeList.put(question.getId(), question.getType());
//			}
//			
//			List<Long> idList = new ArrayList<>(entityQuestionsIdTypeList.keySet());
//			
//			// 데이터 저장하기
//			for(Questions question : questions) {
//				if(idList.contains(question.getId())) { // 이미 존재하는 요소일경우 덮어쓰기
//					// 만약 기존의 내용과 type이 다를셩우
//					if(!question.getType().equals(entityQuestionsIdTypeList.get(question.getId()))) {
//						questionsRepository.deleteById(question.getId());
//					}
//					
//					questionsRepository.save(question);
//					
//					// 객관식 options이 있을경우
//					if(question.getType().equals("choice") && question.getOptions() != null && question.getOptions().size() > 0) {
//						
//						for(Options option : question.getOptions()) {
//							
//						}
//					}
//				} else if(question.getId() <= 0) { // 추가된 요소일경우 삽입
//					question.setId(null);
//					question.setSurveys(survey);
//					questionsRepository.save(question);
//				} else { // 없어진 요소일경우 삭제
//					questionsRepository.deleteById(question.getId());
//				}
//				
//				JsonObject questionObject = new JsonObject();
//				
//				questionObject.addProperty("id", question.getId());
//				questionObject.addProperty("type", question.getType());
//				questionObject.addProperty("questionOrder", question.getQuestionOrder());
//				questionObject.addProperty("description", question.getDescription());
//				
//				// 객관식일 경우
//				if(question.getType().equals("choice")) {
//					List<Options> options = optionsRepository.findBySurveysAndQuestionsOrderByOptionOrder(survey, question);
//					
//					JsonArray optionArray = new JsonArray();
//					
//					for(Options option : options) {
//						JsonObject optionObject = new JsonObject();
//						
//						optionObject.addProperty("id", option.getId());
//						optionObject.addProperty("type", option.getOption());
//						optionObject.addProperty("optionOrder", option.getOptionOrder());
//						
//						optionArray.add(optionObject);
//					}
//				}
//				
//				list.add(questionObject);
//			}
//			res.add("list", list);
//			res.addProperty("result", true);
//			
//		} catch (Exception e ) {
//			e.printStackTrace();
//			res.addProperty("result", false);
//			res.addProperty("msg", "Save Survey Project Faild");
//		}
//		return res.toString();
//	}

}
