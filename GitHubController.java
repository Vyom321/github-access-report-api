package com.example.demo.controller;

import com.example.demo.model.UserRepoResponse;
import com.example.demo.service.GitHubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GitHubController {

    private final GitHubService service;

    public GitHubController(GitHubService service) {
        this.service = service;
    }

    @GetMapping("/report")
    public List<UserRepoResponse> generateReport() {
        return service.generateReport();
    }
}
