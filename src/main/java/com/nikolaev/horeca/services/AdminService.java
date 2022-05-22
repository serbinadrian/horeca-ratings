package com.nikolaev.horeca.services;

import com.nikolaev.horeca.datasets.CafesDataset;
import com.nikolaev.horeca.datasets.UserAvatarColorsDataset;
import com.nikolaev.horeca.datasets.UserNamesDataset;
import com.nikolaev.horeca.datasets.UserRatingDataset;
import com.nikolaev.horeca.domains.*;
import com.nikolaev.horeca.misc.ErrorType;
import com.nikolaev.horeca.misc.Role;
import com.nikolaev.horeca.repositories.OrganizationRepository;
import com.nikolaev.horeca.repositories.UserAvatarsRepository;
import com.nikolaev.horeca.repositories.UserRatingRepository;
import com.nikolaev.horeca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    UserRatingRepository userRatingRepository;
    @Autowired
    UserAvatarsRepository userAvatarsRepository;

    public void wipeData() {
        userRepository.deleteAll();
        userAvatarsRepository.deleteAll();
        organizationRepository.deleteAll();
        userRatingRepository.deleteAll();

        createAdmin();
    }

    public List<ErrorMessage> ValidateOrganization(String name) {
        List<ErrorMessage> errorMessages = new ArrayList<>();

        if (organizationRepository.existsByName(name)) {
            ErrorMessage errorMessage = new ErrorMessage("Ошибка! такая организация существует", ErrorType.ALREADY_EXISTS);
            errorMessages.add(errorMessage);
        }

        return errorMessages;
    }

    public List<ErrorMessage> createFakeDatabase() {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        boolean hasErrors = saveUsersWithAvatars();
        if (hasErrors) {
            ErrorMessage errorMessage = new ErrorMessage("Ошибка! несоответсвие размеров датасетов users", ErrorType.INCOMPATIBLE_DATASETS_SIZES);
            errorMessages.add(errorMessage);
            return errorMessages;
        }

        hasErrors = saveOrganizations();
        if (hasErrors) {
            ErrorMessage errorMessage = new ErrorMessage("Ошибка несоответсвие размеров датасетов cafes", ErrorType.INCOMPATIBLE_CAFES_SIZES);
            errorMessages.add(errorMessage);
            return errorMessages;
        }

        hasErrors = saveRating();
        if (hasErrors) {
            ErrorMessage errorMessage = new ErrorMessage("Ошибка несоответсвие размеров датасетов rating", ErrorType.INCOMPATIBLE_RATING_SIZES);
            errorMessages.add(errorMessage);
            return errorMessages;
        }

        return errorMessages;
    }

    private boolean saveUsersWithAvatars() {
        int size = 0;
        UserNamesDataset userNamesDataset = new UserNamesDataset();
        UserAvatarColorsDataset userAvatarColorsDataset = new UserAvatarColorsDataset();

        int userListSize = userNamesDataset.size;
        int userAvatarsSize = userAvatarColorsDataset.size;

        if (userListSize != userAvatarsSize) {
            return true;
        }

        List<String> usernames = new ArrayList<>(userNamesDataset.usernames.keySet());

        for (int i = 0; i < userListSize; i++) {
            String key = usernames.get(i);
            User user = createUser(key, userNamesDataset.usernames.get(key));

            if (!userRepository.existsByName(user.getName())) {
                userRepository.save(user);
                User savedUser = userRepository.getByName(key);
                UserAvatar userAvatar = createUserAvatar(savedUser,
                        userAvatarColorsDataset.userColors.get(i),
                        userAvatarColorsDataset.userSecondaryColors.get(i));
                userAvatarsRepository.save(userAvatar);
            }
            size++;
        }
        System.out.println("Users created: " + size);
        return false;
    }

    private boolean saveOrganizations() {

        CafesDataset cafesDataset = new CafesDataset();
        int cafesListSize = cafesDataset.size;
        int progressIndicator = 0;

        for (int i = 0; i < cafesListSize; i++) {
            Organization organization = cafesDataset.organizations.get(i);
            List<ErrorMessage> errorMessages = ValidateOrganization(organization.getName());
            if (errorMessages.isEmpty()) {
                organizationRepository.save(organization);
            }
            progressIndicator = i;
        }
        return false;
    }

    private boolean saveRating() {

        List<String> names = extractCafesNames();

        for (String name : names) {
            createTestRating(name);
            calculateRatingOfOrganization(name);
            calculateStarRatingOfOrganization(name);
        }

        return false;
    }
    //TodO
    public void calculateRatingOfOrganization(String name){
        Organization organization = organizationRepository.getByName(name);
        List<UserRating> ratings = userRatingRepository.findAllByOrganizationId(organization.getId());
        int ratingListSize = ratings.size();
        double sumRating = 0;
        for (UserRating rating : ratings){
            sumRating += rating.getProcessedRating();
        }

        double ratingOfOrganization = round(sumRating / ratingListSize, 2);
        organization.setRating(ratingOfOrganization);
        organization.setUserRatesCount(ratingListSize);
        organizationRepository.save(organization);
    }

    //todo to service
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public void calculateStarRatingOfOrganization(String name){
        Organization organization = organizationRepository.getByName(name);
        if (organization.getRating() == 0 && organization.getUserRatesCount() ==  0){
            List<UserRating> ratings = userRatingRepository.findAllByOrganizationId(organization.getId());
            if(!ratings.isEmpty()){
                calculateRatingOfOrganization(organization.getName());
                organization = organizationRepository.getByName(name);
            }
        }
        double rating = organization.getRating();
        double starRating = roundToStarRating(rating);
        organization.setStarRating(starRating);
        organizationRepository.save(organization);
    }
    public List<String> extractCafesNames(){
        List<String> cafesNames = new ArrayList<>();
        CafesDataset cafesDataset = new CafesDataset();
        int cafesListSize = cafesDataset.size;
        for (int i = 0; i < cafesListSize; i++) {
            cafesNames.add(cafesDataset.organizations.get(i).getName());
        }
        return cafesNames;
    }

    public double roundToStarRating(double value){
        double floor = Math.floor(value);
        double ceil = Math.ceil(value);
        double base = value - floor;
        double remainder = base / 0.25;

        if(remainder >= 1 && remainder < 3){
            value = floor + 0.5;
        }
        if(remainder >= 3){
            value = ceil;
        }
        return value;
    }

    private List<String> extractUSerNames(){
        UserNamesDataset userNamesDataset = new UserNamesDataset();
        return new ArrayList<>(userNamesDataset.usernames.keySet());
    }
    public void createTestRating(String cafeName) {
        UserRatingDataset userRatingDataset = new UserRatingDataset();
        Organization organization = organizationRepository.getByName(cafeName);
        List<String> usernames = extractUSerNames();

        List<Integer> usedRatingIndexList = new ArrayList<>();
        List<Integer> usedUserIndexList = new ArrayList<>();

        int ratingsCount = userRatingDataset.size;
        int usersCount = usernames.size();
        int minUsersCount = 3;

        int usersToProcess = randomInRage(minUsersCount, usersCount);
        System.out.println("usersToProcess: " + usersToProcess);

        for (int i = 0; i < usersToProcess; i++) {
            int userIndex = randomInRage(0, usersCount);
            if(usedUserIndexList.contains(userIndex)){
                while (usedUserIndexList.contains(userIndex)) {
                    userIndex = randomInRage(0, usersCount);
                }
            }
            usedUserIndexList.add(userIndex);
            int ratingIndex = randomInRage(0, ratingsCount);
            if(usedRatingIndexList.contains(ratingIndex)){
                while (usedRatingIndexList.contains(ratingIndex)) {
                    ratingIndex = randomInRage(0, ratingsCount);
                }
            }
            usedRatingIndexList.add(ratingIndex);
            UserRating userRating = processUserRating(usernames.get(userIndex), userRatingDataset.userRatingList.get(ratingIndex), organization.getId());
            userRatingRepository.save(userRating);
        }
    }

    private UserRating processUserRating(String username, UserRating userRating, long organizationId){
        User user = userRepository.findByName(username);
        UserRating rating = new UserRating();
        rating.setUserId(user.getId());
        rating.setOrganizationId(organizationId);
        rating.setPersonnelRate(userRating.getPersonnelRate());
        rating.setTasteRate(userRating.getTasteRate());
        rating.setDate(new Date());
        rating.setLocationRate(userRating.getLocationRate());
        rating.setCleanRate(userRating.getCleanRate());
        rating.setMoodRate(userRating.getMoodRate());
        rating.setServiceToPriceRate(userRating.getServiceToPriceRate());
        rating.setFoodToPriceRate(userRating.getFoodToPriceRate());
        rating.setComment(userRating.getComment());
        return rating;
    }

    public int randomInRage(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    private User createUser(String name, String fullName) {
        User user = new User();
        user.setName(name);
        user.setFullName(fullName);
        user.setEmail(name + "@mail.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        return user;
    }

    public UserAvatar createUserAvatar(User user, String primaryColor, String secondaryColor) {
        UserAvatar userAvatar = new UserAvatar();
        userAvatar.setUserId(user.getId());
        userAvatar.setAppliedUserColor(primaryColor);
        userAvatar.setAppliedSecondaryUserColor(secondaryColor);
        if (user.getRole().equals(Role.ADMIN)) {
            userAvatar.setInitials("admin");
        } else {
            userAvatar.generateInitials(user.getFullName());
        }

        return userAvatar;
    }

    public void createAdmin() {
        UserAvatarColorsDataset colors = new UserAvatarColorsDataset();
        User admin = new User();
        admin.setName("admin");
        admin.setFullName("Admin");
        admin.setEmail("admin@horeca.localhost");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);

        if (!userRepository.existsByName(admin.getName())) {
            userRepository.save(admin);
            admin = userRepository.getByName("admin");
            UserAvatar adminAvatar = createUserAvatar(admin, colors.userColors.get(-1), colors.userSecondaryColors.get(-1));
            userAvatarsRepository.save(adminAvatar);
        }
    }
}
