package com.abcdefg.Security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor	
public class JwtTokenProvider {
	private final UserDetailsService userDetailsService;

	private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${springboot.jwt.secret}")
	private String secretKey = "randomKey";
	private final long tokenValidMillisecond = 1000L * 60 * 60;

	// 스프링 빈 생성 후 실행(secretKey 값을 Base64형식으로 인코딩하여 저장)
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(String userUid, List<String> roles) {
		logger.info("[createToken] 토큰 생성 시작");

		Date now = new Date();
		String token = Jwts.builder().subject(userUid).claim("roles", roles).issuedAt(now)
				.expiration(new Date(now.getTime() + tokenValidMillisecond)).signWith(getSigningKey()).compact();
		// subject(userUid) : sub 클레임에 userUid 저장
		// claim("roles", roles) : roles 라는 비공개 클레임에 roles 저장
		logger.info("[createToken] 토큰 생성 완료");
		return token;
	}

	// JWT 토큰으로 인증 정보 조회
	public Authentication getAuthentication(String token) {
		logger.info("[getAuthentication] 토큰 인증 정보 조회 시작");
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
		logger.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}", userDetails.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// JWT 토큰에서 회원 구별 정보 추출
	public String getUsername(String token) {
		logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
		String info = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload()
				.getSubject();
		logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
		return info;
	}

	/**
	 * HTTP Request Header 에 설정된 토큰 값을 가져옴
	 *
	 * @param request Http Request Header
	 * @return String type Token 값
	 */
	public String resolveToken(HttpServletRequest request) {
		logger.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
		return request.getHeader("X-AUTH-TOKEN");
	}

	// JWT 토큰의 유효성 + 만료일 체크
	public boolean validateToken(String token) {
		logger.info("[validateToken] 토큰 유효 체크 시작");
		try {
			Jws<Claims> claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
			logger.info("[validateToken] 토큰 유효 체크 완료");
			return !claims.getPayload().getExpiration().before(new Date());
		} catch (Exception e) {
			logger.info("[validateToken] 토큰 유효 체크 예외 발생");
			return false;
		}
	}

}
