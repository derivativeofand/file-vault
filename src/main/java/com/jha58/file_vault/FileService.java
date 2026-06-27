package com.jha58.file_vault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    
    @Autowired
    private FileRepository fileRepository;

    public List<FileMetaData> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileMetaData getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + id));
    }

    public FileMetaData uploadFile(MultipartFile file) throws IOException {
        FileMetaData fileMetaData = new FileMetaData();
        fileMetaData.setName(file.getOriginalFilename());
        fileMetaData.setContentType(file.getContentType());
        fileMetaData.setSize(file.getSize());
        fileMetaData.setUploadedAt(java.time.LocalDateTime.now());
        fileMetaData.setStoragePath("C:/file_vault_storage/" + file.getOriginalFilename());
        return fileRepository.save(fileMetaData);
    }

    public FileMetaData updateFile(Long id, FileMetaData updatedFile) {
        FileMetaData existingFile = getFileById(id);
        existingFile.setName(updatedFile.getName());
        existingFile.setContentType(updatedFile.getContentType());
        return fileRepository.save(existingFile);
    }

    public void deleteFile(Long id) {
        FileMetaData existingFile = getFileById(id);
        fileRepository.delete(existingFile);
    }

    public FileMetaData getFileByName(String fileName) {
        return fileRepository.findByFileName(fileName);
    }
}
