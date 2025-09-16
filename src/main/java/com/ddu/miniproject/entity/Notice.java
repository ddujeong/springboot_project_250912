package com.ddu.miniproject.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notice")
@SequenceGenerator(
		name = "NOTICE_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "NOTICE_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1 , // 시퀀스 증가 값
		allocationSize = 1 // 시퀀스 증가치
		)
public class Notice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="NOTICE_SEQ_GENERATOR" )  // 자동증가 (AI)
	private Integer id;
	
	@Column(length = 200)
	private String btitle;
	
	@Column(length = 500)
	private String bcontent;
	
	
	private Integer bhit = 0;
	
	private LocalDateTime createdate;
	
	@OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
	private List<Comments> commentList;
	
	@ManyToOne
	private Member author;
	
	@ManyToMany
	private Set<Member> voter;
	
	@ManyToMany(mappedBy ="scrapnotices" )
	private Set<Member> scrapers;
	
	private String category;
}
