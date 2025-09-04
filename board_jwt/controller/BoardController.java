package com.abcdefg.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcdefg.entity.Board;
import com.abcdefg.Service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final com.abcdefg.Service.BoardService boardService;
	
	@GetMapping("/get/{num}")
	public Board getArticle(@PathVariable("num") int articleNo) throws Exception {
		Board board = boardService.getArticle(articleNo);
		return board;
	}
}
