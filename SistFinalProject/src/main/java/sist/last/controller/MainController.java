package sist.last.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String start()
    {
        return "/layout/main";
    }

    @GetMapping("/test")
    public String test(){
        return "/layout/test";
    }

}


