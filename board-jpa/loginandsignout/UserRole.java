package com.abcdefg.service;

import lombok.Getter;

@Getter
public enum UserRole {
	ADMIIN("ROLE_ADMIN"), USER("ROLE_USER");
	
	UserRole (String role) {
		this.role = role;
	}
	
	private String role;
}
