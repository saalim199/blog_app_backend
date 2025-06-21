package com.saalimcorp.blog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.saalimcorp.blog.entity.Category;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    Category findByName(String name);
}
