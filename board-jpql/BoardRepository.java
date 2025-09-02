package com.abcdefg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.abcdefg.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>, QuerydslPredicateExecutor <Board>{
	
}
