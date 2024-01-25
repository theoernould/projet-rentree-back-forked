package imt.deploy.deployer.services;

import imt.deploy.deployer.models.Commit;
import imt.deploy.deployer.models.Pipeline;
import imt.deploy.deployer.models.Repository;
import imt.deploy.deployer.models.User;
import imt.deploy.deployer.repositories.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PipelineService {
    private final PipelineRepository pipelineRepository;
    private final PipelineExecutor pipelineExecutor;
    private final GitHubService gitHubService;

    @Value("${github.repo.owner}")
    private String githubOwner;

    @Value("${github.repo.name}")
    private String githubRepo;

    @Value("${github.repo.access-token}")
    private String githubAccessToken;

    public List<Pipeline> findAll() {
        return pipelineRepository.findAllByOrderByNumberDesc();
    }

    public Optional<Pipeline> findById(String id) {
        return pipelineRepository.findById(id);
    }

    public Integer getNextNumber() {
        return pipelineRepository.findFirstByOrderByNumberDesc().map(pipeline -> pipeline.number() + 1).orElse(1);
    }

    public Pipeline save(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    public void triggerPipeline(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        Map<String, Object> details = gitHubService.getRepositoryDetailsWithLastCommit(githubOwner, githubRepo, githubAccessToken);
        Commit commit = getCommitFromDetails(details, formatter);
        Repository repository = getRepositoryFromDetails(details);
        Pipeline pipeline = Pipeline.builder()
                .number(getNextNumber())
                .user(user)
                .branchName(details.get("branchName").toString())
                .commit(commit)
                .repository(repository)
                .timestamp(LocalDateTime.now())
                .steps(Pipeline.Step.steps())
                .build();
        Pipeline savedPipeline = pipelineRepository.save(pipeline);
        pipelineExecutor.executePipeline(savedPipeline.id());
    }

    private static Repository getRepositoryFromDetails(Map<String, Object> details) {
        return Repository.builder()
                .name(details.get("repositoryName").toString())
                .url(details.get("repositoryUrl").toString())
                .build();
    }

    private static Commit getCommitFromDetails(Map<String, Object> details, DateTimeFormatter formatter) {
        return Commit.builder()
                .id(details.get("commitId").toString())
                .author(details.get("commitAuthor").toString())
                .message(details.get("commitMessage").toString())
                .timestamp(LocalDateTime.parse(details.get("commitTimestamp").toString(), formatter))
                .url(details.get("commitUrl").toString())
                .build();
    }

}
