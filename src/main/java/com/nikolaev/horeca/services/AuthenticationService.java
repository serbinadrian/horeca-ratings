package com.nikolaev.horeca.services;

import com.nikolaev.horeca.domains.ErrorMessage;
import com.nikolaev.horeca.domains.User;
import com.nikolaev.horeca.misc.ErrorType;
import com.nikolaev.horeca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public List<ErrorMessage> validateLogin(String username, String password) {
        User user = new User();
        User targetUser;
        boolean emailOnly = false;
        List<ErrorMessage> errorMessages = new ArrayList<>();
        ErrorMessage message = new ErrorMessage("Неверное имя пользователя или пароль", ErrorType.AUTH);

        if (isEmail(username)) {
            user.setEmail(username);
            emailOnly = true;
        } else {
            user.setName(username);
        }

        user.setPassword(password);

        if (!isUserExists(user)) {
            errorMessages.add(message);
            return errorMessages;
        } else if (emailOnly) {
            targetUser = userRepository.findByEmail(user.getEmail());
        } else {
            targetUser = userRepository.findByName(user.getName());
        }

        if (!isEqualPasswords(targetUser.getPassword(), user.getPassword())) {
            errorMessages.add(message);
            return errorMessages;
        }

        return errorMessages;
    }
    public List<ErrorMessage> validateRegistration(String username, String password, String repeatPassword, String email) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);

        List<ErrorMessage> errorMessages = new ArrayList<>();

        if(!isEmail(email)){
            ErrorMessage message = new ErrorMessage("Введен не Email", ErrorType.INVALID_EMAIL);
            errorMessages.add(message);
        }

        if(!isLatinAndNumbers(username) || isUserExists(user)){
            ErrorMessage message = new ErrorMessage("Пользователь уже существует или введено некорректное имя", ErrorType.INVALID_EMAIL);
            errorMessages.add(message);
        }

        if(!isEqualPasswords(password, repeatPassword)){
            ErrorMessage message = new ErrorMessage("Пароли не совпадают", ErrorType.INVALID_EMAIL);
            errorMessages.add(message);
        }

        return errorMessages;
    }

    private boolean isLatinAndNumbers(String data) {
        String regex = "\\w+";
        return data.matches(regex);
    }

    private boolean isEmail(String email) {
        String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        return email.matches(regex);
    }

    private boolean isEqualPasswords(String password, String password2) {
        return password.equals(password2);
    }

    private boolean isUserExists(User user) {
        return userRepository.existsByName(user.getName()) || userRepository.existsByEmail(user.getEmail());
    }

    private boolean isUsernameExists(String username) {
        return userRepository.existsByName(username);
    }

    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isPasswordMatch(User user) {
        User targetUser = userRepository.findByName(user.getName());
        String password = targetUser.getPassword();
        return password.equals(user.getPassword());
    }
}
