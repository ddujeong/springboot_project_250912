package com.ddu.miniproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "member")
public class Member {
	
	@Id
	private String memberid;
	
	private String memberpw;
	
	private String membername;
	
	@Column(unique = true)
	private String memberphone;
}
