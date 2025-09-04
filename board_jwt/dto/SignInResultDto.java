package com.abcdefg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResultDto extends SignUpResultDto{
	private String token;

	@Builder
	public SignInResultDto(boolean success, int code, String msg, String token) {
		super(success, code, msg);
		this.token = token;
	}
}
