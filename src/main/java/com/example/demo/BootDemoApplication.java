package com.example.demo;

import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BootDemoApplication {
	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		// set variables
		setClasspath();
		System.out.println("><>> Constants.SALESFORCE_REDIRECT_URI : "+Constants.SALESFORCE_REDIRECT_URI);
		SpringApplication.run(BootDemoApplication.class, args);
	}

	@PostConstruct
	public void setupDbWithData(){
		//User user= new User("Ashish", null);
		//user.setSkills(Arrays.asList(new Skill("java"), new Skill("js")));
		//user= userRepository.save(user);
	}
	
	public static void setClasspath() {

		try {
			FileReader reader=new FileReader("src/main/resources/application.properties");
			Properties p = new Properties(); 
			p.load(reader);  
			Set set=p.entrySet();  
			Iterator itr = set.iterator();  
			while(itr.hasNext()){  
				
				Map.Entry entry=(Map.Entry)itr.next();  
				System.out.println(entry.getKey()+" = "+entry.getValue());  
				String keyValue = (String) entry.getKey();
				System.out.println(keyValue.equals("SALESFORCE_CONSUMER_KEY"));
				if(keyValue.equals("SALESFORCE_CONSUMER_KEY")) {
					Constants.SALESFORCE_CONSUMER_KEY = (String) entry.getValue();
				}
				else if(keyValue.equals("SALESFORCE_CONSUMER_SECRET")) {
					Constants.SALESFORCE_CONSUMER_SECRET = (String) entry.getValue();
				}
				else if(keyValue.equals("SALESFORCE_REDIRECT_URI")) {
					Constants.SALESFORCE_REDIRECT_URI = (String) entry.getValue();
				}
				else if(keyValue.equals("SALESFORCE_PROD_URI")) {
					Constants.SALESFORCE_PROD_URI = (String) entry.getValue();
				}
				else if(keyValue.equals("SALESFORCE_SNDBX_URI")) {
					Constants.SALESFORCE_SNDBX_URI = (String) entry.getValue();
				}  
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
