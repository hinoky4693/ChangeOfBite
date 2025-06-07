package com.ssafy.cob.domain.community.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.cob.domain.attachment.model.dao.FileDao;
import com.ssafy.cob.domain.attachment.model.dto.FileDto;
import com.ssafy.cob.domain.community.model.dao.BoardDao;
import com.ssafy.cob.domain.community.model.dto.Board;
import com.ssafy.cob.domain.community.model.dto.SearchCondition;

@Service
public class BoardServiceImpl implements BoardService {

	private BoardDao boardDao;
	
	@Autowired
	private FileDao fileDao;

	private final String uploadPath = new File("src/main/resources/uploads").getAbsolutePath();

	
	public BoardServiceImpl (BoardDao boardDao) {
		this.boardDao = boardDao;
	}
	
	@Override
	public List<Board> getBoardList() {
		return boardDao.selectAll();
	}
	
	@Transactional
	@Override
	public Board readBoard(int id) {
		boardDao.updateViewCnt(id);
		return boardDao.selectOne(id);
	}

	@Override
	public Board getBoard(int id) {
		return boardDao.selectOne(id);
	}

	@Transactional
	@Override
	public void writeBoard(Board board, List<MultipartFile> files) {
	    boardDao.insertBoard(board);  // 게시글 저장
	    saveFiles("POST", board.getId(), files); // 파일 저장
	}

	@Transactional
	@Override
	public boolean removeBoard(int id) {
		fileDao.deleteFilesByRelated("POST", id);  // 첨부파일 삭제
	    int result = boardDao.deleteBoard(id);
	    return result == 1;
	}

	@Transactional
	@Override
	public void modifyBoard(Board board) {
		boardDao.updateBoard(board);
	}

	@Override
	public List<Board> search(SearchCondition condition) {
		String word = condition.getWord();
		return boardDao.search(condition);
	}
	

	@Override
	public void likePost(int postId, int userId) {
	    if (!boardDao.hasUserLikedPost(postId, userId)) {
	        boardDao.insertUserLike(postId, userId);
	        boardDao.incrementLikeCount(postId); // board.likes += 1
	    }
	}

	@Override
	public void dislikePost(int postId, int userId) {
	    if (boardDao.hasUserLikedPost(postId, userId)) {
	        boardDao.deleteUserLike(postId, userId);
	        boardDao.decrementLikeCount(postId); // board.likes -= 1
	    }
	}

	@Override
	public boolean hasUserLikedPost(int postId, int userId) {
	    return boardDao.hasUserLikedPost(postId, userId);
	}


	@Override
	public void saveFiles(String relatedType, int relatedId, List<MultipartFile> files) {
	    if (files == null || files.isEmpty()) return;

	    for (MultipartFile file : files) {
	        if (!file.isEmpty()) {
	            try {
	                String uuid = UUID.randomUUID().toString();
	                String originalName = file.getOriginalFilename();
	                String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
	                String savedFileName = uuid + "." + extension;

	                String fullPath = uploadPath + File.separator + savedFileName;
	                File dest = new File(fullPath);
	                dest.getParentFile().mkdirs();
	                file.transferTo(dest);

	                FileDto fileDto = new FileDto();
	                fileDto.setRelatedType(relatedType);
	                fileDto.setRelatedId(relatedId);

	                // ✅ 여기만 수정
	                // DB에는 브라우저에서 접근 가능한 URL로 저장해야 함
	                String urlPath = "/uploads/" + savedFileName;
	                fileDto.setFilePath(urlPath); // ✅ 이 부분이 핵심

	                fileDto.setOriginalName(originalName);
	                fileDto.setFileSize(file.getSize());
	                fileDto.setMimeType(file.getContentType());
	                fileDto.setFileExtension(extension);

	                fileDao.insertFile(fileDto);

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	@Override
	public List<Board> getLikedPostsByUserId(int userId) {
	    return boardDao.selectLikedPostsByUserId(userId);
	}


}
