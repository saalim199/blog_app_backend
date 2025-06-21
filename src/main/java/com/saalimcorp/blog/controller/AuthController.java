package com.saalimcorp.blog.controller;

import com.saalimcorp.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.saalimcorp.blog.dto.ResponseDTO;
import com.saalimcorp.blog.dto.UserDTO;
import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.service.AuthService;
import com.saalimcorp.blog.service.JwtService;
import com.saalimcorp.blog.service.UserDetailsServiceImp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserDetailsServiceImp userDetailsServiceImp;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO user) {
        try {
            ResponseDTO response = new ResponseDTO();
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(user.getUsername());
            String token = jwtService.generateToken(userDetails.getUsername());
            User userDB = authService.getUserByUsername(user.getUsername());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userDB.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setEmail(user.getEmail());
            userDTO.setToken(token);
            userDTO.setRoles(user.getRoles());
            response.setMessage("Success");
            response.setData(userDTO);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Error logging in user: {}", e.getMessage());
            ResponseDTO authResponse = new ResponseDTO();
            authResponse.setMessage("Invalid username or password");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userService.getUserByUsername(authentication.getName());
            responseDTO.setMessage("Success");
            responseDTO.setData(user);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage("User not logged in");
            return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
        }
    }

}
