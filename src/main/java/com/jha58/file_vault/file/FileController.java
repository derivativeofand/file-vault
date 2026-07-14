package com.jha58.file_vault.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/uploads")
public class FileController {
    
    @Autowired
    private FileService fileService;

    @GetMapping
    public List<FileMetaData> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{id}")
    public FileMetaData getFileById(@PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @GetMapping("/filter") 
    public List<FileMetaData> getFilesByContentType(@RequestParam String contentType) {
        return fileService.getFilesByContentType(contentType);
    }

    @PostMapping
    public FileMetaData uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }
    
    // @PutMapping("/{id}")
    // public FileMetaData updateFile(@PathVariable Long id, @RequestBody FileMetaData file) {
    //     return fileService.updateFile(id, file);
    // }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) throws IOException {
        fileService.deleteFile(id);
    }
}
