package imt.deploy.deployer.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception exception) {
        log.error("An error occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue");
    }
}
