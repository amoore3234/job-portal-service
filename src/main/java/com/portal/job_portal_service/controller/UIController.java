package com.portal.job_portal_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portal")
public class UIController {

    @GetMapping("/user/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/user/register")
    public String getSignupPage() {
        return "signup";
    }

    @GetMapping("/user/home")
    public String getHomePage() {
        return "index";
    }
}
