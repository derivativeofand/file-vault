package com.jha58.file_vault.admin;

import com.jha58.file_vault.file.FileMetaData;
import com.jha58.file_vault.file.FileService;
import com.jha58.file_vault.user.User;
import com.jha58.file_vault.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;

    @Autowired 
    private FileService fileService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping ("/users/{id}")
    public void deleteUser(@PathVariable Long id) { 
        userService.deleteUser(id);
    }

    @GetMapping("/files")
    public List<FileMetaData> getAllFiles() {
        return fileService.getAllFilesAdmin();
    }

    @DeleteMapping("/files/{id}")
    public void deleteFile(@PathVariable Long id) throws IOException {
        fileService.deleteFile(id);
    }
}
