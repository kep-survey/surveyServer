package com.kep.surveyServer.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import com.kep.surveyServer.model.Questions;
import com.kep.surveyServer.service.SurveyServerService;

@RestController
@RequestMapping("/api/*")
@CrossOrigin
public class SurveyServerController {
	
	@Autowired
	private SurveyServerService surveyServerService;
	
	@PostMapping("/createSurvey")
	public String createSurvey(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			String surveyTitle = body.get("title").asText();
			String surveyDesc = body.get("description").asText();
			
			return surveyServerService.createSurvey(surveyTitle, surveyDesc);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	@GetMapping("/getSurveyList")
	public String getSurveyList() {
		return surveyServerService.getSurveyList();
	}
	
	@PostMapping("copySurvey")
	public String copySurvey(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			Long surveyId = body.get("surveyId").asLong();
			
			return surveyServerService.copySurvey(surveyId);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	@PostMapping("/deleteSurvey")
	public String deleteSurvey(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			Long surveyId = body.get("surveyId").asLong();
			
			return surveyServerService.deleteSurvey(surveyId);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	@GetMapping("/getSurvey")
	public String getSurvey(@RequestParam Long surveyId) {
		return surveyServerService.getSurvey(surveyId);
	}
	
	@PostMapping("/saveSurvey")
	public String saveSurvey(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			Long surveyId = body.get("surveyId").asLong();
			Questions[] temp = objectMapper.convertValue(body.get("questions"), Questions[].class);
			List<Questions> questions = Arrays.asList(temp);
			
			return surveyServerService.saveSurvey(surveyId, questions);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	@GetMapping("/getSurveyInfo")
	public String getSurveyInfo(@RequestParam Long surveyId) {
		return surveyServerService.getSurveyInfo(surveyId);
	}
	
	@PostMapping("/setSurveyInfo")
	public String setSurveyInfo(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);

			Long surveyId = body.get("surveyId").asLong();
			String title = body.get("title").asText();
			String description = body.get("description").asText();
			
			return surveyServerService.setSurveyInfo(surveyId, title, description);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	/* 설문 배포 :: 설문 상태 조회 */
	@GetMapping("/getSurveyDeploy")
	public String getSurveyDeploy(@RequestParam Long surveyId) {
		return surveyServerService.getSurveyDeploy(surveyId);
	}
	
	/* 설문 배포 :: 설문 환영/완료 메시지 저장 */
	@PostMapping("/setSurveyMsg")
	public String setSurveyMsg(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			
			String flag = body.get("flag").asText();
			Long surveyId = body.get("surveyId").asLong();
			String welcomeMsg, completeMsg;
			
			if (flag.equals("welcome")) {
				welcomeMsg = body.get("welcomeMsg").asText();
				completeMsg = "";
			} else if (flag.equals("complete")){
				welcomeMsg = "";
				completeMsg = body.get("completeMsg").asText();
			} else {
				welcomeMsg = "";
				completeMsg = "";
			}
			
			return surveyServerService.setSurveyMsg(flag, surveyId, welcomeMsg, completeMsg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	/* 설문 배포 :: 설문 상태 관리 */
	@PostMapping("/openSurvey")
	public String openSurvey(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			
			Long surveyId = body.get("surveyId").asLong();
			int status = body.get("status").asInt();
			
			return surveyServerService.openSurvey(surveyId, status);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	/* 설문 현황 :: 설문 현황 리스트 가져오기 */
	@GetMapping("/getSurveyStatus")
	public String getSurveyStatus(@RequestParam Long registerId) {
		return surveyServerService.getSurveyStatus(registerId);
	}
	
	/* 설문 결과 :: 유저 응답 리스트 가져오기 */
	@GetMapping("/getSurveyResultList")
	public String getSurveyResultList(@RequestParam Long surveyId) {
		return surveyServerService.getSurveyResultList(surveyId);
	}
	
	/* 설문 결과 :: 결과 분석 데이터 가져오기 */
	@GetMapping("/getSurveyResultAnalysis")
	public String getSurveyResultAnalysis(@RequestParam Long surveyId) {
		return surveyServerService.getSurveyResultAnalysis(surveyId);
	}
	
	/* 설문 결과 :: 상세 결과 조회 */
	@GetMapping("/getSurveyResultDetail")
	public String getSurveyResultDetail(@RequestParam Long surveyId, @RequestParam String botUserId) {
		return surveyServerService.getSurveyResultDetail(surveyId, botUserId);
	}
	
}
