package com.abcdefg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abcdefg.entity.BoardUser;
import com.abcdefg.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService{
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<BoardUser>_boardUser = this.userRepository.findByUsername(username);
		if (_boardUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		BoardUser boardUser = _boardUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if ("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIIN.getRole()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getRole()));
		}
		
		return new User(boardUser.getUsername(), boardUser.getPassword(), authorities);
	}
	
	
}
