package com.ddu.miniproject.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.repository.MemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	public Member create(String memberid, String memberpw, String membername, String memberphone) {
//		Member member = new Member();
//		member.setMemberid(memberid);
//	}

}
