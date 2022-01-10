package com.example.letflix.model;

public class PostResponse {
    public boolean status;
    public String message;

    @Override
    public String toString() {
        return "PostResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
