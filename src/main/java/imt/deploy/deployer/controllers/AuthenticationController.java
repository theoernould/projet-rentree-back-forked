package imt.deploy.deployer.controllers;

import imt.deploy.deployer.models.dto.UserDTO;
import imt.deploy.deployer.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @GetMapping("/user")
    public UserDTO user(OAuth2AuthenticationToken authentication) {
        return userService.getUserFromAuthenticationToken(authentication).toDTO();
    }

}
