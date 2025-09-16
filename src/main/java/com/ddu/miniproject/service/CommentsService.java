package com.ddu.miniproject.service;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.miniproject.entity.Comments;
import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Notice;
import com.ddu.miniproject.repository.CommentRepository;
import com.ddu.miniproject.repository.NoticeRepository;
import com.ddu.miniproject.security.DataNotFoundException;

@Service
public class CommentsService {
	
	@Autowired
	CommentRepository commentRepository;

	@Autowired
    NoticeRepository noticeRepository;
	
	public Comments create(Notice notice, String ctext, Member member) {
		Comments comment = new Comments();
		comment.setCtext(ctext);
		comment.setCreatedate(LocalDateTime.now());
		comment.setAuthor(member);
		comment.setNotice(notice);
		commentRepository.save(comment);
		return comment;
	}
	public Comments getComment(Integer id) {
		Optional<Comments> _comment =commentRepository.findById(id);
		if(_comment.isPresent()) {
			return _comment.get();
		} else {
			throw new DataNotFoundException("해당 답변이 존재하지 않습니다.");
		}
	}
	public void modify(Comments comments, String ctext) {
		comments.setCtext(ctext);
		commentRepository.save(comments);
	}
	public void delete(Comments comments) {
		commentRepository.delete(comments);
	}
	public void vote(Member member,Comments comments ) {
		comments.getVoter().add(member);
		commentRepository.save(comments);
	}
	
	
	
}
