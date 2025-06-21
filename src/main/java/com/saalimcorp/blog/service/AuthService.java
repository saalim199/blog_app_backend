package com.saalimcorp.blog.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saalimcorp.blog.dto.ResponseDTO;
import com.saalimcorp.blog.dto.UserDTO;
import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public ResponseEntity<?> registerUser(UserDTO user) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (user.getUsername() != null && !user.getUsername().isEmpty() &&
                    user.getEmail() != null && !user.getEmail().isEmpty() &&
                    user.getPassword() != null && !user.getPassword().isEmpty() &&
                    user.getConfirmPassword() != null && !user.getConfirmPassword().isEmpty()) {

                if (!user.getPassword().equals(user.getConfirmPassword())) {
                    response.setMessage("Passwords do not match");
                    log.error("Passwords do not match");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                String email = user.getEmail();
                boolean emailChecker = email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$");
                int passworLen = user.getPassword().length();
                if(!emailChecker){
                    response.setMessage("Invalid email");
                    log.error("Invalid email");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if(passworLen<8){
                    response.setMessage("Password should be at least 8 characters");
                    log.error("Password should be at least 8 characters");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                User newUser = new User();
                newUser.setUsername(user.getUsername().trim());
                newUser.setEmail(user.getEmail().trim());
                newUser.setRoles(List.of("USER"));
                newUser.setPassword(passwordEncoder.encode(user.getPassword()));
                newUser.setCreatedAt(java.time.LocalDateTime.now());
                userRepository.save(newUser);
                response.setMessage("Success");
                log.info("User registered successfully: {}", user.getUsername());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                log.error("Invalid user data provided");
                response.setMessage("Invalid user data provided");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (DataIntegrityViolationException e){
            log.error("User data provided already exists");
            response.setMessage("User data provided already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            response.setMessage("Error registering user: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by username: " + e.getMessage());
        }
    }
}
