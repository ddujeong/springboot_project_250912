package com.ddu.miniproject.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Reserve;
import com.ddu.miniproject.security.MemberCreateForm;
import com.ddu.miniproject.service.MemberService;
import com.ddu.miniproject.service.ReserveService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	@Autowired
	private ReserveService reserveService;
	
	@Autowired
	MemberService memberService;
	
	@GetMapping(value = "/reservation")
	public String reservation() {
		return "reservation";
	}
	@PostMapping("/reserveOk")
	public String reserveOk(@RequestParam("machine") String machine,@RequestParam("rdatetime")@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")LocalDateTime rdatetime  ,BindingResult bindingResult, Principal principal) {
		Member member = memberService.getMember(principal.getName());
		reserveService.reserve(machine,rdatetime, member);
		
		return "redirect:/member/orders";
	}
	
}
