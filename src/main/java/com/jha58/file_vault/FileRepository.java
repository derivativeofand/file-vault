package com.jha58.file_vault;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {
    FileMetaData findByFileName(String fileName);
}
