package com.saalimcorp.blog.controller;

import java.util.List;

import com.saalimcorp.blog.entity.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saalimcorp.blog.dto.CommentDTO;
import com.saalimcorp.blog.dto.ResponseDTO;
import com.saalimcorp.blog.entity.Blog;
import com.saalimcorp.blog.entity.Category;
import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.service.BlogService;
import com.saalimcorp.blog.service.CategoryService;
import com.saalimcorp.blog.service.CommentService;
import com.saalimcorp.blog.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/blogs")
@AllArgsConstructor
public class BlogController {

    private final CategoryService categoryService;

    private final BlogService blogService;

    private final UserService userService;

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody Blog blog, @RequestParam List<String> categories) {
        ResponseDTO response = new ResponseDTO();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                String username = auth.getName();
                User user = userService.getUserByUsername(username);
                if (user == null) {
                    response.setMessage("User Not Found");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // User not found
                }
                var categoryList = categoryService.getAllCategory();
                List<Category> BlogCategoriesList = categoryList.stream().filter(c -> categories.contains(c.getName()))
                        .toList();
                var createdBlog = blogService.createBlog(blog, user, BlogCategoriesList);
                response.setMessage("Success");
                response.setData(createdBlog);
                return new ResponseEntity<>(response, HttpStatus.CREATED); // Blog created successfully
            }
            response.setMessage("User Not Authenticated");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.setMessage("Error creating blog: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        ResponseDTO response = new ResponseDTO();
        try {
            var blogs = blogService.getAllBlogs();
            if (blogs == null || blogs.isEmpty()) {
                response.setMessage("No Blogs Found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // No blogs found
            }
            response.setMessage("Success");
            response.setData(blogs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error fetching blogs: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getBlogsByCategory(@PathVariable String categoryName) {
        ResponseDTO response = new ResponseDTO();
        try {
            var blogs = blogService.getBlogsByCategoryName(categoryName);
            if (blogs == null) {
                response.setMessage("No Blogs Found for this Category");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // No blogs found for category
            }
            response.setMessage("Success");
            response.setData(blogs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error fetching blogs by category: " + e.getMessage());
            return new ResponseEntity<>(response,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-blogs")
    public ResponseEntity<?> getBlogsByUser() {
        ResponseDTO response = new ResponseDTO();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.getUserByUsername(username);
            var blogs = blogService.getBlogByAuthorId(user.getId());
            if (blogs == null || blogs.isEmpty()) {
                response.setMessage("No Blogs Found for this User");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // No blogs found for user
            }
            response.setMessage("Success");
            response.setData(blogs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error fetching user's blogs: " + e.getMessage());
            return new ResponseEntity<>(response,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<?> deleteBlog(@PathVariable String blogId) {
        ResponseDTO response = new ResponseDTO();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                String username = auth.getName();
                User user = userService.getUserByUsername(username);
                if (user == null) {
                    response.setMessage("User Not Found");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                Blog blog = blogService.getBlogById(blogId);
                if (blog == null) {
                    response.setMessage("Blog Not Found");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                if (!blog.getAuthor().getId().equals(user.getId()) && !user.getRoles().contains("ADMIN")) {
                    response.setMessage("Unauthorized to delete this blog");
                    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
                }
                blogService.deleteBlog(blogId);
                response.setMessage("Success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.setMessage("User Not Authenticated");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.setMessage("Error deleting blog: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<?> getBlogById(@PathVariable String blogId) {
        ResponseDTO response = new ResponseDTO();
        try {
            var blog = blogService.getBlogById(blogId);
            if (blog == null) {
                response.setMessage("Blog Not Found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error fetching blog: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{blogId}/like")
    public ResponseEntity<?> likeBlog(@PathVariable String blogId) {
        ResponseDTO response = new ResponseDTO();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                String username = auth.getName();
                blogService.likeBlog(blogId, username);
                response.setMessage("Success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.setMessage("User Not Authenticated");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.setMessage("Error liking blog: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{blogId}/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentDTO comment, @PathVariable String blogId) {
        ResponseDTO response = new ResponseDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        try {
            commentService.addCommentToBlog(blogId, comment, username);
            response.setMessage("Success");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage("Error adding comment: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{blogId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String blogId, @PathVariable String commentId) {
        ResponseDTO response = new ResponseDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        try {
            commentService.deleteCommentFromBlog(blogId, commentId, username);
            response.setMessage("Success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error deleting comment: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{blogId}/comments")
    public ResponseEntity<?> getCommentByBlogId(@PathVariable String blogId) {
        ResponseDTO response = new ResponseDTO();
        try{
            List<Comment> comments = commentService.findByBlogId(blogId);
            response.setMessage("Success");
            response.setData(comments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
