package imt.deploy.deployer.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String login;
    private String name;
    private String avatarUrl;
    private String htmlUrl;
}
