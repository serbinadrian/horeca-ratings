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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
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

    //todo  user !WARNING! unauthorized admin actions
    User user;
    UserAvatar userAvatar;

    @PostMapping("/admin/wipedata")
    public String wipeData(@RequestParam(value = "adminPassword")String adminPassword, Model model){
        //todo  user
        user = userRepository.getByName("admin");
        userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<ErrorMessage> errorList = authenticationService.validateLogin(user.getName(), adminPassword);
        if(!errorList.isEmpty()){
            for (ErrorMessage errorMessage: errorList) {
                model.addAttribute(errorMessage.getType().getTemplateValue(), errorMessage.getMessage());
            }
        }
        adminService.wipeData();
        //todo  user
        user = userRepository.getByName("admin");

        //todo fix
        model.addAttribute("user", user);
        model.addAttribute("isSignedIn", true);
        model.addAttribute("userAvatar", userAvatar);
        UserAvatar userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<UserRating> rating = userRatingRepository.findAllByUserId(user.getId());
        int ratingListSize = rating.size();
        model.addAttribute("rating", rating);
        model.addAttribute("ratingListSize", ratingListSize);
        model.addAttribute("userAvatar", userAvatar);

        return "admin";
    }


    @PostMapping("/admin/addOrganization")
    public String addFakeComposition(@RequestParam(value = "organizationName")String name,
                                     @RequestParam(value = "organizationEmail")String email,
                                     @RequestParam(value = "organizationSpecs")String specs,
                                     @RequestParam(value = "organizationLocation")String location,
                                     @RequestParam(value = "organizationWebsite")String website,
                                     @RequestParam(value = "organizationVkLink")String vkLink,
                                     @RequestParam(value = "organizationFbLink")String fbLink,
                                     @RequestParam(value = "organizationInstLink")String instLink,
                                     Model model){
                                     //todo  user
        user = userRepository.getByName("admin");
        userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<ErrorMessage> errorMessages = adminService.ValidateOrganization(name);

        if(errorMessages.isEmpty()){
            Organization organization = new Organization();
            organization.setEmail(email);
            organization.setSpecs(specs);
            organization.setLocation(location);
            organization.setWebsite(website);
            organization.setVkLink(vkLink);
            organization.setFbLink(fbLink);
            organization.setInstLink(instLink);
            organization.setName(name);
            organizationRepository.save(organization);
        }
        else{
            for (ErrorMessage error : errorMessages) {
                model.addAttribute(error.getType().getTemplateValue(), error);
                error.print();
            }
        }
        //todo fix
        model.addAttribute("user", user);
        model.addAttribute("isSignedIn", true);
        model.addAttribute("userAvatar", userAvatar);
        UserAvatar userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<UserRating> rating = userRatingRepository.findAllByUserId(user.getId());
        int ratingListSize = rating.size();
        model.addAttribute("rating", rating);
        model.addAttribute("ratingListSize", ratingListSize);
        model.addAttribute("userAvatar", userAvatar);
        return "admin";
    }

    @PostMapping("/admin/addUSer")
    public String addUser(@RequestParam(value = "userName")String name,
                          @RequestParam(value = "userEmail")String email,
                          @RequestParam(value = "userPassword")String password,
                          @RequestParam(value = "userFullName")String fullname,
                          Model model) {
        user = userRepository.getByName("admin");
        userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<ErrorMessage> errorMessages = authenticationService.validateRegistration(name, fullname, password, password, email);
        if (errorMessages.isEmpty()) {
            User userToSave = new User();
            userToSave.setPassword(password);
            userToSave.setRole(Role.USER);
            userToSave.setFullName(fullname);
            userToSave.setName(name);
            userToSave.setEmail(email);
            userRepository.save(userToSave);
            userToSave = userRepository.getByName(name);
            UserAvatarColorsDataset userAvatarColorsDataset = new UserAvatarColorsDataset();
            UserAvatar userAvatar1 = adminService.createUserAvatar(userToSave,
                    userAvatarColorsDataset.userColors.get(adminService.randomInRage(0, 20)),
                    userAvatarColorsDataset.userSecondaryColors.get(adminService.randomInRage(0, 20)));
            userAvatarsRepository.save(userAvatar1);
        }

        model.addAttribute("user", user);
        model.addAttribute("isSignedIn", true);
        model.addAttribute("userAvatar", userAvatar);
        UserAvatar userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<UserRating> rating = userRatingRepository.findAllByUserId(user.getId());
        int ratingListSize = rating.size();
        model.addAttribute("rating", rating);
        model.addAttribute("ratingListSize", ratingListSize);
        model.addAttribute("userAvatar", userAvatar);
        return "admin";
    }

    @PostMapping("/admin/fillDB")
    public String fillDatabase(Model model){
        //todo  user
        user = userRepository.getByName("admin");
        userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<ErrorMessage> errorMessages = adminService.createFakeDatabase();

        if (!errorMessages.isEmpty()) {
            for (ErrorMessage error : errorMessages) {
                model.addAttribute(error.getType().getTemplateValue(), error);
                error.print();
            }
        }

        //todo fix
        model.addAttribute("user", user);
        model.addAttribute("isSignedIn", true);
        model.addAttribute("userAvatar", userAvatar);
        UserAvatar userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<UserRating> rating = userRatingRepository.findAllByUserId(user.getId());
        int ratingListSize = rating.size();
        model.addAttribute("rating", rating);
        model.addAttribute("ratingListSize", ratingListSize);
        model.addAttribute("userAvatar", userAvatar);
        return "admin";
    }
}

