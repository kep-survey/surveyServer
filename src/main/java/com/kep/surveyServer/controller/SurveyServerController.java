package com.kep.surveyServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kep.surveyServer.service.SurveyServerService;

@RestController
@RequestMapping("/api/*")
public class SurveyServerController {
	
	@Autowired
	private SurveyServerService surveyServerService;
	
	@PostMapping("/setSurveyInfo")
	public String setSurveyInfo(@RequestBody String req) {
		return "";
	}
}
