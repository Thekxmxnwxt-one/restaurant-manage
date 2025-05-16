package com.example.cook.model;

import lombok.Data;

import java.util.List;

@Data
public class ResponseModel<T> {
    private int status;

    private String description;

    private T data;

}
