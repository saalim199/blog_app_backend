package com.saalimcorp.blog.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private String message;
    private Object data;
    private String token;
}
