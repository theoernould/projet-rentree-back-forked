package imt.deploy.deployer.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import imt.deploy.deployer.models.Pipeline;
import imt.deploy.deployer.models.WebhookData;
import imt.deploy.deployer.services.PipelineExecutor;
import imt.deploy.deployer.services.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WebhookController {
    private final PipelineService pipelineService;
    private final PipelineExecutor pipelineExecutor;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody JsonNode payload) {
        log.info("Received GitHub Webhook Payload: ");
        log.info(payload.toString());
        log.info("  Repository: {}", payload.get("repository").get("full_name").textValue());
        log.info("  Pusher: {}", payload.get("pusher").get("name").textValue());
        log.info("  Ref: {}", payload.get("ref").textValue());
        log.info("  Before Commit ID: {}", payload.get("before").textValue());
        log.info("  After Commit ID: {}", payload.get("after").textValue());
        log.info("  Created: {}", payload.get("created").booleanValue());
        log.info("  Deleted: {}", payload.get("deleted").booleanValue());
        log.info("  Forced: {}", payload.get("forced").booleanValue());

        if (Objects.nonNull(payload.get("head_commit"))) {
            JsonNode headCommit = payload.get("head_commit");
            log.info("  Head Commit:");
            log.info("    Message: {}", headCommit.get("message").textValue());
            log.info("    Timestamp: {}", headCommit.get("timestamp").textValue());
            log.info("    URL: {}", headCommit.get("url").textValue());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime timestamp = LocalDateTime.parse(payload.get("head_commit").get("timestamp").textValue(), formatter);

        WebhookData webhookData = WebhookData.builder()
                .commitId(payload.get("after").textValue())
                .branchName(payload.get("ref").textValue())
                .repositoryName(payload.get("repository").get("full_name").textValue())
                .repositoryUrl(payload.get("repository").get("html_url").textValue())
                .commitAuthor(payload.get("pusher").get("name").textValue())
                .commitMessage(payload.get("head_commit").get("message").textValue())
                .commitTimestamp(timestamp)
                .commitUrl(payload.get("head_commit").get("url").textValue())
                .build();

        Pipeline savedPipeline = pipelineService.save(webhookData.toPipeline(pipelineService.getNextNumber()));

        pipelineExecutor.executePipeline(savedPipeline.id());

        return ResponseEntity.ok().build();
    }

}