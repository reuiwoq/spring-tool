package com.abcdefg.Service;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.abcdefg.entity.Board;
import com.abcdefg.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	
	public Board getArticle(int articleNo) throws DataAccessException {
		Optional<Board> _board = boardRepository.findById(articleNo);
		Board board = null;
		if (_board.isPresent()) {
			board = _board.get();
		}
		return board;
	}
}
