package com.saalimcorp.blog.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.saalimcorp.blog.entity.Blog;

public interface BlogRepository extends MongoRepository<Blog, ObjectId> {
    List<Blog> findByAuthorId(ObjectId authorId);

}
