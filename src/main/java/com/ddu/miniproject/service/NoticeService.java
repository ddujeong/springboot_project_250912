package com.ddu.miniproject.service;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddu.miniproject.entity.Member;
import com.ddu.miniproject.entity.Notice;
import com.ddu.miniproject.repository.MemberRepository;
import com.ddu.miniproject.repository.NoticeRepository;
import com.ddu.miniproject.security.DataNotFoundException;


@Service
public class NoticeService {

    private final MemberRepository memberRepository;

	@Autowired
    NoticeRepository noticeRepository;

    NoticeService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
	
	public List<Notice> getList(){
		return noticeRepository.findAll();
	}
	
	public Notice getNotice(Integer id) {
		Optional<Notice> nOptional =noticeRepository.findById(id);
		
		if (nOptional.isPresent()) {
			return nOptional.get();
		} else {
			throw new DataNotFoundException("notice not found");
		}
	}
	public void create(String btitle, String bcontent, Member member, String category ) {
		Notice notice = new Notice();
		notice.setBtitle(btitle);
		notice.setBcontent(bcontent);
		notice.setCreatedate(LocalDateTime.now());
		notice.setCategory(category);
		notice.setAuthor(member);
		
		noticeRepository.save(notice);
	}
	public Page<Notice> getPageList (int page, String kw, String category){
		int size = 10; // 1페이지당 글 10개씩 출력
		int startRow = page *10; // * 페이징 의 첫번째 글 넘버 * 첫 페이지 page=0 -> 0*10 = 0 / 두번째 페이지 page =1 -> 1*10 = 10
		int endRow = (startRow) + size; // * 페이징의 마지막 글 넘버 * 첫번째 페이지  0 +10 = 10 / 두번째 페이지 10 + 10 = 20
		
		List<Notice> searchNoticeList = noticeRepository.searchNoticesWithPaging(kw, startRow, endRow, category);
		int totalSearchNotice = noticeRepository.countSearchResult(kw, category);
		
		Page<Notice> pagingList = new PageImpl<>(searchNoticeList, PageRequest.of(page, size), totalSearchNotice);
		
		return pagingList;
	}
	public void modify(Notice notice, String btitle, String bcontent ) {
		notice.setBtitle(btitle);
		notice.setBcontent(bcontent);
		noticeRepository.save(notice);
	}
	public void delete(Notice notice) {
		noticeRepository.delete(notice);
	}
	public void hit(Integer id) {
		noticeRepository.upBhit(id);
	}
	@Transactional
	public void vote(Member member, Notice notice) {
		if (notice.getVoter() ==null) {
			notice.setVoter(new HashSet<>());
		}
		Set<Member> voters = notice.getVoter();
		
		if (voters.contains(member)) {
			voters.remove(member);
		} else {
			voters.add(member);
		}
		noticeRepository.save(notice);
	}
	public void scrap(Member member, Notice notice) {
		
		if (notice.getScrapers().contains(member)) {
			notice.getScrapers().remove(member);
			member.getScrapnotices().remove(notice);
		} else {
			notice.getScrapers().add(member);
			member.getScrapnotices().add(notice);
		}
		noticeRepository.save(notice);
		memberRepository.save(member);
	}

}
