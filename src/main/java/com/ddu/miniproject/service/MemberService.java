package com.ddu.miniproject.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.repository.MemberRepository;
import com.ddu.miniproject.security.DataNotFoundException;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Member create(String memberid, String memberpw, String membername, String memberphone) {
		Member member = new Member();
		member.setMemberid(memberid);
		member.setMemberpw(passwordEncoder.encode(memberpw));
		member.setMembername(membername);
		member.setMemberphone(memberphone);
		
		memberRepository.save(member);
		return member;
	}
	public Member getMember(String memberid) {
		Optional<Member> _member = memberRepository.findByMemberid(memberid);
		
		if (_member.isPresent()) {
			Member member =_member.get();
			return member;
		} else {
			throw new DataNotFoundException("해당 유저는 존재하지 않는 유저입니다.");
		}
	}
	public void delete(Member member) {
		memberRepository.deleteById(member.getMemberid());
	}

}
