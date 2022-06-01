package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Code {

	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer Id;
	public Integer getId() {
        return Id;
    }

    public void setUserId(Integer Id) {
        this.Id = Id;
    }
	
	@Column(length = 2000)
    private String codeName;
	
	public Code(String codeName) {
		this.codeName = codeName;
	}
	
	public Code() {
	}
	
	public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
