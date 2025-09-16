package com.ddu.miniproject.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
	
	@Id
	private String memberid;
	
	private String memberpw;
	
	private String membername;
	
	@Column(unique = true)
	private String memberphone;
	
	@ManyToMany
	private Set<Notice> scrapnotices;
}
