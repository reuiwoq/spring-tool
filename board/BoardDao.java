package com.abcdefg.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.abcdefg.model.NoticeBoard;

@Mapper
@Repository
public interface BoardDao {
	public List<NoticeBoard> selectAllArticles() throws DataAccessException;
	public void insertArticle(NoticeBoard noticeBoard)throws DataAccessException;
	public NoticeBoard selectArticle(int articleNo)throws DataAccessException;
	public void updateArticle(NoticeBoard noticeBoard)throws DataAccessException;
	public void deleteArticle(int articleNo)throws DataAccessException;
}
