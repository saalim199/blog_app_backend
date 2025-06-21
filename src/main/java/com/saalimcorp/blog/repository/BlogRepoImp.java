package com.saalimcorp.blog.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.saalimcorp.blog.entity.Blog;
import com.saalimcorp.blog.entity.Category;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class BlogRepoImp {

    private final MongoTemplate mongoTemplate;

    public List<Blog> getBlogsByCategory(Category category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("categories").in(category));
        return mongoTemplate.find(query, Blog.class);
    }
}
