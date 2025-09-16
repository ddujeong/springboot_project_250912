package com.ddu.miniproject.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Reserve;
import com.ddu.miniproject.repository.MemberRepository;
import com.ddu.miniproject.security.MemberCreateForm;
import com.ddu.miniproject.service.MemberService;
import com.ddu.miniproject.service.ReserveService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/member")
public class MemberController {

    private final SecurityFilterChain filterChain;

    private final MemberRepository memberRepository;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	ReserveService reserveService;

    MemberController(MemberRepository memberRepository, SecurityFilterChain filterChain) {
        this.memberRepository = memberRepository;
        this.filterChain = filterChain;
    }
	
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
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/memberDelete")
	public String memberDelete(@RequestParam("memberid") String memberid,Principal principal, HttpServletRequest request ) {
		Member member = memberService.getMember(memberid);
		
		if (!member.getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		memberService.delete(member);
		request.getSession().invalidate();
		SecurityContextHolder.clearContext();
		return "redirect:/";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/myPage")
	public String myPage(Principal principal , Model model) {
		Member member =memberService.getMember(principal.getName());
		List<Reserve> reserves = reserveService.getReserveList(member);
		reserveService.updateStatus(reserves);
		if (!member.getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		model.addAttribute("reserves",reserves);
		model.addAttribute("member",member);
		MemberCreateForm form = new MemberCreateForm();
		form.setMemberid(member.getMemberid());
	    form.setMembername(member.getMembername());
		form.setMemberphone(member.getMemberphone());
		    // 비밀번호는 비워두는 것이 일반적
		
	    model.addAttribute("memberCreateForm", form);
		return "myPage";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/modify/{memberid}")
	public String memberModify(@PathVariable("memberid")String memberid, @Valid MemberCreateForm memberCreateForm,BindingResult bindingResult, Principal principal, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("tab", "profile");
			model.addAttribute("memberCreateForm", memberCreateForm);
			model.addAttribute("member", memberService.getMember(principal.getName())); // member도 필요
			return "mypage";
		}
		Member member = memberService.getMember(principal.getName());
		
		memberService.modify(member, memberCreateForm.getMembername(), memberCreateForm.getMemberpw(), memberCreateForm.getMemberphone());
		return String.format("redirect:/member/myPage/%s?tab=profile", memberid);
	}
	
}
