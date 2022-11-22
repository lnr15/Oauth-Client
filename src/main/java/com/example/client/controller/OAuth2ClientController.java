package com.example.client.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.client.model.TokenDetails;
import com.example.client.model.UserProfile;

@Controller
public class OAuth2ClientController {
	
	static Logger logger = LoggerFactory.getLogger(OAuth2ClientController.class);
	
	@Value("${hostIp}")
	private String  hostIp;
	
	@GetMapping("/index")
	public String getIndexPage() {
		return "index";
	}
	
	@RequestMapping("/error")
	@ResponseBody
	public String getErrorPath() {
		return "<center><h1>Something went wrong</h1></center>";
	}
	
	@GetMapping("/auth_code")
	public String userProfile(@RequestParam("code")String code, Model model) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("clientapp", "654321");
		try {
		RequestEntity<Void> reqEntity = RequestEntity.post(new URI("http://"+hostIp+":5656/oauth/token"+"?code="+code+"&scope=read"+"&grant_type=authorization_code")).headers(headers).build();
		ResponseEntity<TokenDetails> re = new RestTemplate().exchange(reqEntity, TokenDetails.class);
		TokenDetails td = re.getBody();
		logger.info("Access Token received from OAuth Server : "+td.getAccess_token());
		logger.info("Refresh Token received from OAuth Server : "+ td.getRefresh_token());
		
		HttpHeaders headers1 = new HttpHeaders();
		headers1.setBearerAuth(td.getAccess_token());
		
		RequestEntity<Void> reqEntity1 = RequestEntity.get(new URI("http://"+hostIp+":5656/api/users/me")).headers(headers1).build();
		ResponseEntity<UserProfile> re1 = new RestTemplate().exchange(reqEntity1, UserProfile.class);
		UserProfile userProfile = re1.getBody();
		model.addAttribute("userProfile", userProfile);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "show";
	}

}
