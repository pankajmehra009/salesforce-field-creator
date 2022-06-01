package com.example.demo.entity;

import javax.persistence.*;

/**
 * Created by ashish on 13/5/17.
 */
@Entity
public class Skill {
    @Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer skillId;
    @Column
    private String skillName;
    @ManyToOne
    private Users user;

    public Skill(String skillName) {
		this.skillName = skillName;
	}

	public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Skill() {
    }

    public Skill(String skillName, Users user) {
        this.skillName = skillName;
        this.user = user;
    }
}
