package com.saalimcorp.blog.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

@Data
public class UserDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String token;
    private String username;
    private String email;
    private List<String> roles; // e.g., "USER", "ADMIN"
    private String password;
    private String confirmPassword; // For registration purposes
    private LocalDateTime createdAt;
}
