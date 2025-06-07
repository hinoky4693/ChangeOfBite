package com.ssafy.cob.domain.community.model.dao;

import java.util.List;

import com.ssafy.cob.domain.community.model.dto.Board;
import com.ssafy.cob.domain.community.model.dto.SearchCondition;

public interface BoardDao {
	//전체 커뮤니티 게시글 조회
	public List<Board> selectAll();
	
	//게시글 하나 조회
	public Board selectOne(int id);
	
	//게시글 등록
	public void insertBoard(Board board);
	
	//게시글 삭제
	public int deleteBoard(int id);
	
	//게시글 수정
	public void updateBoard(Board board);
	
	//조회수 증가
	public void updateViewCnt(int id);
	
	//검색
	public List<Board> search(SearchCondition condition);
	
	void insertUserLike(int postId, int userId);
	void deleteUserLike(int postId, int userId);
	boolean hasUserLikedPost(int postId, int userId);
	void incrementLikeCount(int postId);
	void decrementLikeCount(int postId);


	public List<Board> selectLikedPostsByUserId(int userId);
}
