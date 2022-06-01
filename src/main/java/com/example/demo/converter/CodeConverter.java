package com.example.demo.converter;

import com.example.demo.dto.CodeDto;
import com.example.demo.entity.Code;

public class CodeConverter {

	public static Code dtoToEntity(CodeDto codedto) {
		Code code = new Code(codedto.getCode());
		return code;
	}

	public static CodeDto entityToDto(Code code) {
		return new CodeDto(code.getCodeName());
	}
}
