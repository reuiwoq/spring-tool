package com.abcdefg;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abcdefg.entity.Board;
import com.abcdefg.repository.BoardRepository;
import com.abcdefg.service.BoardService;
import com.abcdefg.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	private final UserService userService;
	
	
	private List<Board> articleList;
	
	@GetMapping({"/list", "/"})
	public String getArticleList(Model model) {
		articleList = boardService.listArticles();
		model.addAttribute("dataList", articleList);
		return "list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping({"/add", ""})
	public String writeArticle() {
		return "write";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/addarticle")
	public String addArticle(@RequestParam(value="i_title") String title, 
		@RequestParam(value="i_content") String content, Principal principal) {
		Board board = new Board();
		board.setTitle(title);
		board.setContent(content);
		board.setWriter(userService.getUser(principal.getName()));
		boardService.addArticle(board);
		
		return "redirect:list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/remove")
	public String removeArticle(@RequestParam String articleNo) {
		boardService.removeArticle(Integer.parseInt(articleNo));
		return "redirect:list";
	}
	
	@GetMapping("/view")
	public ModelAndView viewArticle(@RequestParam(value="no") String articleNo) {
		Board board = new Board(); //추가
		board = boardService.viewArticle(Integer.parseInt(articleNo));
		ModelAndView mv = new ModelAndView();
		mv.setViewName("view");
		mv.addObject("article", board);
		return mv;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/edit")
	public String editArticle(@RequestParam String articleNo, @RequestParam String title, 
		@RequestParam(value="content") String content, RedirectAttributes redirectAttr) {
		Board board = new Board();
		board.setId(Integer.parseInt(articleNo)); //article이 아닌 id로 바꿔야함
		board.setTitle(title);
		board.setContent(content);
		boardService.editArticle(board);
		redirectAttr.addAttribute("no", articleNo);
		return "redirect:view";
	}
}
