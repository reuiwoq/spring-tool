package com.abcdefg.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abcdefg.entity.BoardUser;
import com.abcdefg.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public BoardUser create(String username, String email, String password) {
		BoardUser user = new BoardUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return user;
	}
	
	public BoardUser getUser(String username) {
        Optional<BoardUser> boardUser = this.userRepository.findByUsername(username);
        if (boardUser.isPresent()) {
            return boardUser.get();
        } else {
            throw new DataNotFoundException("boarduser not found");
        }
    }
}
