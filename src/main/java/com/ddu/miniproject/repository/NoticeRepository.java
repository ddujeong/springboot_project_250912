package com.ddu.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.miniproject.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
