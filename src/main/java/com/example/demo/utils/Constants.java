package com.example.demo.utils;

public interface Constants {
	static final String GET_USER_BY_ID = "/getUser/{userId}";
	static final String GET_ALL_USERS = "/getAllUsers";
	static final String SAVE_USER = "/saveUser";
	static final String SALESFORCE_CONSUMER_KEY = "3MVG9Y6d_Btp4xp7xxXFOrwzsIK5ZcRR4GPpYLoYFeyYOsfV6wLEcre7lJXmXmOfze__RT2C6xoa.7c2zjg0L";
	static final String SALESFORCE_CONSUMER_SECRET = "146D10237AD275AE1E3BF41E2DA2A286DAC92DF4B8CBF617DC450383BBDB30D7";
	static final String SALESFORCE_REDIRECT_URI = "https://localhost:8080/oauth2/callback";
	static final String SALESFORCE_PROD_URI = "https://login.salesforce.com";
	static final String SALESFORCE_SNDBX_URI = "https://test.salesforce.com";
	//static final String SALESFORCE_USERNAME = "p";
	//static final String SALESFORCE_PASSWORD = "https://test.salesforce.com";
	
	static final String SALESFORCE_API_VERSION = "38";
	 
    /*'grant_type': 'password',
    'client_id' : sf_conf["client_id"],
    'client_secret' : sf_conf["client_secret"],
    'username'  : sf_conf["username"],
    'password'  : sf_conf["password"] */
	//https://dzone.com/articles/java-8-springboot-angularjs-bootstrap-springdata-j
}
 