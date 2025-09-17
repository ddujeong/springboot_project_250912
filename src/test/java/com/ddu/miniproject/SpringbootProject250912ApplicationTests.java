package com.ddu.miniproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
