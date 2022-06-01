package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Code;
import com.example.demo.entity.Skill;
import com.example.demo.entity.Users;
import com.example.demo.repository.CodeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Constants;

@Controller
public class HomeController {
	@RequestMapping("/home")
	public String home() {
		return "/views/index.html"; 
	}
	
	@Autowired
	CodeRepository codeRepository;
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/oauth2/callback")
	public String home(@RequestParam String code) {
		System.out.println("code : "+code);
		Users user = userRepository.findAll().get(0);
		System.out.println("user : "+user);
		String env = user.getUserName();
		String urlSFFromDB = (env.equals("prod")) ? Constants.SALESFORCE_PROD_URI : 
							 (env.equals("sandbox"))  ? Constants.SALESFORCE_SNDBX_URI : user.getSkills().get(0).getSkillName();
		System.out.println("urlSFFromDB : "+urlSFFromDB);
		URL url;
		try {
			String urlSF = urlSFFromDB + "/services/oauth2/token?" + 
							"grant_type=authorization_code"+
							"&code="+code+
							"&client_id="+Constants.SALESFORCE_CONSUMER_KEY+
							"&client_secret="+Constants.SALESFORCE_CONSUMER_SECRET+
							"&redirect_uri="+Constants.SALESFORCE_REDIRECT_URI;

			
			url = new URL(urlSF);
			
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST"); // PUT is another valid option
			http.setDoOutput(true);

	        Reader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
	        
			StringBuilder builder = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	        	builder.append((char)c);
	        
	        Code codename= new Code(builder.toString());
			codename= codeRepository.save(codename);
			 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "/home";
	}
	
	@RequestMapping(value = "/oauth2/sflogin", method = RequestMethod.GET) 
	public ModelAndView home(@RequestParam String env,@RequestParam String customdomain) {
		userRepository.deleteAll();
		Users user= new Users(env, null);
		user.setSkills(Arrays.asList(new Skill(customdomain)));
		user= userRepository.save(user);
		System.out.println("login started : "+env);
		String urlSF = (env.equals("prod")) ? Constants.SALESFORCE_PROD_URI : 
					(env.equals("sandbox"))  ? Constants.SALESFORCE_SNDBX_URI : customdomain;
		System.out.println("login started : "+urlSF);	
		String loginURL = urlSF+"/services/oauth2/authorize?client_id="+
						  Constants.SALESFORCE_CONSUMER_KEY+"&redirect_uri="+
						  Constants.SALESFORCE_REDIRECT_URI+"&response_type=code";
		
		return new ModelAndView("redirect:" + loginURL);
	}
}
