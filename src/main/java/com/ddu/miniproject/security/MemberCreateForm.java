package com.ddu.miniproject.security;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberCreateForm {
	
	@Size(min = 3, max = 10, message = "아이디는 3자 이상 10자 이하입니다.")
	@NotEmpty(message = "사용자 ID는 필수 항목입니다.")
	private String memberid;
	
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String memberpw;
	
	@NotEmpty(message = "비밀번호  확인은 필수 항목입니다.")
	private String checkpw;
	
	@NotEmpty(message = "이름은 필수 항목입니다.")
	private String membername;
	
	@NotEmpty(message = "전화번호는 필수 항목입니다.")
	private String memberphone;
}
