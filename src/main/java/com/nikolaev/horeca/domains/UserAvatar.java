package com.nikolaev.horeca.domains;

import com.sun.istack.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@Entity
@Table(name = "userAvatars")
@EnableAutoConfiguration
public class UserAvatar {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    long id;
    long userId;
    String initials;
    String appliedUserColor;
    String  appliedSecondaryUserColor;

    public UserAvatar(){}

    public void generateInitials(String name){
        String[] fullName = name.split(" ");
        this.initials = getFirstLetterAsString(fullName[0]) + getFirstLetterAsString(fullName[1]);
    }

    private String getFirstLetterAsString(@NotNull String data){
        String firstLetter = "";
        if(data.length() != 0){
            char letter = data.charAt(0);
            firstLetter = Character.toString(letter);
            firstLetter = firstLetter.toUpperCase();
        }
        return firstLetter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getAppliedUserColor() {
        return appliedUserColor;
    }

    public void setAppliedUserColor(String appliedUserColor) {
        this.appliedUserColor = appliedUserColor;
    }

    public String getAppliedSecondaryUserColor() {
        return appliedSecondaryUserColor;
    }

    public void setAppliedSecondaryUserColor(String appliedSecondaryUserColor) {
        this.appliedSecondaryUserColor = appliedSecondaryUserColor;
    }
}