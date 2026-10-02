package com.jha58.file_vault.file;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jha58.file_vault.rag.TextExtractorService;
import com.jha58.file_vault.rag.TextChunker;
import com.jha58.file_vault.rag.RagService;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/files")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    @Autowired 
    private RagService ragService;

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
    public FileMetaData uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) throws IOException {
        fileService.deleteFile(id);
    }
    
}
