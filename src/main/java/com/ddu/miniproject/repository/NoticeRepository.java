package com.ddu.miniproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ddu.miniproject.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	public Optional<Notice> findByBtitle(String btitle);
	
	public Optional<Notice> findByBtitleAndBcontent (String btitl, String bcontent);
	
	public List<Notice> findByBtitleLike(String keyword);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE notice SET bhit= bhit+1 WHERE id= :id" , nativeQuery = true)
	public int upBhit(@Param("id") Integer id);
	
	// 페이징 관련 메서드들 
	@Query( value = "SELECT * FROM ( " +
	                 " SELECT n.*, ROWNUM rnum FROM ( " +
	                 "   SELECT * FROM notice ORDER BY createdate DESC " +
	                 " ) n WHERE ROWNUM <= :endRow " +
	                 ") WHERE rnum > :startRow", nativeQuery = true)
	List<Notice> findNoticesWithPaging(@Param("startRow") int startRow,
	                                           @Param("endRow") int endRow);
	Page<Notice> findAll(Specification<Notice> spec, Pageable pageable);
	
	// 검색 결과를 페이징하는 리스트를 조회하는 메서드
		@Query(value = "SELECT * FROM ( " +
			    	       "   SELECT n.*, ROWNUM rnum FROM ( " +
			    	       "       SELECT DISTINCT n.* " +
			    	       "       FROM notice n " +
			    	       "       LEFT OUTER JOIN member m1 ON n.author_memberid = m1.memberid " +
			    	       "       LEFT OUTER JOIN comments c ON c.notice_id = n.id " +
			    	       "       LEFT OUTER JOIN member m2 ON c.author_memberid = m2.memberid " +
			    	       "       WHERE n.btitle LIKE '%' ||:kw || '%' " +
			    	       "          OR n.bcontent LIKE  '%' ||:kw || '%'" +
			    	       "          OR m1.memberid LIKE  '%' ||:kw || '%' " +
			    	       "          OR c.ctext LIKE  '%' ||:kw || '%' " +
			    	       "          OR m2.memberid LIKE  '%' ||:kw || '%' " +
			    	       "       ORDER BY n.createdate DESC " +
			    	       "   ) n WHERE ROWNUM <= :endRow " +
			    	       ") WHERE rnum > :startRow", 
			    	       nativeQuery = true)
		List<Notice> searchNoticesWithPaging(@Param("kw") String kw, @Param("startRow") int startRow, @Param("endRow") int endRow);
		
		// 검색 결과 총 갯수 반환
		@Query(value = 
	    	       "       SELECT COUNT(DISTINCT n.id) " +
	    	       "       FROM notice n " +
	    	       "       LEFT OUTER JOIN member m1 ON n.author_memberid = m1.memberid " +
	    	       "       LEFT OUTER JOIN comments c ON c.notice_id = n.id " +
	    	       "       LEFT OUTER JOIN member m2 ON c.author_memberid = m2.memberid " +
	    	       "       WHERE n.btitle LIKE '%' ||:kw || '%' " +
	    	       "          OR n.bcontent LIKE  '%' ||:kw || '%'" +
	    	       "          OR m1.memberid LIKE  '%' ||:kw || '%' " +
	    	       "          OR c.ctext LIKE  '%' ||:kw || '%' " +
	    	       "          OR m2.memberid LIKE  '%' ||:kw || '%' ",
	    	       nativeQuery = true)
		int countSearchResult(@Param("kw") String kw);
}
