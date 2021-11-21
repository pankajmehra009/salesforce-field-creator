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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
	
	@RequestMapping("/api/deletesession")
	public void saveUser() {
		codeService.deleteSession();
	}
}
