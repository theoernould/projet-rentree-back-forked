package imt.deploy.deployer.services;

import imt.deploy.deployer.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public User getUserFromAuthenticationToken(OAuth2AuthenticationToken authentication) {
        return User.builder()
                .id(authentication.getPrincipal().getAttribute("id"))
                .login(authentication.getPrincipal().getAttribute("login"))
                .name(authentication.getPrincipal().getAttribute("name"))
                .avatarUrl(authentication.getPrincipal().getAttribute("avatar_url"))
                .htmlUrl(authentication.getPrincipal().getAttribute("html_url"))
                .build();
    }
}
