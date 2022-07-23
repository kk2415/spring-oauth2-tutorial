package com.oauth2.custom.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/oauth/redirect")
    public String redirect() {

        return "redirect";
    }

}
