package imt.deploy.deployer.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "webhookData")
@Data
@Builder
@Accessors(fluent = true)
public class WebhookData {
    @Id
    private String id;
    private String commitId;
    private String branchName;
    private String repositoryName;
    private String repositoryUrl;
    private String commitAuthor;
    private String commitMessage;
    private LocalDateTime commitTimestamp;
    private String commitUrl;

    public Pipeline toPipeline(Integer number) {
        Commit commit = Commit.builder()
                .timestamp(commitTimestamp)
                .author(commitAuthor)
                .message(commitMessage)
                .url(commitUrl)
                .build();
        Repository repository = Repository.builder()
                .name(repositoryName)
                .url(repositoryUrl)
                .build();
        return Pipeline.builder()
                .number(number)
                .user(null)
                .branchName(branchName)
                .commit(commit)
                .repository(repository)
                .timestamp(LocalDateTime.now())
                .steps(Pipeline.Step.steps())
                .build();
    }
}
