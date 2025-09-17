package com.ddu.miniproject.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Notice;
import com.ddu.miniproject.repository.NoticeRepository;
import com.ddu.miniproject.security.CommentsForm;
import com.ddu.miniproject.security.NoticeForm;
import com.ddu.miniproject.service.MemberService;
import com.ddu.miniproject.service.NoticeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	MemberService memberService;

	
	@GetMapping(value = "/notice/{category}")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page, @RequestParam(value = "kw" , defaultValue = "") String kw, @PathVariable("category") String category) {
		Page<Notice> paging = noticeService.getPageList(page, kw, category );
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("category", category);
		
		return "notice";
	}
	@GetMapping(value = "/contentView/{id}")
	public String View(Model model ,@PathVariable("id") Integer id, CommentsForm commentsForm, @RequestParam(value ="page", defaultValue = "0") int page) {
		noticeService.hit(id);
		Notice notice = noticeService.getNotice(id);
		model.addAttribute("notice", notice);
		
		return "contentView";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/create/{category}")
	public String noticeCreate(NoticeForm noticeForm, Principal principal, Model model, @PathVariable("category") String category) {
		 Member member =memberService.getMember(principal.getName());
		if (member.getMemberid().equals("admin")) {
			category ="notice";
		}
		model.addAttribute("membername",member.getMembername());
		model.addAttribute("category", category);
		return"writeForm";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/create")
	public String noticeCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {
		Member member = memberService.getMember(principal.getName());
		
		if (bindingResult.hasErrors()) {
			return "notice";
		}
		noticeService.create(noticeForm.getBtitle(),noticeForm.getBcontent(), member,noticeForm.getCategory());
		return"redirect:/notice/notice/" + noticeForm.getCategory();
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/modify/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String modify(@PathVariable("id") Integer id, NoticeForm noticeForm, Principal principal) {
		Notice notice =noticeService.getNotice(id);
		
		if (!notice.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		
		// question_form 에 questionForm에 subject 와 content 를 value 로 출력하는 기능이 이미 구현되어 있으므로 
		// 해당 폼을 재사용하기 위해 questionForm에 question 의 필드값을 저장하여 전송
		noticeForm.setBtitle(notice.getBtitle());
		noticeForm.setBcontent(notice.getBcontent());
		noticeForm.setMembername(notice.getAuthor().getMembername());
		
		return "noticeModify";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/modify/{id}")
	public String noticeModify(@PathVariable("id") Integer id, @Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "noticeModify";
		}
		Notice notice =noticeService.getNotice(id);
		
		if (!notice.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		noticeService.modify(notice, noticeForm.getBtitle(), noticeForm.getBcontent());
		return String.format("redirect:/notice/contentView/%s", id);
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/delete/{id}")
	public String noticeDelete(@PathVariable("id") Integer id, Principal principal) {
		Notice notice = noticeService.getNotice(id);
		
		if (!notice.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		noticeService.delete(notice);
		return"redirect:/";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/vote/{id}")
	public String noticeVote(@PathVariable("id") Integer id, Principal principal) {
		Notice notice = noticeService.getNotice(id);
		Member member =memberService.getMember(principal.getName());
		
		noticeService.vote(member, notice);
		
		return String.format("redirect:/notice/contentView/%s", id) ;
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/scrap/{id}")
	public String noticeScrap(@PathVariable("id") Integer id, Principal principal) {
		Notice notice = noticeService.getNotice(id);
		Member member =memberService.getMember(principal.getName());
		
		noticeService.scrap(member, notice);
		
		return String.format("redirect:/notice/contentView/%s", id) ;
	}

}
