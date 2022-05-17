package com.nikolaev.horeca.controllers;

import com.nikolaev.horeca.DTOs.UserCommentDTO;
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

import java.util.ArrayList;
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

        List<UserCommentDTO> commentDtos = new ArrayList<>();

        List<UserRating> ratings = userRatingRepository.findAllByOrganizationId(organization.getId());

        for (UserRating rating:ratings) {
            UserCommentDTO userCommentDTO = new UserCommentDTO(userRepository.getById(rating.getUserId()),userAvatarsRepository.getByUserId(rating.getUserId()), rating);
            commentDtos.add(userCommentDTO);
        }
        model.addAttribute("comments", commentDtos);
        model.addAttribute("organization", organization);
        return "about";
    }

    @GetMapping("/user/{id}/rateCompany/{companyId}")
    public String rateCompany(@PathVariable(value = "companyId") long companyId,
                              @PathVariable(value = "id") long userId,
                              Model model){
        UserRating userRating = new UserRating();
        Organization organization = organizationRepository.getById(companyId);
        organization.setStarsMarkup();
        if (isSignedIn) {
            model.addAttribute("user", user);
        }
        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("organization", organization);
        model.addAttribute("rating", userRating);
        return "rate";
    }

    @PostMapping("/rate/organization/")
    public String saveRating(@RequestParam(value = "userId")Long userId,
                             @RequestParam(value = "organizationId")Long organizationId,
                             @RequestParam(value = "personnel")Integer personnelRate,
                             @RequestParam(value = "mood")Integer moodRate,
                             @RequestParam(value = "taste")Integer tasteRate,
                             @RequestParam(value = "serviceToPrice")Integer serviceToPriceRate,
                             @RequestParam(value = "location")Integer locationRate,
                             @RequestParam(value = "foodToPrice")Integer foodToPriceRate,
                             @RequestParam(value = "clean")Integer cleanRate,
                             @RequestParam(value = "comment")String comment,
                             Model model){
        User user1 = userRepository.getById(userId);

        Organization organization = organizationRepository.getById(organizationId);
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setOrganizationId(organizationId);
        userRating.setPersonnelRate(personnelRate);
        userRating.setMoodRate(moodRate);
        userRating.setTasteRate(tasteRate);
        userRating.setServiceToPriceRate(serviceToPriceRate);
        userRating.setLocationRate(locationRate);
        userRating.setFoodToPriceRate(foodToPriceRate);
        userRating.setCleanRate(cleanRate);
        userRating.setComment(comment);

        userRatingRepository.save(userRating);

        adminService.calculateRatingOfOrganization(organization.getName());
        adminService.calculateStarRatingOfOrganization(organization.getName());

        organization = organizationRepository.getById(organizationId);
        //todo toService
        organization.setStarsMarkup();

        List<UserCommentDTO> commentDtos = new ArrayList<>();

        List<UserRating> ratings = userRatingRepository.findAllByOrganizationId(organization.getId());

        for (UserRating rating:ratings) {
            UserCommentDTO userCommentDTO = new UserCommentDTO(userRepository.getById(rating.getUserId()),userAvatarsRepository.getByUserId(rating.getUserId()), rating);
            commentDtos.add(userCommentDTO);
        }
        model.addAttribute("comments", commentDtos);
        model.addAttribute("organization", organization);
        //toService
        return "about";
    }

    @GetMapping("/deleteRate/{id}/{orgId}")
    public String deleteUserComment(@PathVariable(value = "id")long id,
                                    @PathVariable(value = "orgId")long orgId){
        userRatingRepository.deleteById(id);
        Organization organization = organizationRepository.getById(orgId);
        adminService.calculateRatingOfOrganization(organization.getName());
        adminService.calculateStarRatingOfOrganization(organization.getName());
        return "redirect:" + "/userpage/"+ user.getName();
    }

    @GetMapping("/userpage/{username}")
    public String getUserPage(Model model, @PathVariable (value = "username") String username){
        if (isSignedIn && username.equals(user.getName())) {
            if(username.equals("admin")){
                user = userRepository.getByName("admin");
            }
            List<UserRating> userRatingList = userRatingRepository.findAllByUserId(user.getId());
            List<UserCommentDTO> userComments = new ArrayList<>();
            for (UserRating rating : userRatingList){
                Organization organization = organizationRepository.getById(rating.getOrganizationId());
                UserCommentDTO userCommentDTO = new UserCommentDTO(organization, rating);
                userComments.add(userCommentDTO);
            }
            UserAvatar userAvatar = userAvatarsRepository.getByUserId(user.getId());
            model.addAttribute("userAvatar", userAvatar);
            model.addAttribute("comments", userComments);
            model.addAttribute("user", user);
            model.addAttribute("isSignedIn", isSignedIn);
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
            for (ErrorMessage errorMessage: errors) {
                model.addAttribute(errorMessage.getType().getTemplateValue(), errorMessage.getMessage());
            }
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