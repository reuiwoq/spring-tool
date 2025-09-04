package com.abcdefg.Service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abcdefg.Security.JwtTokenProvider;
import com.abcdefg.common.CommonResponse;
import com.abcdefg.dto.SignInResultDto;
import com.abcdefg.dto.SignUpResultDto;
import com.abcdefg.entity.User;
import com.abcdefg.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignService {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public SignUpResultDto signUp(String id, String password, String name, String role) {
		// TODO Auto-generated method stub
		User user;
		if (role.equalsIgnoreCase("admin")) {
			user = User.builder().uid(id).name(name).password(passwordEncoder.encode(password))
					.roles(Collections.singletonList("ROLE_ADMIN")).build();
		} else {
			user = User.builder().uid(id).name(name).password(passwordEncoder.encode(password))
					.roles(Collections.singletonList("ROLE_USER")).build();
		}

		User savedUser = userRepository.save(user);
		SignUpResultDto signUpResultDto = new SignUpResultDto();

		if (!savedUser.getName().isEmpty()) {
			setSuccessResult(signUpResultDto);
		} else {
			setFailResult(signUpResultDto);
		}
		return signUpResultDto;
	}

	public SignInResultDto signIn(String id, String password) throws RuntimeException {
		// TODO Auto-generated method stub
		User user = userRepository.getByUid(id);

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException();
		}
		SignInResultDto signInResultDto = SignInResultDto.builder()
				.token(jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRoles())).build();
		// String.valueOf(user.getUid()) 를 사용하면 null인 경우 문자열 "null"로 치환해 줌
		setSuccessResult(signInResultDto);

		return signInResultDto;
	}

	private void setSuccessResult(SignUpResultDto result) {
		result.setSuccess(true);
		result.setCode(CommonResponse.SUCCESS.getCode());
		result.setMsg(CommonResponse.SUCCESS.getMsg());
	}

	private void setFailResult(SignUpResultDto result) {
		result.setSuccess(false);
		result.setCode(CommonResponse.FAIL.getCode());
		result.setMsg(CommonResponse.FAIL.getMsg());
	}
}
