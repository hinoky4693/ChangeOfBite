package com.ssafy.cob.domain.community.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.cob.domain.attachment.model.dao.FileDao;
import com.ssafy.cob.domain.community.model.dto.Board;
import com.ssafy.cob.domain.community.service.BoardService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class BoardRestController {
	private final BoardService boardService;

	public BoardRestController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@Autowired
    private FileDao fileDao;

	// 게시글 전체조회
	@GetMapping("/posts")
	public ResponseEntity<List<Board>> list() {
		List<Board> list = boardService.getBoardList();
		return ResponseEntity.ok(list);
	}

	// 게시글 상세조회
	@GetMapping("/posts/{postId}")
	public ResponseEntity<Board> detail(@PathVariable("postId") int id) {
		Board board = boardService.readBoard(id);
		if (board != null) {
			return ResponseEntity.ok(board);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	// 게시글 등록
	@PostMapping("/posts")
	public ResponseEntity<String> write(@ModelAttribute Board board,
	                                    @RequestPart(value = "attachs", required = false) List<MultipartFile> files) {
		boardService.writeBoard(board, board.getAttachs()); // 파일 포함 저장
	    return new ResponseEntity<>("게시글 등록 완료", HttpStatus.CREATED);
	}

	// 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<Void> delete(@PathVariable("postId") int id) {
		boolean isDeleted = boardService.removeBoard(id);

		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	// 게시글 수정
	@PutMapping("/posts/{postId}")
	public ResponseEntity<Void> update(@PathVariable("postId") int id,
	                                   @RequestParam("title") String title,
	                                   @RequestParam("content") String content,
	                                   @RequestParam("category") String category,
	                                   @RequestParam("userId") int userId,
	                                   @RequestPart(value = "files", required = false) List<MultipartFile> files) {

	    Board board = new Board();
	    board.setId(id);
	    board.setTitle(title);
	    board.setContent(content);
	    board.setCategory(category);
	    board.setUserId(userId);

	    boardService.modifyBoard(board);
	    fileDao.deleteFilesByRelated("POST", id);
	    boardService.saveFiles("POST", id, files);
	    return ResponseEntity.ok().build();
	}
	
	@GetMapping("/posts/liked")
	public ResponseEntity<List<Board>> likedPosts(@RequestParam("userId") int userId) {
	    List<Board> liked = boardService.getLikedPostsByUserId(userId);
	    return ResponseEntity.ok(liked);
	}

	
	@PostMapping("/posts/{postId}/like")
	public ResponseEntity<Void> likePost(@PathVariable int postId, @RequestBody Map<String, Integer> body) {
	    int userId = body.get("userId");
	    boardService.likePost(postId, userId);
	    return ResponseEntity.ok().build();
	}

	@PostMapping("/posts/{postId}/dislike")
	public ResponseEntity<Void> dislikePost(@PathVariable int postId, @RequestBody Map<String, Integer> body) {
	    int userId = body.get("userId");
	    boardService.dislikePost(postId, userId);
	    return ResponseEntity.ok().build();
	}

	@GetMapping("/posts/{postId}/like/check")
	public ResponseEntity<Map<String, Boolean>> checkLike(@PathVariable int postId, @RequestParam int userId) {
	    boolean liked = boardService.hasUserLikedPost(postId, userId);
	    return ResponseEntity.ok(Collections.singletonMap("liked", liked));
	}

	
	
	
	

}
