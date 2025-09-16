package com.ddu.miniproject.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Reserve;
import com.ddu.miniproject.service.MemberService;
import com.ddu.miniproject.service.ReserveService;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	@Autowired
	private ReserveService reserveService;
	
	@Autowired
	MemberService memberService;
	
	@GetMapping(value = "/reservation")
	public String reservation(@RequestParam(value="machine", required=false, defaultValue="세탁기") String machine,
			@RequestParam(value="rdate", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            Model model) {
		List<String> allTimes = List.of("10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00");
		LocalDate today =LocalDate.now();
		LocalTime now = LocalTime.now();
		
		if (selectedDate == null) selectedDate = today;
		List<Reserve> reservedList = reserveService.getReservesByDate(selectedDate, machine);
		List<String> reservedTimes = reservedList.stream().map(r -> r.getReservetime().toLocalTime().toString().substring(0,5)).toList();
		
		
		 Map<String, Boolean> timeOptions = new LinkedHashMap<>();
		    for (String t : allTimes) {
		        LocalTime optionTime = LocalTime.parse(t);
		        if (selectedDate.equals(today) && optionTime.isBefore(now)) {
		            continue; // 지난 시간은 아예 제외
		        }
		        timeOptions.put(t, reservedTimes.contains(t)); // true = 예약완료
		    }
		model.addAttribute("timeOptions", timeOptions);
	    model.addAttribute("minDate", today.toString());
	    model.addAttribute("selectedDate", selectedDate);
		model.addAttribute("reservedTimes", reservedTimes);
		model.addAttribute("selectedMachine",machine);
		
		return "reservation";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/reserveOk")
	public String reserveOk(@RequestParam("machine") String machine,@RequestParam("rdate")@DateTimeFormat(pattern="yyyy-MM-dd")LocalDate rdate,@RequestParam(value = "rtime" ,required = false)String rtime, Principal principal, Model model) {
		
		
		 if (rtime == null || rtime.isEmpty()) {
		        model.addAttribute("error", "예약 시간을 선택해주세요.");
		   return reservation(machine, rdate, model);
		 }
		LocalTime time = LocalTime.parse(rtime);
		LocalDateTime rdatetime =LocalDateTime.of(rdate, time);
		Member member = memberService.getMember(principal.getName());
		reserveService.reserve(machine,rdatetime, member);
		
		return "redirect:/member/myPage";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/reserveDelete/{id}")
	public String delete(@PathVariable("id") Integer id, Principal principal) {
		Reserve reserve = reserveService.getReserve(id);
		
		if (!reserve.getMember().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		reserveService.deleteReserve(reserve);
		return "redirect:/reserve/orders";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/orders")
	public String orders(Principal principal, Model model) {
		Member member =memberService.getMember(principal.getName());
		List<Reserve> reserves = reserveService.getReserveList(member);
		
		for (Reserve r : reserves) {
		    if (r.getReservetime().isBefore(LocalDateTime.now())) {
		        r.setStatus("진행중");
		    }
		}
		model.addAttribute("member",member);
		model.addAttribute("reserves",reserves);
		
		return"orders";
	}
	
}
