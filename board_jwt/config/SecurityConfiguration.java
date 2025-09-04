package com.abcdefg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.abcdefg.Security.CustomAccessDeniedHandler;
import com.abcdefg.Security.CustomAuthenticationEntryPoint;
import com.abcdefg.Security.JwtAuthenticationFilter;
import com.abcdefg.Security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final JwtTokenProvider jwtTokenProvider; 
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(HttpBasicConfigurer::disable) // UI를 사용하는 것을 기본값으로 가진 시큐리티 설정을 비활성
				.csrf(CsrfConfigurer::disable) // CSRF 보안설정 비활성
				.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// JWT 토큰인증 방식의 사용으로 세션은 사용하지 않음
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/sign-api/sign-in", "/sign-api/sign-up", "/sign-api/exception").permitAll()
						.requestMatchers("**exception**").permitAll().anyRequest().hasAnyRole("ADMIN")
						// 나머지 요청은 ADMIN 권한을 가진 사용자에게 허용
				)
				.exceptionHandling(authenticationManager -> authenticationManager
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 과정에서 발생하는 예외
						.accessDeniedHandler(new CustomAccessDeniedHandler())) // 권한을 확인하는 과정에서 발생한 예외
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class);
					// 현재 필터에서 인증이 정상처리되면 UsernamePasswordAuthenticationFilter 는 자동으로 통과
		return http.build();
	}
}
