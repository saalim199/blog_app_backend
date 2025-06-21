package com.saalimcorp.blog.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

@Document(collection = "blogs")
@Data
public class Blog {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String title;

    private String content;

    @DBRef
    private User author;

    @DBRef
    private List<Category> categories = new ArrayList<>();

    @DBRef
    private List<User> likedBy = new ArrayList<>();

    @DBRef
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;
}
