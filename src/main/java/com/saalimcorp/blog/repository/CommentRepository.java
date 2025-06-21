package com.saalimcorp.blog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.saalimcorp.blog.entity.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    List<Comment> findByPostId(ObjectId id);
}
