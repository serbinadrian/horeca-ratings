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
        ErrorMessage message = new ErrorMessage("Неверное имя пользователя или пароль", ErrorType.INVALID_AUTH);

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
    public List<ErrorMessage> validateRegistration(String username, String name, String password, String repeatPassword, String email) {

        String fullName = normalize(name);

        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);

        List<ErrorMessage> errorMessages = new ArrayList<>();

        if(!isFullName(fullName) || isEmpty(fullName)){
            ErrorMessage message = new ErrorMessage("Введены некорректные данные", ErrorType.INVALID_NAME);
            errorMessages.add(message);
        }

        if(!isEmail(email) || isEmpty(email)){
            ErrorMessage message = new ErrorMessage("Введен некорректный Email", ErrorType.INVALID_EMAIL);
            errorMessages.add(message);
        }

        if(!isLatinAndNumbers(username) || isUserExists(user) || isEmpty(username)){
            ErrorMessage message = new ErrorMessage("Пользователь уже существует или введено некорректное имя", ErrorType.INVALID_USER);
            errorMessages.add(message);
        }

        if(!isEqualPasswords(password, repeatPassword) || isEmpty(password) || isEmpty(repeatPassword)){
            ErrorMessage message = new ErrorMessage("Пароли не совпадают или некорректны", ErrorType.INVALID_PASSWORD);
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

    private boolean isFullName(String name){
        String[] words = name.split(" ");
        return words.length == 2;
    }

    private boolean isEqualPasswords(String password, String password2) {
        return password.equals(password2);
    }

    private boolean isUserExists(User user) {
        return userRepository.existsByName(user.getName()) || userRepository.existsByEmail(user.getEmail());
    }

    private boolean isEmpty(String data){
        return data.length() == 0;
    }

    public String normalize(String data){
        StringBuilder result = new StringBuilder();
        String[] words = data.split(" ");
        for (String word : words) {
            String target = word.trim();
            if(!target.isEmpty()){
                char letter = target.charAt(0);
                String firstLetter = Character.toString(letter);
                String stringBody = target.substring(1);
                result.append(firstLetter.toUpperCase()).append(stringBody.toLowerCase()).append(" ");
            }
        }
        System.out.println("normalized: [" + result.toString().trim() +"]");
        return result.toString().trim();
    }
}
