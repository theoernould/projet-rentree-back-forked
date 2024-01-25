package imt.deploy.deployer.services;

import imt.deploy.deployer.models.Pipeline;
import imt.deploy.deployer.models.dto.PipelineDTO;
import imt.deploy.deployer.repositories.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.python.util.PythonInterpreter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PipelineExecutor {
    private final SimpMessagingTemplate messagingTemplate;
    private final PipelineRepository pipelineRepository;
    private final String stepsScriptsFilesPath = "/steps/";

    @Async
    public void executePipeline(String pipelineId) {
        log.info("Executing pipeline: {}", pipelineId);
        sendRefreshedPipelines();
        Pipeline pipeline = pipelineRepository.findById(pipelineId).get();
        executeSteps(pipeline);
    }

    private void executeSteps(Pipeline pipeline) {
        try {
            for (Pipeline.Step step : Pipeline.Step.values()) {
                updateStepStatus(pipeline, step, Pipeline.Status.RUNNING);
                Thread.sleep(5000);
                executeStep(pipeline, step);
            }
        } catch (Exception e) {
            log.error("Pipeline execution failed", e);
        }
    }

    @Async
    public void sendRefreshedPipelines() {
        List<PipelineDTO> pipelines = pipelineRepository.findAllByOrderByNumberDesc().stream().map(Pipeline::toDTO).toList();
        messagingTemplate.convertAndSend("/topic/pipelines", pipelines);
    }

    private void updateStepStatus(Pipeline pipeline, Pipeline.Step step, Pipeline.Status status) {
        pipeline.steps().put(step, status);
        Pipeline savedPipeline = pipelineRepository.save(pipeline);
        messagingTemplate.convertAndSend("/topic/pipelines/" + savedPipeline.id(), savedPipeline.toDTO());
    }

    private void executeStep(Pipeline pipeline, Pipeline.Step step) throws Exception {
        log.info("Executing step: {}", step);
        /*String stepPath = stepsScriptsFilesPath + step.nameFormatted() + "/" + step.nameFormatted() + ".py";
        int exitCode = executeScript(getScriptFile(stepPath));
        log.info("Step {} executed with exit code: {}", step, exitCode);
        Pipeline.Status status = exitCode == 0 ? Pipeline.Status.SUCCESS : Pipeline.Status.FAILURE;*/
        Pipeline.Status status = Math.random() * 100 < 10 ? Pipeline.Status.FAILURE : Pipeline.Status.SUCCESS;
        updateStepStatus(pipeline, step, status);
        if(status == Pipeline.Status.FAILURE) {
            throw new Exception("Step " + step + " failed");
        }
    }

    private File getScriptFile(String scriptName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(scriptName);
        InputStream inputStream = classPathResource.getInputStream();
        File tempFile = File.createTempFile(scriptName, ".py");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            FileCopyUtils.copy(inputStream, out);
        }
        return tempFile;
    }

    private int executeScript(File scriptFile) {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.execfile(scriptFile.getAbsolutePath());
            return 0; // Assuming the script execution is successful
        } catch (Exception e) {
            log.error("Python script execution failed", e);
            return 1; // Indicate failure
        } finally {
            if (!scriptFile.delete()) {
                log.warn("Temporary script file could not be deleted: {}", scriptFile.getAbsolutePath());
            }
        }
    }
}
