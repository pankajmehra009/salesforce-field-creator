package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.converter.CodeConverter;
import com.example.demo.converter.UserConverter;
import com.example.demo.dto.CodeDto;
import com.example.demo.repository.CodeRepository;
import com.example.demo.service.CodeService;

@Service
public class CodeServiceimpl implements CodeService{
	
	@Autowired
	CodeRepository codeRepository;

	@Override
	public List<CodeDto> getAccessCode() {
		return  codeRepository.findAll().stream().map(CodeConverter::entityToDto).collect(Collectors.toList());
	}
	
	@Override
	public void deleteSession() {
		codeRepository.deleteAll();
	}
}
