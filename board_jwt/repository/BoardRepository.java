package com.abcdefg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcdefg.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
