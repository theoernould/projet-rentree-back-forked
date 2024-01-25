package imt.deploy.deployer.models.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommitDTO {
    private LocalDateTime timestamp;
    private String id;
    private String author;
    private String message;
    private String url;
}
