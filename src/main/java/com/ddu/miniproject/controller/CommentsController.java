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

import com.ddu.miniproject.entity.Comments;
import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Notice;
import com.ddu.miniproject.repository.NoticeRepository;
import com.ddu.miniproject.security.CommentsForm;
import com.ddu.miniproject.security.NoticeForm;
import com.ddu.miniproject.service.CommentsService;
import com.ddu.miniproject.service.MemberService;
import com.ddu.miniproject.service.NoticeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/comment")
public class CommentsController {

	@Autowired
	NoticeService noticeService;
	
	@Autowired
	CommentsService commentsService;
	
	@Autowired
	MemberService memberService;

	
	@PostMapping(value = "/create/{id}")
	public String createComment(Model model,@PathVariable("id") Integer id,@Valid CommentsForm commentsForm, BindingResult bindingResult, Principal principal) {
		Notice notice= noticeService.getNotice(id);
		Member member =memberService.getMember(principal.getName());
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("notice", notice);
			return "contentView";
		}
		Comments comment = commentsService.create(notice,commentsForm.getCtext(), member);
		
		return String.format("redirect:/notice/contentView/%s#comment_%s", id, comment.getId());
	}
	@GetMapping(value = "/modify/{id}/bid={bid}")
	public String modify(@PathVariable("id") Integer id,@PathVariable("bid") Integer bid, CommentsForm commentsForm, Principal principal, Model model) {
		Notice notice = noticeService.getNotice(bid);
		Comments comments = commentsService.getComment(id);
		if (!comments.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		commentsForm.setCtext(comments.getCtext());
		model.addAttribute("notice",notice);
		return "commentModify";
	}
	@PostMapping(value = "/modify/{id}")
	public String commentModify(@PathVariable("id") Integer id, @Valid CommentsForm commentsForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "contentView";
		}
		Comments comment = commentsService.getComment(id);
		
		if (!comment.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		commentsService.modify(comment, commentsForm.getCtext());
		return String.format("redirect:/notice/contentView/%s#comment_%s", comment.getNotice().getId(), comment.getId());
	}
	@GetMapping(value = "/delete/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String commentDelete(@PathVariable("id") Integer id, Principal principal) {
		Comments comment = commentsService.getComment(id);
		
		if (!comment.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		commentsService.delete(comment);
		return String.format("redirect:/notice/contentView/%s", comment.getNotice().getId()) ; // 리스트로 이동
	}
	
}