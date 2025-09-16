package com.ddu.miniproject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "reserve")
@SequenceGenerator(
		name = "RESERVE_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "RESERVE_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1 , // 시퀀스 증가 값
		allocationSize = 1 // 시퀀스 증가치
		)
public class Reserve {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="RESERVE_SEQ_GENERATOR" )  // 자동증가 (AI)
	private Integer id;
	
	private String machine;
	
	private String status;
	
	private LocalDateTime reservetime;
	
	private LocalDateTime createtime;
	
	@ManyToOne
	private Member member;
}
