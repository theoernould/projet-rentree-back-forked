package imt.deploy.deployer.models.dto;

import imt.deploy.deployer.models.Pipeline;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class PipelineDTO {
    private String id;
    private Integer number;
    private UserDTO user;
    private String branchName;
    private CommitDTO commit;
    private RepositoryDTO repository;
    private LocalDateTime timestamp;
    private Map<Pipeline.Step, Pipeline.Status> steps;
}
