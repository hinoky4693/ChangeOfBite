package com.ssafy.cob.domain.community.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.cob.domain.community.model.dto.Board;
import com.ssafy.cob.domain.community.model.dto.SearchCondition;

public interface BoardService {
	public List<Board> getBoardList();
	
	//클릭시 조회수 증가
	public Board readBoard(int id);
	
	//클릭시 조회수 증가xx
	public Board getBoard(int id);
	
	public void writeBoard(Board board, List<MultipartFile> files);
	
	public boolean removeBoard(int id);
	
	public void modifyBoard(Board board);
	
	public List<Board> search(SearchCondition condition);
	
	void likePost(int postId, int userId);
	void dislikePost(int postId, int userId);
	boolean hasUserLikedPost(int postId, int userId);

	
	public void saveFiles(String relatedType, int relatedId, List<MultipartFile> files);
	
	public List<Board> getLikedPostsByUserId(int userId);

}
