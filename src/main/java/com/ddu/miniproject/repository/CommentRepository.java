package com.ddu.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.miniproject.entity.Comments;

public interface CommentRepository extends JpaRepository<Comments, Integer> {

	
}
