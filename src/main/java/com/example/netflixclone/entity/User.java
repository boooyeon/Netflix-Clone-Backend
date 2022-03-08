package com.example.netflixclone.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {
	@Id // Primary Key
	@GeneratedValue(strategy=GenerationType.IDENTITY) //프로젝트에서 연결된 DB의 넘버링 전략을 따라간다
	private int id; //시퀀스, auto_increment
	
	@Column(nullable=false, length=30, unique=true)
	private String username; // 아이디
	
	@Column(nullable = false, length=100) // 123456=>해쉬(비밀번호 암호화)
	private String password;
	
	@Column(nullable=false, length=50)
	private String email;
}