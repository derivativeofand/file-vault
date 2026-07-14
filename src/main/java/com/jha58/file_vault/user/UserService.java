package com.jha58.file_vault.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User updateUser()
}
