package com.saalimcorp.blog.service;

import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.saalimcorp.blog.entity.Blog;
import com.saalimcorp.blog.entity.Category;
import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.repository.BlogRepoImp;
import com.saalimcorp.blog.repository.BlogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class BlogService {

    private final BlogRepository blogRepository;
    private final CategoryService categoryService;
    private final BlogRepoImp blogRepoImp;
    private final UserService userService;

    public List<Blog> getAllBlogs() {
        try {
            return blogRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching blogs: {}", e.getMessage());
            return null;
        }
    }

    public Blog getBlogById(String id) {
        try {
            ObjectId blogId = new ObjectId(id);
            return blogRepository.findById(blogId).orElse(null);
        } catch (Exception e) {
            log.error("Error fetching blog with ID {}: {}", id, e.getMessage());
            return null;
        }
    }

    public List<Blog> getBlogByAuthorId(ObjectId authorId) {
        try {
            return blogRepository.findByAuthorId(authorId);
        } catch (Exception e) {
            log.error("Error fetching blogs by author ID {}: {}", authorId, e.getMessage());
            return null;
        }
    }

    public Blog createBlog(Blog blog, User author, List<Category> categories) {
        try {
            if (blog != null && blog.getTitle() != null && !blog.getTitle().isEmpty()
                    && blog.getContent() != null && !blog.getContent().isEmpty()) {
                blog.setContent(blog.getContent().trim());
                blog.setAuthor(author);
                blog.setCreatedAt(LocalDateTime.now());
                blog.setCategories(categories);
                return blogRepository.save(blog);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error creating blog: {}", e.getMessage());
            return null;
        }
    }

    public List<Blog> getBlogsByCategoryName(String categoryName) {
        try {
            var category = categoryService.getCategoryByName(categoryName);
            if (category == null) {
                log.warn("Category with name {} not found", categoryName);
                return null;
            }
            return blogRepoImp.getBlogsByCategory(category);
        } catch (Exception e) {
            log.error("Error fetching blogs by category ID {}: {}", categoryName, e.getMessage());
            return null;
        }
    }

    public List<Blog> getBlogsByUser(ObjectId userId) {
        try {
            return blogRepository.findByAuthorId(userId);
        } catch (Exception e) {
            log.error("Error fetching blogs by user ID {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public boolean deleteBlog(String id) {
        try {
            ObjectId blogId = new ObjectId(id);
            if (blogRepository.existsById(blogId)) {
                blogRepository.deleteById(blogId);
                return true;
            } else {
                log.warn("Blog with ID {} not found", id);
                throw new IllegalArgumentException("Blog not found");
            }
        } catch (Exception e) {
            log.error("Error deleting blog with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting blog: " + e.getMessage());
        }
    }

    public void likeBlog(String blogId, String username) {
        try {
            ObjectId id = new ObjectId(blogId);
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
            User user = userService.getUserByUsername(username);
            if (user == null) {
                log.warn("User with username {} not found", username);
                throw new IllegalArgumentException("User not found");
            }
            boolean isLiked = blog.getLikedBy().removeIf(likedUser -> likedUser.getId().equals(user.getId()));
            if (!isLiked) {
                blog.getLikedBy().add(user);
            }
            blogRepository.save(blog);
        } catch (Exception e) {
            log.error("Error liking blog with ID {}: {}", blogId, e.getMessage());
            throw new RuntimeException("Error liking blog: " + e.getMessage());
        }
    }
}
