package imt.deploy.deployer.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryDTO {
    private String name;
    private String url;
}
