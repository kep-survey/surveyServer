package com.kep.surveyServer.service;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.kep.surveyServer.model.Registers;
import com.kep.surveyServer.repository.AnswersRepository;
import com.kep.surveyServer.repository.OptionsRepository;
import com.kep.surveyServer.repository.QuestionsRepository;
import com.kep.surveyServer.repository.SurveyHistoryRepository;
import com.kep.surveyServer.repository.SurveysRepository;
import com.kep.surveyServer.repository.RegistersRepository;

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
			
			res.addProperty("status", survey.getStatus());
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
	
	public String copySurvey(Long surveyId) {
		JsonObject res = new JsonObject();
		
		try {
			Surveys survey = surveysRepository.findById(surveyId).get();
			List<Questions> questions = questionsRepository.findBySurveysId(surveyId);
			List<Options> options = optionsRepository.findBySurveysId(surveyId);
			
			Surveys surveyEntity = new Surveys().builder()
												.register(survey.getRegisters())
												.sumQuestions(survey.getSumQuestions())
												.title(survey.getTitle())
												.description(survey.getDescription())
												.welcomeMsg(survey.getWelcomeMsg())
												.completeMsg(survey.getCompleteMsg())
												.build();
			Surveys newSurvey = surveysRepository.save(surveyEntity);
			
			for (int index = 0; index < questions.size(); index++) {
				Questions questionEntity = questions.get(index);
				Questions newQuestion = new Questions().builder()
															.surveys(newSurvey)
															.type(questionEntity.getType())
															.questionOrder(questionEntity.getQuestionOrder())
															.description(questionEntity.getDescription())
															.build();
				
				Questions newQuestionEntity = questionsRepository.save(newQuestion);
				
				List<Options> newOptions = new ArrayList<Options>();
				for (int second_index = 0; second_index < questions.size(); second_index++) {
					Options entity = options.get(second_index);
					Options newOption = new Options().builder()
																.surveys(newSurvey)
																.questions(newQuestionEntity)
																.optionOrder(entity.getOptionOrder())
																.option(entity.getOption())
																.build();
					newOptions.add(newOption);
				}
				optionsRepository.saveAll(newOptions);
			}
			
			res.addProperty("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			
			res.addProperty("result", false);
			res.addProperty("msg", "해당하는 설문이 존재하지 않습니다. 다시 시도해주세요");
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
			List<Questions> questions = questionsRepository.findBySurveysIdOrderByQuestionOrder(survey.getId());
			
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
	
	/* 설문 배포 :: 설문 상태 조회 */
	public String getSurveyDeploy(@RequestParam Long surveyId) {
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
	public String setSurveyMsg(String flag, Long surveyId, String welcomeMsg, String completeMsg) {
		JsonObject res = new JsonObject();
		
		try {
			Optional<Surveys> survey = surveysRepository.findById(surveyId); 
			Surveys entity = survey.get();
			
			if (flag.equals("welcome")) {
				entity.setWelcomeMsg(welcomeMsg);
			} else if (flag.equals("complete")){
				entity.setCompleteMsg(completeMsg);
			} else {
				throw new Exception();
			}
			
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
				List<SurveyHistory> surveyHistory = surveyHistoryRepository.findByIdSurveyIdAndQuestionOrderIsNot(surveyId, 0);
				
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
		
		Optional<Surveys> optionalSurvey = surveysRepository.findById(surveyId);
		Surveys survey = optionalSurvey.get();
		
		List<SurveyHistory> surveyHistoryList = surveyHistoryRepository.findByIdSurveyIdAndQuestionOrderIsNot(surveyId, 0);
		
		for (int index = 0; index < surveyHistoryList.size(); index++) {			
			JsonObject result = new JsonObject();
			SurveyHistory entity = surveyHistoryList.get(index);
			
			String botUserId = entity.getId().getBotUserId();
			String participationTime = entity.getParticipationTime().toString();
			
			result.addProperty("botUserId", botUserId);
			result.addProperty("participationTime", participationTime);
			
			resultList.add(result);
		}
		
		res.addProperty("title", survey.getTitle());
		res.add("resultList", resultList);
		
		return res.toString();
	}
	
	/* 설문 결과 :: 결과 분석 데이터 가져오기 */
	public String getSurveyResultAnalysis(@RequestParam Long surveyId) {
		JsonObject res = new JsonObject();
		JsonArray analysisList = new JsonArray();

		List<Questions> questions = questionsRepository.findBySurveysId(surveyId);
		for (int index = 0; index < questions.size(); index++) {
			JsonObject obj = new JsonObject();
			JsonObject datacollection = new JsonObject();
			JsonArray labels = new JsonArray();
			JsonArray datasets = new JsonArray();
			JsonObject dataset = new JsonObject();
			dataset.addProperty("label", "응답수");
			dataset.addProperty("backgroundColor", "#FAE100");
			dataset.addProperty("pointBackgroundColor", "white");
			dataset.addProperty("borderWidth", 0);
			dataset.addProperty("pointBorderColor", "249EBF");
			
			JsonArray data = new JsonArray();
			
			Questions question = questions.get(index);
			
			if (question.getType().equals("choice")) {
				List<Options> options = optionsRepository.findByQuestionsIdOrderByOptionOrder(question.getId());
				for (int second_index = 0; second_index < options.size(); second_index++) {
					Options option = options.get(second_index);
					labels.add(option.getOption());
					
					int count = answersRepository.findByQuestionsIdAndAnswerEquals(question.getId(), option.getOption()).size();
					data.add(count);
				}
				
				dataset.add("data", data);
				datasets.add(dataset);
				
				datacollection.add("labels", labels);
				datacollection.add("datasets", datasets);
				
				obj.addProperty("question", question.getDescription());
				obj.addProperty("type", question.getType());
				obj.add("datacollection", datacollection);
				
				analysisList.add(obj);
			} else {
				List<Answers> answers = answersRepository.findByQuestionsId(question.getId());
				JsonArray answerList = new JsonArray();
				
				for (int second_index = 0; second_index < answers.size(); second_index++) {
					answerList.add(answers.get(second_index).getAnswer());
				}
				
				obj.addProperty("question", question.getDescription());
				obj.addProperty("type", question.getType());
				obj.add("datacollection", answerList);
				
				analysisList.add(obj);
			}
		}
		
		res.add("analysisList", analysisList);
		
		return res.toString();
	}
	
	/* 설문 결과 :: 상세 결과 조회 */
	public String getSurveyResultDetail(@RequestParam Long surveyId, @RequestParam String botUserId) {
		JsonObject res = new JsonObject();
		JsonArray result = new JsonArray();
		
		Optional<Surveys> survey = surveysRepository.findById(surveyId);
		Surveys entity = survey.get();
		
		List<Questions> questionList = questionsRepository.findBySurveysIdOrderByQuestionOrder(surveyId);
		
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
				
				try {
					resultItem.addProperty("answer", answer.getAnswer());
				} catch (NullPointerException e) {
					resultItem.addProperty("answer", "");
				}
				
				result.add(resultItem);
			} else {
				JsonObject resultItem = new JsonObject();
				
				resultItem.addProperty("type", "text");
				resultItem.addProperty("question", questionTitle);
				
				try {
					resultItem.addProperty("answer", answer.getAnswer());
				} catch (NullPointerException e) {
					resultItem.addProperty("answer", "");
				}
				
				result.add(resultItem);
			}
		}
		
		res.addProperty("title", entity.getTitle());
		res.add("result", result);
		
		return res.toString();
	}
}
