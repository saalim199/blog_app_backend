package com.saalimcorp.blog.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "categories")
@Data
public class Category {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NonNull
    @Indexed(unique = true)
    private String name;

    private String description;

    private LocalDateTime createdAt;

}
