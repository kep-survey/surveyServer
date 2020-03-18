package com.kep.surveyServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.kep.surveyServer.service.SurveyServerService;

@RestController
@RequestMapping("/api/*")
public class SurveyServerController {
	
	@Autowired
	private SurveyServerService surveyServerService;
	
	@PostMapping("/setSurveyInfo")
	public String setSurveyInfo(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			
			Long surveyId = body.get("surveyId").asLong();
			String welcomeMsg = body.get("welcomeMsg").asText();
			String completeMsg = body.get("completeMsg").asText();
			
			return surveyServerService.setSurveyInfo(surveyId, welcomeMsg, completeMsg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	@PostMapping("/openSurvey")
	public String openSurvey(@RequestBody String req) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode body = objectMapper.readValue(req, JsonNode.class);
			
			Long surveyId = body.get("surveyId").asLong();
			Boolean open = body.get("open").asBoolean();
			
			return surveyServerService.openSurvey(surveyId, open);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			JsonObject res = new JsonObject();
			res.addProperty("result", "false");
			res.addProperty("msg", "Json processing error occurred");
			
			return res.toString();
		}
	}
	
	@GetMapping("/getSurveyStatus")
	public String getSurveyStatus() {
		return surveyServerService.getSurveyStatus();
	}
}
