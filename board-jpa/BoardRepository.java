package com.abcdefg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abcdefg.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
	
	@Query(value="select * from board where write_id = :inName order by write_date desc", nativeQuery=true)
	List<Board> getUserContent(@Param("inName") String name);
}
