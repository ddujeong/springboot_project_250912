package com.ddu.miniproject.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Reserve;
import com.ddu.miniproject.repository.ReserveRepository;

@Service
public class ReserveService {
	
	@Autowired
	private ReserveRepository reserveRepository;
	
	@Autowired
	MemberService memberService;
	
	public Reserve reserve(String machine, LocalDateTime rdatetime, Member member) {
		LocalDateTime start = rdatetime.minusSeconds(360);
		LocalDateTime end = rdatetime.plusSeconds(360);
		Optional<Reserve> _reserve = reserveRepository.findByMachineAndReservetimeBetween(machine, start, end);
		
		if (_reserve.isPresent()) {
			throw new IllegalStateException("해당 시간의 예약이 존재합니다.");
		} else {
			Reserve reserve = new Reserve();
			
			reserve.setMachine(machine);
			reserve.setMember(member);
			reserve.setReservetime(rdatetime);
			reserve.setCreatetime(LocalDateTime.now());
			reserve.setStatus("예약완료");
			return reserveRepository.save(reserve);
		}
	}
	public void deleteReserve(Reserve reserve) {
		reserveRepository.delete(reserve);
	}
	public List<Reserve> getReserve(Member member){
		return reserveRepository.findByMember(member);
	}

}
