package imt.deploy.deployer.models;

import imt.deploy.deployer.models.dto.CommitDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Commit {
    private LocalDateTime timestamp;
    private String id;
    private String author;
    private String message;
    private String url;

    public CommitDTO toDTO() {
        return CommitDTO.builder().timestamp(timestamp).id(id).author(author).message(message).url(url).build();
    }
}
