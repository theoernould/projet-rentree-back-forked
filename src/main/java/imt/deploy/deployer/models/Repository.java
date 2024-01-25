package imt.deploy.deployer.models;

import imt.deploy.deployer.models.dto.RepositoryDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Repository {
    private String name;
    private String url;

    public RepositoryDTO toDTO() {
        return RepositoryDTO.builder().name(name).url(url).build();
    }
}
