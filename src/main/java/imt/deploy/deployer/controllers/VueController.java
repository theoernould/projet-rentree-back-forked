package imt.deploy.deployer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VueController {

    @RequestMapping(value = "/{path:[^.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
