package com.meama.meamacollect.application.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TemplateController {

    @RequestMapping(value = {"/", "/login", "/panel"})
    public String index() {
        return "forward:/index.html";
    }
}
