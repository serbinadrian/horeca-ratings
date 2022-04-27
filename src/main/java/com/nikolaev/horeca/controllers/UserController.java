package com.nikolaev.horeca.controllers;

import com.nikolaev.horeca.domains.ErrorMessage;
import com.nikolaev.horeca.domains.User;
import com.nikolaev.horeca.misc.Role;
import com.nikolaev.horeca.repositories.UserRepository;
import com.nikolaev.horeca.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    User user = new User();
    boolean isSignedIn = false;
    @GetMapping("/")
    public String getHome(Model model) {
        if(isSignedIn){
            model.addAttribute("user", user);
        }

        model.addAttribute("isSignedIn", isSignedIn);

        return "index";
    }
    @GetMapping("/signin")
    public String getLoginPage() {
        return "sign-in";
    }

    @GetMapping("/signup")
    public String getRegistrationPage() {
        return "sign-up";
    }

    @PostMapping("/login")
    public String signIn(@RequestParam(value = "username")String username,
                         @RequestParam(value = "password")String password,
                         Model model) {

        System.out.println("Got username: " + username);
        System.out.println("Got password: " + password);
        List<ErrorMessage> errors =  authenticationService.validateLogin(username, password);
        if(errors.isEmpty()){
            user = userRepository.findByName(username);
            isSignedIn = true;
            return "redirect:/";
        }
        else{
            isSignedIn = false;
            for (ErrorMessage error: errors) {
                model.addAttribute(error.getType().getTemplateValue(), error);
                error.print();
            }
        }
        return "sign-in";
    }

    @GetMapping("/register")
    public String registerNewUser(@RequestParam(value = "username")String username,
                                  @RequestParam(value = "name")String name,
                                  @RequestParam(value = "email")String email,
                                  @RequestParam(value = "password")String password,
                                  @RequestParam(value = "password")String repeatPassword,
                                  Model model) {

        System.out.println("Got username: " + username);
        System.out.println("Got name: " + name);
        System.out.println("Got email: " + email);
        System.out.println("Got password: " + password);
        System.out.println("Got repeatPassword: " + repeatPassword);
        List<ErrorMessage> errors =  authenticationService.validateRegistration(username, password, repeatPassword, email);
        if(errors.isEmpty()){
            user.setName(username);
            user.setFullName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.USER);
            userRepository.save(user);
            isSignedIn = true;
            return "redirect:/";
        }
        else{
            isSignedIn = false;
            for (ErrorMessage error: errors) {
                model.addAttribute(error.getType().getTemplateValue(), error);
                error.print();
            }
        }
        return "sign-up";
    }

    @GetMapping("/signout")
    public String signOut(){
        isSignedIn = false;
        user = new User();
        return "redirect:/signin";
    }
}
