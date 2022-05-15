package com.nikolaev.horeca.controllers;

import com.nikolaev.horeca.datasets.UserAvatarColorsDataset;
import com.nikolaev.horeca.domains.*;
import com.nikolaev.horeca.misc.Role;
import com.nikolaev.horeca.repositories.OrganizationRepository;
import com.nikolaev.horeca.repositories.UserAvatarsRepository;
import com.nikolaev.horeca.repositories.UserRatingRepository;
import com.nikolaev.horeca.repositories.UserRepository;
import com.nikolaev.horeca.services.AdminService;
import com.nikolaev.horeca.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AdminService adminService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAvatarsRepository userAvatarsRepository;
    @Autowired
    UserRatingRepository userRatingRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    User user = new User();
    boolean isSignedIn = false;

    @GetMapping("/")
    public String getHome(Model model) {
        if (isSignedIn) {
            model.addAttribute("user", user);
        }
        boolean isAdmin = userRepository.existsByName("admin");
        if(!isAdmin){
            adminService.createAdmin();
        }

        List<Organization> organizations = organizationRepository.findAll();

        for (Organization organization  : organizations){
            organization.setStarsMarkup();
        }


        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("organizations", organizations);

        return "index";
    }

    @GetMapping("/company/{companyId}")
    public String getCompanyAbout(@PathVariable(value = "companyId") long companyId,
                                  Model model){
        Organization organization = organizationRepository.getById(companyId);
        organization.setStarsMarkup();
        model.addAttribute("organization", organization);
        return "about";
    }

    @GetMapping("/user/{id}/rateCompany/{companyId}")
    public String rateCompany(@PathVariable(value = "companyId") long companyId,
                              @PathVariable(value = "id") long userId,
                              Model model){
        Organization organization = organizationRepository.getById(companyId);
        organization.setStarsMarkup();
        if (isSignedIn) {
            model.addAttribute("user", user);
        }
        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("organization", organization);
        return "rate";
    }

    @GetMapping("/userpage/{username}")
    public String getUserPage(Model model, @PathVariable (value = "username") String username){
        if (isSignedIn && username.equals(user.getName())) {
            if(username.equals("admin")){
                user = userRepository.getByName("admin");
            }
            UserAvatar userAvatar = userAvatarsRepository.getByUserId(user.getId());
            List<UserRating> userRatingList = userRatingRepository.findAllByUserId(user.getId());
            int ratingListSize = userRatingList.size();
            model.addAttribute("rating", userRatingList);
            model.addAttribute("ratingCount", ratingListSize);
            model.addAttribute("user", user);
            model.addAttribute("isSignedIn", isSignedIn);
            model.addAttribute("username", username);
            model.addAttribute("userAvatar", userAvatar);
        }
        else{
            return "redirect:/signin";
        }
        if(username.equals("admin")){
            return "admin";
        }
        return "userpage";
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
    public String signIn(@RequestParam(value = "username") String username,
                         @RequestParam(value = "password") String password,
                         Model model) {

        System.out.println("Got username: " + username);
        System.out.println("Got password: " + password);
        List<ErrorMessage> errors = authenticationService.validateLogin(username, password);
        if (errors.isEmpty()) {
            user = userRepository.findByName(username);
            isSignedIn = true;
            return "redirect:/";
        } else {
            isSignedIn = false;
            model.addAttribute("username", username);
        }
        return "sign-in";
    }

    @GetMapping("/register")
    public String registerNewUser(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "email") String email,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "repeatPassword") String repeatPassword,
                                  Model model) {

        System.out.println("Got username: " + username);
        System.out.println("Got name: " + name);
        System.out.println("Got email: " + email);
        System.out.println("Got password: " + password);
        System.out.println("Got repeatPassword: " + repeatPassword);
        List<ErrorMessage> errors = authenticationService.validateRegistration(username, name, password, repeatPassword, email);
        if (errors.isEmpty()) {
            user.setName(username);
            user.setFullName(authenticationService.normalize(name));
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.USER);
            userRepository.save(user);
            user = userRepository.findByName(username);
            UserAvatarColorsDataset colorsDataset = new UserAvatarColorsDataset();
            int size = colorsDataset.size;
            int index = (int)Math.floor(Math.random()*(size+1));

            String primaryColor = colorsDataset.userColors.get(index);
            String secondaryColor =  colorsDataset.userSecondaryColors.get(index);
            System.out.println(index + " " + primaryColor + " " + secondaryColor);
            UserAvatar avatar = adminService.createUserAvatar(user, primaryColor, secondaryColor);
            userRepository.save(user);
            userAvatarsRepository.save(avatar);
            isSignedIn = true;
            return "redirect:/";
        } else {
            isSignedIn = false;
            for (ErrorMessage error : errors) {
                model.addAttribute(error.getType().getTemplateValue(), error);
                error.print();
            }
            model.addAttribute("invalid_authentication",  "есть ошибки в регистрации");
            model.addAttribute("username", username);
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("repeatPassword", repeatPassword);
        }
        return "sign-up";
    }

    @GetMapping("/signout")
    public String signOut() {
        isSignedIn = false;
        user = new User();
        return "redirect:/signin";
    }
}