package com.ddu.miniproject.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	
	public List<Reserve> findByMachineAndReservetimeBetween(String machine,LocalDateTime start, LocalDateTime end);
	
	public List<Reserve>findByMember(Member member);
}
