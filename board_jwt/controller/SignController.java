package com.abcdefg.Controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcdefg.Service.SignService;
import com.abcdefg.dto.SignInResultDto;
import com.abcdefg.dto.SignUpResultDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
public class SignController {
	private final SignService signService;
	
	private Logger logger = LoggerFactory.getLogger(SignController.class);

	@PostMapping("/sign-in")
	public SignInResultDto signIn(@RequestParam(required = true) String id,
			@RequestParam(required = true) String password) throws RuntimeException {
		logger.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", id);
		SignInResultDto signInResultDto = signService.signIn(id, password);

		if (signInResultDto.getCode() == 0) {
			logger.info("[signIn] 정상적으로 로그인되었습니다. id : {}, token : {}", id, signInResultDto.getToken());
		}
		return signInResultDto;
	}

	@PostMapping("/sign-up")
	public SignUpResultDto signUp(@RequestParam(required = true) String id,
			@RequestParam(required = true) String password, @RequestParam(required = true) String name,
			@RequestParam(required = true) String role) {
		logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}, role : {}", id, name, role);
		SignUpResultDto signUpResultDto = signService.signUp(id, password, name, role);

		logger.info("[signUp] 회원가입을 완료했습니다. id : {}", id);
		return signUpResultDto;
	}

	@GetMapping("/exception")
	public void exceptionTest() throws RuntimeException {
		throw new RuntimeException("접근이 금지되었습니다.");
	}

	@ExceptionHandler(value=RuntimeException.class)
	public Map<String, String> ExceptionHandler(RuntimeException e) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		logger.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

		Map<String, String> map = new HashMap<>();
		map.put("error type", httpStatus.getReasonPhrase());
		map.put("code", "400");
		map.put("message", "에러 발생");

		return map;
	}
}
