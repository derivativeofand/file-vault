package com.jha58.file_vault.file;

import java.time.LocalDateTime;

import com.jha58.file_vault.user.User;

import jakarta.persistence.*;

@Entity
public class FileMetaData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String contentType;
    private LocalDateTime uploadedAt;
    private Long size;
    private String storagePath = "uploads/";

    // Empty constructor for File entity
    public FileMetaData() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setOwner(User user) {
        this.user = user;
    }

    public User getOwner() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
