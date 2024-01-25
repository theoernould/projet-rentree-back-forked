package imt.deploy.deployer.controllers;

import imt.deploy.deployer.models.Pipeline;
import imt.deploy.deployer.models.User;
import imt.deploy.deployer.models.dto.PipelineDTO;
import imt.deploy.deployer.services.PipelineService;
import imt.deploy.deployer.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PipelineController {
    private final PipelineService pipelineService;
    private final UserService userService;

    @GetMapping("/pipelines")
    public List<PipelineDTO> findAll() {
        List<PipelineDTO> pipelines = pipelineService.findAll().stream().map(Pipeline::toDTO).toList();
        log.info("Pipelines: {}", pipelines);
        return pipelines;
    }

    @PostMapping("/pipelines")
    public ResponseEntity<?> triggerPipeline(OAuth2AuthenticationToken authentication) {
        User user = userService.getUserFromAuthenticationToken(authentication);
        pipelineService.triggerPipeline(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pipelines/{id}")
    public ResponseEntity<PipelineDTO> getPipeline(@PathVariable String id) {
        Optional<Pipeline> pipeline = pipelineService.findById(id);
        return pipeline.map(value -> ResponseEntity.ok(value.toDTO())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
