package com.jha58.file_vault.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jha58.file_vault.user.User;
import com.jha58.file_vault.user.UserRepository;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FileMetaData> getAllFiles() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        return fileRepository.findByUser(owner);
    }

    public FileMetaData getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + id));
    }

    public FileMetaData uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        Path uploadDir = Paths.get("uploads");

        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String username = SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Path storagePath = Paths.get("uploads/" + uniqueFileName);

        Files.copy(file.getInputStream(), storagePath);

        FileMetaData fileMetaData = new FileMetaData();
        fileMetaData.setName(file.getOriginalFilename());
        fileMetaData.setContentType(file.getContentType());
        fileMetaData.setSize(file.getSize());
        fileMetaData.setOwner(user);
        fileMetaData.setStoragePath(storagePath.toString());
        fileMetaData.setUploadedAt(java.time.LocalDateTime.now());
        return fileRepository.save(fileMetaData);
    }

    public FileMetaData updateFile(Long id, FileMetaData updatedFile) {
        FileMetaData existingFile = getFileById(id);
        existingFile.setName(updatedFile.getName());
        existingFile.setContentType(updatedFile.getContentType());
        return fileRepository.save(existingFile);
    }

    public void deleteFile(Long id) throws IOException {
        FileMetaData file = fileRepository.findById(id).orElseThrow(() 
        -> new RuntimeException("File not found with id: " + id));

        Path storagePath = Paths.get(file.getStoragePath());
        Files.deleteIfExists(storagePath);

        fileRepository.deleteById(id);
    }

    public FileMetaData getFileByName(String fileName) {
        return fileRepository.findByName(fileName);
    }

    public List<FileMetaData> getFilesByContentType(String contentType) {
        String username = SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return fileRepository.findByContentType(contentType, user);
    }
}
