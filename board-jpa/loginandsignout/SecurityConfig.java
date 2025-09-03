package com.abcdefg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
	private static final String[] PERMIT_ALL_PATTERNS = {"/**"};
//	private static final String[] LOGOUT = {"/user/logout"};
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequest) -> authorizeHttpRequest
				.requestMatchers(PERMIT_ALL_PATTERNS).permitAll()) // '/'밑에 모두 허용
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.csrf((csrf) -> csrf.ignoringRequestMatchers(PERMIT_ALL_PATTERNS)) // csrf도 허용
				.formLogin(formLogin -> formLogin.loginPage("/user/login").defaultSuccessUrl("/notice/list"))
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
						.logoutSuccessUrl("/notice/list").invalidateHttpSession(true));
				
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
