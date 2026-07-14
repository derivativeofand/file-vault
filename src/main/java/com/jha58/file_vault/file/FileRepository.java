package com.jha58.file_vault.file;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {
    FileMetaData findByName(String name);
    List<FileMetaData> findByContentType(String contentType);
}