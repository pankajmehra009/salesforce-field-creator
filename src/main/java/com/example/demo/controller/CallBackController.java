package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CodeDto;
import com.example.demo.dto.FieldDTO;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Code;
import com.example.demo.repository.CodeRepository;
import com.example.demo.service.CodeService;
import com.example.demo.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("")
public class CallBackController {
	
	@Autowired
	CodeService codeService;
	
	
	@RequestMapping("/code/getaccesscode")
	public List<CodeDto> getCode() {
		System.out.println("code : ");
		return codeService.getAccessCode();
	}
	
	@RequestMapping("/api/sobjects")
	public String getSobjectList(@RequestParam String code,@RequestParam String domain) {
		
		URL url;
		try {
			String urlSF = domain + "/services/data/v53.0/sobjects/";
			
			url = new URL(urlSF); 
			
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("GET"); // PUT is another valid option
			http.setDoOutput(true);
			http.setRequestProperty ("Authorization", "Bearer "+code);
	        Reader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
	        
			StringBuilder builder = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	        	builder.append((char)c);
	        
	        System.out.println(builder.toString());
	        return builder.toString();
			 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}
	
	@RequestMapping(value="/api/sobjects/createfield", method= RequestMethod.POST)
	public String getCreateField(@RequestBody FieldDTO fDto) throws IOException {
		System.out.println(fDto.getField());
		System.out.println(fDto.getSession());
		System.out.println(fDto.getDomain());
		URL url;
		OutputStreamWriter osw = null;
		OutputStream os = null;
		try {
			String urlSF = fDto.getDomain() + "/services/data/v42.0/tooling/sobjects/CustomField/";
			
	        url = new URL(urlSF);
	        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	        httpCon.setDoOutput(true);
	        httpCon.setDoInput(true);
	        httpCon.setRequestProperty("Content-Type", "application/json");
	        httpCon.setRequestMethod("POST");
	        httpCon.setRequestProperty ("Authorization", "Bearer "+fDto.getSession());
	        httpCon.setAllowUserInteraction(true); 
	        os = httpCon.getOutputStream();
	        osw = new OutputStreamWriter(os, "UTF-8");    
	        osw.write(fDto.getField());
	        osw.flush();

	        osw.close();
	        os.close();  //don't forget to close the OutputStream 
	        
	        httpCon.connect();
	        boolean isError = (httpCon.getResponseCode() >= 400);
	        InputStream is = isError ? httpCon.getErrorStream() : httpCon.getInputStream();
	       
        	//read the inputstream and print it
	        String result;
	        
	        BufferedInputStream bis = new BufferedInputStream(is);
	        ByteArrayOutputStream buf = new ByteArrayOutputStream();
	        int result2 = bis.read();
	        while(result2 != -1) {
	            buf.write((byte) result2);
	            result2 = bis.read();
	        }
	        result = buf.toString();
	        System.out.println(result);
	        
	        return result;
	       
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		 
		return "error";
	}
	
	@RequestMapping(value="/api/createlead", method= RequestMethod.POST)
	public String getCreateLead(@RequestBody LeadDTO fDto) throws IOException {
		System.out.print("><>>>>> "+fDto.id);
		String userInfo = getUserDetails(fDto);
		ObjectMapper mapper = new ObjectMapper();
		final ObjectNode node = new ObjectMapper().readValue(userInfo, ObjectNode.class);
		String fullname = node.get("display_name").asText();
		String firstname = fullname.split(" ")[0];
		String lastname = fullname.split(" ")[1];
		String email = node.get("email").asText();
		System.out.print(email);
		System.out.print(fullname);
		System.out.print(firstname);
		URL url;
		OutputStreamWriter osw = null;
		OutputStream os = null;
		try {
			String urlSF = "https://webto.salesforce.com/servlet/servlet.WebToLead?encoding=UTF-8";
			String lead = "oid=00D09000009DF9a"
					+ "&retURL=http%3A%2F%2Fhappychef-io.herokuapp.com%2Fhome"
					+ "&first_name="+encodeValue(firstname)
					+ "&last_name="+encodeValue(lastname)
					+ "&Fields_created__c="+encodeValue(lastname)
					+ "&Company="+encodeValue("Web App")
					+ "&LeadSource="+encodeValue("Web App")
					+ "&email="+encodeValue(email)
					+ "&description="+encodeValue("Fields Created : ")+ fDto.count
					+ "&submit=Submit";
	        url = new URL(urlSF);
	        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	        httpCon.setDoOutput(true);
	        httpCon.setDoInput(true);
	        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        httpCon.setRequestMethod("POST");
	        //httpCon.setRequestProperty ("Authorization", "Bearer "+fDto.sessionId);
	        //httpCon.setAllowUserInteraction(true); 
	        os = httpCon.getOutputStream();
	        osw = new OutputStreamWriter(os, "UTF-8");    
	        osw.write(lead);
	        osw.flush();

	        osw.close();
	        os.close();  //don't forget to close the OutputStream 
	        
	        httpCon.connect();
	        boolean isError = (httpCon.getResponseCode() >= 400);
	        InputStream is = isError ? httpCon.getErrorStream() : httpCon.getInputStream();
	       
        	//read the inputstream and print it
	        String result;
	        
	        BufferedInputStream bis = new BufferedInputStream(is);
	        ByteArrayOutputStream buf = new ByteArrayOutputStream();
	        int result2 = bis.read();
	        while(result2 != -1) {
	            buf.write((byte) result2);
	            result2 = bis.read();
	        }
	        result = buf.toString();
	        System.out.println(result);
	        
	        return result;
	       
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		 
		return "error";
	}
	
	private String encodeValue(String value) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}
	
	public String getUserDetails(LeadDTO fDto) throws IOException {
		System.out.print(fDto.id);
		URL url;
		OutputStreamWriter osw = null;
		OutputStream os = null;
		try {
			String urlSF = fDto.id;
			
	        url = new URL(urlSF);
	        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	        httpCon.setDoOutput(true);
	        httpCon.setDoInput(true);
	        httpCon.setRequestProperty("Content-Type", "application/json");
	        httpCon.setRequestMethod("GET");
	        httpCon.setRequestProperty ("Authorization", "Bearer "+fDto.sessionId);
	        httpCon.setAllowUserInteraction(true); 
	        os = httpCon.getOutputStream();
	        osw = new OutputStreamWriter(os, "UTF-8");    
	        osw.flush();
	        osw.close();
	        os.close();  //don't forget to close the OutputStream 
	        
	        httpCon.connect();
	        boolean isError = (httpCon.getResponseCode() >= 400);
	        InputStream is = isError ? httpCon.getErrorStream() : httpCon.getInputStream();
	       
        	//read the inputstream and print it
	        String result;
	        
	        BufferedInputStream bis = new BufferedInputStream(is);
	        ByteArrayOutputStream buf = new ByteArrayOutputStream();
	        int result2 = bis.read();
	        while(result2 != -1) {
	            buf.write((byte) result2);
	            result2 = bis.read();
	        }
	        result = buf.toString();
	        System.out.println("><> "+result);
	        
	        return result;
	       
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		 
		return "error";
	}
	
	public static class LeadDTO{
		public String count;
		public String id;
		public String sessionId;
		public String domain;
		
	}
	
	@RequestMapping("/api/deletesession")
	public void saveUser() {
		codeService.deleteSession();
	}
}
