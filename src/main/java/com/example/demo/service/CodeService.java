package com.example.demo.service;


import java.util.List;

import com.example.demo.dto.CodeDto;

public interface CodeService {
	
	List<CodeDto> getAccessCode();
    //void saveUser(UserDto userDto);
    //List<UserDto> getAllUsers();
	void deleteSession();
    
}
