package com.nikolaev.horeca.controllers;

import com.nikolaev.horeca.domains.ErrorMessage;
import com.nikolaev.horeca.domains.User;
import com.nikolaev.horeca.domains.UserAvatar;
import com.nikolaev.horeca.domains.UserRating;
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

    /*
    @PostMapping("/admin/addOrganization")
    public String addFakeComposition(@RequestParam(value = "compositionName")String compositionName,
                                     @RequestParam(value = "compositionAuthor")String compositionAuthor,
                                     Model model){
                                     //todo  user
        user = userRepository.getByName("admin");
        userAvatar = userAvatarsRepository.getByUserId(user.getId());
        List<ErrorMessage> errorMessages = adminService.ValidateComposition(compositionName, compositionAuthor);

        if(errorMessages.isEmpty()){
            Composition composition = new Composition(compositionName, compositionAuthor, user);

            compositionRepository.save(composition);
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
        //List<Composition> compositions = compositionRepository.findAllByOwnerId(user.getId());
        //int uploadedListSize = compositions.size();
        //model.addAttribute("ownMusic", compositions);
        //model.addAttribute("uploadedMusicCount", uploadedListSize);
        model.addAttribute("userAvatar", userAvatar);
        return "admin";
    }
*/

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

