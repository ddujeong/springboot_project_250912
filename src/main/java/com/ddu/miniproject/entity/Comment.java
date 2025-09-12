package com.ddu.miniproject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Table(name = "comment")
@SequenceGenerator(
		name = "COMMENT_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "COMMENT_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1 , // 시퀀스 증가 값
		allocationSize = 1 // 시퀀스 증가치
		)
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="COMMENT_SEQ_GENERATOR" )  // 자동증가 (AI)
	private Integer id;
	
	@Column(length = 200)
	private String ctext;
	
	@Column(length = 500)
	private String cdate;
	
	private LocalDateTime createdate;
	
	@ManyToOne
	private Notice notice;
	
	@ManyToOne
	private Member author;
}
