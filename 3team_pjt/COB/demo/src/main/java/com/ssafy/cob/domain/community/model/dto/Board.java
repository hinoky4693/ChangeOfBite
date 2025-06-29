package com.ssafy.cob.domain.community.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.cob.domain.attachment.model.dto.FileDto;

public class Board {
	private int id;
	private int userId;
	private String writer;
	private String category;
	private String title;
	private String content;
	private int viewCnt;
	private String createdAt;
	private int likes;
	private int dislikes;
	private List<MultipartFile> attachs;
	private List<FileDto> files;
	public Board() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public List<MultipartFile> getAttachs() {
		return attachs;
	}
	public void setAttachs(List<MultipartFile> attachs) {
		this.attachs = attachs;
	}
	public List<FileDto> getFiles() {
		return files;
	}
	public void setFiles(List<FileDto> files) {
		this.files = files;
	}
	@Override
	public String toString() {
		return "Board [id=" + id + ", userId=" + userId + ", writer=" + writer + ", category=" + category + ", title="
				+ title + ", content=" + content + ", viewCnt=" + viewCnt + ", createdAt=" + createdAt + ", likes="
				+ likes + ", dislikes=" + dislikes + ", attachs=" + attachs + ", files=" + files + "]";
	}
	
	
	
}
