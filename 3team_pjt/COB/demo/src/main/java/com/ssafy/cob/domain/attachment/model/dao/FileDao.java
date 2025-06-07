package com.ssafy.cob.domain.attachment.model.dao;

import com.ssafy.cob.domain.attachment.model.dto.FileDto;

import java.util.List;

public interface FileDao {
    void insertFile(FileDto file);
    List<FileDto> selectFilesByRelated(String relatedType, int relatedId);
    void deleteFilesByRelated(String relatedType, int relatedId);
    void deleteFileById(int id);
}