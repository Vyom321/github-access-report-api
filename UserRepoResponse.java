package com.example.demo.model;

import java.util.List;

public class UserRepoResponse {

    private String username;
    private List<String> repositories;

    public UserRepoResponse(String username, List<String> repositories) {
        this.username = username;
        this.repositories = repositories;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRepositories() {
        return repositories;
    }
}