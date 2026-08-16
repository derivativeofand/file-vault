package com.jha58.file_vault.file;

import java.util.List;
import com.jha58.file_vault.user.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {
    FileMetaData findByName(String name);
    List<FileMetaData> findByContentType(String contentType, User user);
    List<FileMetaData> findByUser(User user);
}