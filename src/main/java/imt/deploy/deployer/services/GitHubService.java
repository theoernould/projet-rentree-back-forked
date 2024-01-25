package imt.deploy.deployer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private final WebClient githubWebClient;

    public Map<String, Object> getRepositoryDetailsWithLastCommit(String owner, String repo, String accessToken) {
        Map<String, Object> repoDetails = githubWebClient.get().uri("/repos/{owner}/{repo}", owner, repo).header("Authorization", "Bearer " + accessToken).retrieve().bodyToMono(Map.class).block();

        Map<String, Object> lastCommitDetails = githubWebClient.get().uri("/repos/{owner}/{repo}/commits", owner, repo).header("Authorization", "Bearer " + accessToken).retrieve().bodyToMono(List.class).flatMap(list -> Mono.justOrEmpty(((List<Map>) list).stream().findFirst())).block();

        Map<String, Object> details = new LinkedHashMap<>();
        if (repoDetails != null) {
            details.put("repositoryName", repoDetails.get("full_name"));
            details.put("repositoryUrl", repoDetails.get("html_url"));
            details.put("branchName", "refs/heads/" + repoDetails.get("default_branch"));
        }
        if (lastCommitDetails != null) {
            details.put("commitId", lastCommitDetails.get("sha"));
            Map commit = (Map) lastCommitDetails.get("commit");
            Map author = (Map) commit.get("author");
            details.put("commitAuthor", author.get("name"));
            details.put("commitMessage", commit.get("message"));
            details.put("commitTimestamp", author.get("date"));
            details.put("commitUrl", lastCommitDetails.get("html_url"));
        }

        return details;
    }
}
