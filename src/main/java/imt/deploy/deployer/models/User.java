package imt.deploy.deployer.models;

import imt.deploy.deployer.models.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Integer id;
    private String login;
    private String name;
    private String avatarUrl;
    private String htmlUrl;

    public UserDTO toDTO() {
        return UserDTO.builder()
                .id(id)
                .login(login)
                .name(name)
                .avatarUrl(avatarUrl)
                .htmlUrl(htmlUrl)
                .build();
    }
}
