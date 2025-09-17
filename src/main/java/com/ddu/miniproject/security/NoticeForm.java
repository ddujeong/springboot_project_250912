package com.ddu.miniproject.security;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoticeForm {
	private String membername;
	
	@Size(min = 5, message = "제목은 최소 5글자 이상 가능합니다.") // 제목은 최소 5글자 부터 허용
	@Size(max = 200, message = "제목은 200자 까지 가능합니다.") // 제목은 최대 200글자 까지 허용
	@NotEmpty(message = "제목은 필수 항목입니다.") // 공란으로 들어오면 작동
	private String btitle;
	
	@Size(min = 5, message = "내용은 최소 5글자 이상 가능합니다.")
	@Size(max = 500, message = "내용은 최대 500자 까지 가능합니다.") // 내용은 최대 500글자 까지 허용
	@NotEmpty(message = "내용은 필수 항목입니다.")
	private String bcontent;
	
	private String category;
}
