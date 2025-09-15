package com.ddu.miniproject.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.security.MemberCreateForm;
import com.ddu.miniproject.service.MemberService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping(value = "/join")
	public String join(MemberCreateForm memberCreateForm) {
		return "join";
	}
	@PostMapping("/join")
	public String joinOk(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "join";
		}
		if(!memberCreateForm.getMemberpw().equals(memberCreateForm.getCheckpw())) {
			bindingResult.rejectValue("checkpw", "passwordInCorrect", "비밀번호 확인이 일치하지 않습니다.");
			return "join";
		}
		try {
			memberService.create(memberCreateForm.getMemberid(),memberCreateForm.getMemberpw() ,memberCreateForm.getMembername(),memberCreateForm.getMemberphone());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed","이미 등록 된 사용자입니다.");
			return "join";
		}
		return "redirect:/member/login";
	}
	@GetMapping(value = "/login")
	public String login() {
		return "login";
	}
	@GetMapping(value = "/memberDelete/{memberid}")
	public String memberDelete(@PathVariable("memberid")String memberid,Principal principal ) {
		Member member = memberService.getMember(memberid);
		
		if (member.getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		memberService.delete(member);
		return "redirect:/";
	}

}
