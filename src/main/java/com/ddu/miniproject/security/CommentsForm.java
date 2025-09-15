package com.ddu.miniproject.security;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentsForm {
	
	@NotEmpty(message = "내용을 필수입력 항목입니다.")
	private String ctext;
}
