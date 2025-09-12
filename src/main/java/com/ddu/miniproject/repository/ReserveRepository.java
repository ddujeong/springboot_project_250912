package com.ddu.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.miniproject.entity.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {

}
