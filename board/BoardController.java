package com.abcdefg.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abcdefg.model.NoticeBoard;
import com.abcdefg.service.BoardService;

@Controller
@RequestMapping("notice")
public class BoardController {
	private final BoardService boardService;
	private NoticeBoard noticeBoard;
	private List<NoticeBoard> articleList;
	
	Logger logger = LoggerFactory.getLogger("com.abcdefg.controller.BoardController");
	
	public BoardController(BoardService boardService, NoticeBoard noticeBoard) {
		this.boardService = boardService;
		this.noticeBoard = noticeBoard;
	}
	
	@GetMapping({"/list", "/"})
	public String getArticleList(Model model) {
		logger.debug("===>getArticleList DEBUG레벨 호출");
		logger.info("===>getArticleList 호출");
		articleList = boardService.listArticles();
		model.addAttribute("dataList", articleList);
		return "list";
	}
	
	@GetMapping("/add")
	public String writeArticle() {
		return "write";
	}
	
	@PostMapping(value = "/addarticle")
	public String addArticle(@RequestParam(value="i_title") String title, 
		@RequestParam(value="i_content") String content) {
		noticeBoard.setTitle(title);
		noticeBoard.setContent(content);
		noticeBoard.setWriteId("bit");
		boardService.addArticle(noticeBoard);
		
		return "redirect:list"; //위의 '@Getmapping({"/list", "/"})'의 주소
	}
	
	@GetMapping("/view")
	public ModelAndView viewArticle(@RequestParam(value="no") String articleNo) {
		logger.info("viewArticle 호출");
		logger.debug("viewArticle ==> articleNo:" + articleNo);
		noticeBoard = boardService.viewArticle(Integer.parseInt(articleNo));
		ModelAndView mv = new ModelAndView();
		mv.setViewName("view");
		mv.addObject("article", noticeBoard);
		return mv;
	}
	
	@GetMapping("/viewjson")
	@ResponseBody
	public NoticeBoard getArticle(@RequestParam(value="no") String articleNo) {
		noticeBoard = boardService.viewArticle(Integer.parseInt(articleNo));
		
		return noticeBoard;
	}
	
	@PostMapping("/edit")
	public String editArticle(@RequestParam String articleNo, @RequestParam String title, 
		@RequestParam(value="content") String content, RedirectAttributes redirectAttr) {
		noticeBoard.setArticleNo(Integer.parseInt(articleNo));
		noticeBoard.setTitle(title);
		noticeBoard.setContent(content);
		boardService.editArticle(noticeBoard);
		redirectAttr.addAttribute("no", articleNo);
		return "redirect:view";
	}

	@PostMapping("/remove")
	public String removeArticle(@RequestParam String articleNo) {
		boardService.removeArticle(Integer.parseInt(articleNo));
		return "redirect:list";
	}
	
	@GetMapping("/apiadd")
	public String apiAdd() {
		return "create";
	}
}
