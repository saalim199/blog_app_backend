package com.saalimcorp.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saalimcorp.blog.dto.CommentDTO;
import com.saalimcorp.blog.entity.Blog;
import com.saalimcorp.blog.entity.Comment;
import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.repository.BlogRepository;
import com.saalimcorp.blog.repository.CommentRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final BlogRepository blogRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public void addCommentToBlog(String blogId, CommentDTO comment, String username) {
        try {
            if (comment == null || comment.getContent() == null || comment.getContent().isEmpty()) {
                log.warn("Comment content cannot be null or empty");
                throw new IllegalArgumentException("Comment content cannot be null or empty");
            }
            ObjectId id = new ObjectId(blogId);
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
            User user = userService.getUserByUsername(username);
            if (user == null) {
                log.warn("User with username {} not found", username);
                throw new IllegalArgumentException("User not found");
            }

            Comment Newcomment = new Comment();
            Newcomment.setAuthorId(user.getId());
            Newcomment.setPostId(blog.getId());
            Newcomment.setContent(comment.getContent().trim());
            Newcomment.setCreatedAt(LocalDateTime.now());
            Newcomment.setAuthorName(username);
            commentRepository.save(Newcomment);
            blog.getComments().add(Newcomment);
            blogRepository.save(blog);
        } catch (Exception e) {
            log.error("Error adding comment to blog with ID {}: {}", blogId, e.getMessage());
            throw new RuntimeException("Error adding comment: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCommentFromBlog(String blogId, String commentId, String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                log.warn("User with username {} not found", username);
                throw new IllegalArgumentException("User not found");
            }
            if (blogId == null || commentId == null) {
                log.warn("Blog ID and Comment ID cannot be null");
                throw new IllegalArgumentException("Blog ID and Comment ID cannot be null");
            }
            ObjectId blogObjectId = new ObjectId(blogId);
            ObjectId commentObjectId = new ObjectId(commentId);
            Blog blog = blogRepository.findById(blogObjectId)
                    .orElseThrow(() -> new IllegalArgumentException("Blog not found"));
            Comment comment = commentRepository.findById(commentObjectId)
                    .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            if (!blog.getAuthor().getId().equals(user.getId()) && !user.getRoles().contains("ADMIN")
                    && !comment.getAuthorId().equals(user.getId())) {
                log.warn("User {} is not authorized to delete this comment", username);
                throw new IllegalArgumentException("Unauthorized to delete this comment");
            }
            blog.getComments().remove(comment);
            blogRepository.save(blog);
            commentRepository.delete(comment);
        } catch (Exception e) {
            log.error("Error deleting comment with ID {} from blog with ID {}: {}", commentId, blogId, e.getMessage());
            throw new RuntimeException("Error deleting comment: " + e.getMessage());
        }
    }

    public List<Comment> findByBlogId(String id) {
        try{
            ObjectId idObject = new ObjectId(id);
            return commentRepository.findByPostId(idObject);
        }catch(Exception e) {
            log.error("Error finding comment with ID {} from blog with ID {}: {}", id, id, e.getMessage());
            throw new RuntimeException("Error finding comment: " + e.getMessage());
        }
    }
}
