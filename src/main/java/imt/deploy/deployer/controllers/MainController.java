package imt.deploy.deployer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/public/hello")
    public String hello() {
        return "Hello World";
    }

}
