package com.saalimcorp.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                log.error("User not found with username: {}", username);
                return null;
            }
            return user;
        } catch (Exception e) {
            log.error("Error retrieving user by username: {}", e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                log.info("No users found in the database.");
            }
            return users;
        } catch (Exception e) {
            log.error("Error retrieving all users: {}", e.getMessage());
            return null;
        }
    }
}
