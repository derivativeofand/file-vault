package com.jha58.file_vault.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jha58.file_vault.user.User;
import com.jha58.file_vault.user.UserRepository;
import com.jha58.file_vault.rag.RagService;
import org.springframework.ai.vectorstore.VectorStore;

import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private RagService ragService;

    @Autowired 
    private VectorStore vectorStore;

    public List<FileMetaData> getAllFiles() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return fileRepository.findByUser(owner);
    }

    public FileMetaData getFileById(Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        FileMetaData file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Safeguard for the case where the file's owner is not the currently authenticated user
        if (!file.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access Denied");
        }

        return file;
    }

    public FileMetaData uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        Path uploadDir = Paths.get("uploads");

        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Get the currently authenticated user
        String username = SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();


        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Path storagePath = Paths.get("uploads/" + uniqueFileName);

        Files.copy(file.getInputStream(), storagePath);

        // Create and save FileMetaData
        FileMetaData fileMetaData = new FileMetaData();
        fileMetaData.setName(file.getOriginalFilename());
        fileMetaData.setContentType(file.getContentType());
        fileMetaData.setSize(file.getSize());
        fileMetaData.setOwner(user);
        fileMetaData.setStoragePath(storagePath.toString());
        fileMetaData.setUploadedAt(java.time.LocalDateTime.now());
        
        // Save the file metadata to the database
        FileMetaData savedFileMetaData = fileRepository.save(fileMetaData);

        // Process the file with RagService
        ragService.processFile(fileMetaData, file);

        return savedFileMetaData;
    }

    public FileMetaData updateFile(Long id, FileMetaData updatedFile) {
        FileMetaData existingFile = getFileById(id);
        existingFile.setName(updatedFile.getName());
        existingFile.setContentType(updatedFile.getContentType());
        return fileRepository.save(existingFile);
    }

    public void deleteFile(Long id) throws IOException {
        String username = SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
                        
        User currentUser = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        FileMetaData file = fileRepository.findById(id).orElseThrow(() 
        -> new RuntimeException("File not found")); 

        boolean isAdmin = currentUser.getRole().contains("ADMIN");
        boolean isOwner = file.getOwner().getUsername().equals(username);

        // Safeguard for the case where the file's owner is not the currently authenticated user
        if(!isOwner && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        Files.deleteIfExists(Paths.get(file.getStoragePath()));
        fileRepository.deleteById(id);
        ragService.deleteVectorsByFileId(id);
    }

    public void deleteFilesByUser(User user) throws IOException {
        Long id  = user.getId();

        List<FileMetaData> userFiles = fileRepository.findByUser(user);
        for(FileMetaData file : userFiles) {
            try {
                Files.deleteIfExists(Paths.get(file.getStoragePath()));

            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file from disk: " + file.getName(), e);
            }
        }

        fileRepository.deleteAll(userFiles);
        ragService.deleteVectorsByUserId(id); 
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

    public List<FileMetaData> getAllFilesAdmin() {
        return fileRepository.findAll();
    }
}
