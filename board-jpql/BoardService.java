package com.abcdefg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.abcdefg.entity.Board;
import com.abcdefg.entity.QBoard;
import com.abcdefg.repository.BoardRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
private final BoardRepository boardRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Board> listArticles() throws DataAccessException {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QBoard board = QBoard.board;
		return queryFactory.selectFrom(board).orderBy(board.writeDate.desc()).fetch();
	}
	
	public void addArticle(Board board) throws DataAccessException {
		boardRepository.save(board);
	}
	
	public Board viewArticle(int articleNo) throws DataAccessException {
		QBoard board = QBoard.board;
		Predicate predicate = board.id.eq(articleNo);
		Board data = null;
		Optional<Board> optionalBoard = boardRepository.findOne(predicate);
		if(optionalBoard.isPresent()) {
			data = optionalBoard.get();
		}
		return data;
	}
	
	public void editArticle(Board inBoard) throws DataAccessException{
		QBoard board = QBoard.board;
		Predicate predicate = board.id.eq(inBoard.getId());
		Board data = null;
		Optional<Board> optionalBoard = boardRepository.findOne(predicate);

		if(optionalBoard.isPresent()) {
			data = optionalBoard.get();
			data.setTitle(inBoard.getTitle());
			data.setContent(inBoard.getContent());
			boardRepository.save(data);
		}
	}
	
	public void removeArticle(int articleNo) throws DataAccessException{
		boardRepository.deleteById(articleNo);
	}
}
