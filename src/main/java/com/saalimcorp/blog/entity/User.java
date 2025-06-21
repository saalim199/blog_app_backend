package com.saalimcorp.blog.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
public class User {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NonNull
    @Indexed(unique = true)
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private List<String> roles;

    private LocalDateTime createdAt;

}
