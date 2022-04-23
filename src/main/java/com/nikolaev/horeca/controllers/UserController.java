package com.nikolaev.horeca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/signin")
    public String getLoginPage() {
        return "sign-in";
    }

    @GetMapping("/signup")
    public String getRegistrationPage() {
        return "sign-up";
    }

    @PostMapping("/login")
    public String signIn() {
        return "index";
    }

    @GetMapping("/register")
    public String registerNewUser() {
        return "index";
    }
}
