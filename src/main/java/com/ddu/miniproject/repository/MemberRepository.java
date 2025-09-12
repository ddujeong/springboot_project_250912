package com.ddu.miniproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.miniproject.entity.Member;


public interface MemberRepository extends JpaRepository<Member, String> {
	public Optional<Member> findByMemberid(String memberid);
}
