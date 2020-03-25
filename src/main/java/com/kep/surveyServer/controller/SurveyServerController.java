package com.kep.surveyServer.controller;

import java.util.ArrayList;
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

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081" }) 
@RestController
@RequestMapping("/api/*")
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
	
	@GetMapping("/getSurveyList")
	public String getSurveyList() {
		return surveyServerService.getSurveyList();
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
	
	@GetMapping("/getSurveyInfo")
	public String getSurveyInfo(@RequestParam Long surveyId) {
		ObjectMapper objectMapper = new ObjectMapper();

		return surveyServerService.getSurveyInfo(surveyId);
	}
}
