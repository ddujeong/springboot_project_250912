package com.ddu.miniproject.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Reserve;
import com.ddu.miniproject.repository.ReserveRepository;
import com.ddu.miniproject.security.DataNotFoundException;

@Service
public class ReserveService {
	
	@Autowired
	private ReserveRepository reserveRepository;
	
	@Autowired
	MemberService memberService;
	
	public Reserve reserve(String machine, LocalDateTime rdatetime, Member member) {
		if (rdatetime.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("과거 시간에는 예약할 수 없습니다.");
		}
		
		LocalDateTime start = rdatetime;
		LocalDateTime end = rdatetime.plusHours(1);
		List<Reserve> _reserve = reserveRepository.findByMachineAndReservetimeBetween(machine, start, end);
		
		if (!_reserve.isEmpty()) {
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
	public List<Reserve> getReserveList(Member member){
		return reserveRepository.findByMember(member);
	}
	public Reserve getReserve(Integer id) {
		Optional<Reserve>_reserve =reserveRepository.findById(id);
		
		if (_reserve.isPresent()) {
			Reserve reserve=_reserve.get();
			return reserve;
		} else {
			throw new DataNotFoundException("해당 예약은 존재하지 않는 예약입니다.");
		}
	}
	public List<Reserve> getReservesByDate(LocalDate date, String machine){
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.atTime(LocalTime.MAX);
		return reserveRepository.findByMachineAndReservetimeBetween(machine, start, end);
	}
	@Transactional
	public void updateStatus(List<Reserve> reserves) {
		for (Reserve r : reserves) {
		    if (r.getReservetime().isBefore(LocalDateTime.now())) {
		        r.setStatus("진행중");
		        if ( LocalDateTime.now().isAfter( r.getReservetime().plusMinutes(60))) {
					r.setStatus("세탁완료");
				}
		    }
		}
	}

}
