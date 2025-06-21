package com.saalimcorp.blog.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "comments")
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @JsonSerialize(using = ToStringSerializer.class)
    @NonNull
    private ObjectId postId;

    @JsonSerialize(using = ToStringSerializer.class)
    @NonNull
    private ObjectId authorId;

    @NonNull
    private String authorName;

    @NonNull
    private String content;

    private LocalDateTime createdAt;

}
