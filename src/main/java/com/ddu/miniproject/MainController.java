package com.ddu.miniproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	// @GetMapping(value = "/") // 로컬용 root 요청 처리
	@GetMapping(value = "/") // 오라클 클라우드용 root 요청 처리
	public String root() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "redirect:/index";
	}
	@GetMapping(value = "/index") // 오라클 클라우드용 root 요청 처리
	public String index() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "index";
	}
	@GetMapping(value = "/map") // 오라클 클라우드용 root 요청 처리
	public String map() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "map";
	}
}
