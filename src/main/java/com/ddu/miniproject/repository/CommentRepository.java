package com.ddu.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.miniproject.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
