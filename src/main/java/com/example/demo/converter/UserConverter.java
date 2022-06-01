package com.example.demo.converter;

import java.util.stream.Collectors;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;

/**
 * Created by ashish on 13/5/17.
 */
public class UserConverter {
	public static Users dtoToEntity(UserDto userDto) {
		Users user = new Users(userDto.getUserName(), null);
		user.setUserId(userDto.getUserId());
		user.setSkills(userDto.getSkillDtos().stream().map(SkillConverter::dtoToEntity).collect(Collectors.toList()));
		return user;
	}

	public static UserDto entityToDto(Users user) {
		UserDto userDto = new UserDto(user.getUserId(), user.getUserName(), null);
		userDto.setSkillDtos(user.getSkills().stream().map(SkillConverter::entityToDto).collect(Collectors.toList()));
		return userDto;
	}
}
