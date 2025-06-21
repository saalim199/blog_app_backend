package com.saalimcorp.blog.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDTO {
    private String id;
    private String content;
    private String postId;
    private String authorId;
    private LocalDateTime createdAt;
}
