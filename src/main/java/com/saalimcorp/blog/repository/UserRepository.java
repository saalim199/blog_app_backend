package com.saalimcorp.blog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.saalimcorp.blog.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}
