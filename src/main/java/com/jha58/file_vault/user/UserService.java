package com.jha58.file_vault.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jha58.file_vault.file.FileRepository;
import com.jha58.file_vault.file.FileMetaData;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        List<FileMetaData> userFiles = fileRepository.findByUser(user);
        for(FileMetaData file : userFiles) {
            try {
                Files.deleteIfExists(Paths.get(file.getStoragePath()));

            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file from disk: " + file.getName(), e);
            }
        }

        fileRepository.deleteAll(userFiles);

        userRepository.deleteById(id);
    }

}
