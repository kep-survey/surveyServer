package com.kep.surveyServer;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController implements ErrorController{
	// https://jamong-icetea.tistory.com/214
	// url 직접 접슨시 vue 페이지로 다이렉션
	
	@GetMapping("/error")
	public String refireectRoot() {
		return "index.html";
	}
	
	@Override
	public String getErrorPath() {
		return "/error";
	}

}
