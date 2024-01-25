package imt.deploy.deployer.models;

import imt.deploy.deployer.models.dto.PipelineDTO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Document(collection = "pipelines")
@Data
@Builder
@Accessors(fluent = true)
public class Pipeline {
    @Id
    private String id;
    private Integer number;
    private User user;
    private String branchName;
    private Commit commit;
    private Repository repository;
    private LocalDateTime timestamp;
    private Map<Step, Status> steps;

    public PipelineDTO toDTO() {
        return PipelineDTO.builder().id(id).number(number).user(Optional.of(user).map(User::toDTO).orElseGet(null)).timestamp(timestamp).branchName(branchName).repository(repository.toDTO()).commit(commit.toDTO()).steps(steps).build();
    }

    public enum Step {
        RETRIEVE_CODE, COMPILATION, SONARQUBE_ANALYSIS, CREATE_DOCKER_IMAGE, DEPLOY_DOCKER_IMAGE, RUN_DOCKER_IMAGE, INTRUSION_TESTS;

        public static Map<Pipeline.Step, Pipeline.Status> steps() {
            Map<Pipeline.Step, Pipeline.Status> steps = new LinkedHashMap<>();
            steps.put(Pipeline.Step.RETRIEVE_CODE, Pipeline.Status.WAITING);
            steps.put(Pipeline.Step.COMPILATION, Pipeline.Status.WAITING);
            steps.put(Pipeline.Step.SONARQUBE_ANALYSIS, Pipeline.Status.WAITING);
            steps.put(Pipeline.Step.CREATE_DOCKER_IMAGE, Pipeline.Status.WAITING);
            steps.put(Pipeline.Step.DEPLOY_DOCKER_IMAGE, Pipeline.Status.WAITING);
            steps.put(Pipeline.Step.RUN_DOCKER_IMAGE, Pipeline.Status.WAITING);
            steps.put(Pipeline.Step.INTRUSION_TESTS, Pipeline.Status.WAITING);
            return steps;
        }

        public String nameFormatted() {
            return this.name().toLowerCase().replace('_', '-');
        }
    }

    public enum Status {
        SUCCESS, FAILURE, RUNNING, WAITING
    }
}
