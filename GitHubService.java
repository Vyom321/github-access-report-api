package com.example.demo.service;

import com.example.demo.model.UserRepoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GitHubService {

    private final WebClient webClient;

    @Value("${github.org}")
    private String org;

    public GitHubService(WebClient webClient) {
        this.webClient = webClient;
    }

    // ✅ THIS METHOD MUST EXIST
    public List<UserRepoResponse> generateReport() {

        Map<String, List<String>> userRepoMap = new ConcurrentHashMap<>();

        List<Map<String, Object>> repos = fetchAllPages("/orgs/{org}/repos", org);

        if (repos.isEmpty()) return new ArrayList<>();

        Flux.fromIterable(repos)
                .flatMap(repo -> {
                    String repoName = (String) repo.get("name");

                    return fetchAllPagesAsync("/repos/{org}/{repo}/collaborators", org, repoName)
                            .doOnNext(users -> {
                                for (Map<String, Object> user : users) {
                                    String username = (String) user.get("login");

                                    userRepoMap
                                            .computeIfAbsent(username, k -> Collections.synchronizedList(new ArrayList<>()))
                                            .add(repoName);
                                }
                            });
                }, 5)
                .then()
                .block();

        List<UserRepoResponse> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : userRepoMap.entrySet()) {
            result.add(new UserRepoResponse(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    private List<Map<String, Object>> fetchAllPages(String uri, Object... params) {
        List<Map<String, Object>> all = new ArrayList<>();
        int page = 1;

        while (true) {
            List<Map<String, Object>> pageData = webClient.get()
                    .uri(uri + "?per_page=100&page=" + page, params)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            response -> Mono.error(new RuntimeException("GitHub API error")))
                    .bodyToMono(List.class)
                    .block();

            if (pageData == null || pageData.isEmpty()) break;

            all.addAll(pageData);
            page++;
        }

        return all;
    }

    private Mono<List<Map<String, Object>>> fetchAllPagesAsync(String uri, Object... params) {
        return Mono.fromCallable(() -> fetchAllPages(uri, params));
    }
}
