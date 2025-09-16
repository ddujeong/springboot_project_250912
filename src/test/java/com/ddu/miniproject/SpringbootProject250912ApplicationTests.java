package com.ddu.miniproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.service.MemberService;
import com.ddu.miniproject.service.NoticeService;

@SpringBootTest
class SpringbootProject250912ApplicationTests {
	
	@Autowired
	MemberService memberService;
	@Autowired
	NoticeService noticeService;
	@Test
	void contextLoads() {
	}
	@Test
	@DisplayName("더미 데이터 삽입")
	public void testJpa6() {
		Member member = memberService.getMember("tiger");
		for (int i = 0; i <100; i++) {
			String btitle = String.format("테스트 데이터입니다:[%03d]", i);
			String bcontent = "연습내용 더미 데이터 입니다!~!";
			
			noticeService.create(btitle, bcontent, member);
		}
	}
}
