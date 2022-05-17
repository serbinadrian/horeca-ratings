package com.nikolaev.horeca.DTOs;

import com.nikolaev.horeca.domains.Organization;
import com.nikolaev.horeca.domains.User;
import com.nikolaev.horeca.domains.UserAvatar;
import com.nikolaev.horeca.domains.UserRating;

public class UserCommentDTO {
    long id;
    long orgId;
    String initials;
    String appliedUserColor;
    String appliedSecondaryUserColor;
    String fullName;

    String companyName;
    int personnelRate = 0;
    int tasteRate = 0;
    int locationRate = 0;
    int cleanRate = 0;
    int moodRate = 0;
    int serviceToPriceRate = 0;
    int foodToPriceRate = 0;

    String comment = "";


    public UserCommentDTO(Organization organization, UserRating userRating){
        this.id = userRating.getId();
        this.orgId = userRating.getOrganizationId();
        this.companyName = organization.getName();
        this.personnelRate = userRating.getPersonnelRate();
        this.tasteRate = userRating.getTasteRate();
        this.locationRate = userRating.getLocationRate();
        this.cleanRate = userRating.getCleanRate();
        this.moodRate = userRating.getMoodRate();
        this.serviceToPriceRate = userRating.getServiceToPriceRate();
        this.foodToPriceRate = userRating.getFoodToPriceRate();
        this.comment = userRating.getComment();
    }
    public UserCommentDTO(User user, UserAvatar userAvatar, UserRating userRating) {
        this.id = userRating.getId();
        this.orgId = userRating.getOrganizationId();
        this.initials = userAvatar.getInitials();
        this.appliedUserColor = userAvatar.getAppliedUserColor();
        this.appliedSecondaryUserColor = userAvatar.getAppliedSecondaryUserColor();
        this.fullName = user.getFullName();
        this.personnelRate = userRating.getPersonnelRate();
        this.tasteRate = userRating.getTasteRate();
        this.locationRate = userRating.getLocationRate();
        this.cleanRate = userRating.getCleanRate();
        this.moodRate = userRating.getMoodRate();
        this.serviceToPriceRate = userRating.getServiceToPriceRate();
        this.foodToPriceRate = userRating.getFoodToPriceRate();
        this.comment = userRating.getComment();
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getPersonnelRate() {
        return personnelRate;
    }

    public void setPersonnelRate(int personnelRate) {
        this.personnelRate = personnelRate;
    }

    public int getTasteRate() {
        return tasteRate;
    }

    public void setTasteRate(int tasteRate) {
        this.tasteRate = tasteRate;
    }

    public int getLocationRate() {
        return locationRate;
    }

    public void setLocationRate(int locationRate) {
        this.locationRate = locationRate;
    }

    public int getCleanRate() {
        return cleanRate;
    }

    public void setCleanRate(int cleanRate) {
        this.cleanRate = cleanRate;
    }

    public int getMoodRate() {
        return moodRate;
    }

    public void setMoodRate(int moodRate) {
        this.moodRate = moodRate;
    }

    public int getServiceToPriceRate() {
        return serviceToPriceRate;
    }

    public void setServiceToPriceRate(int serviceToPriceRate) {
        this.serviceToPriceRate = serviceToPriceRate;
    }

    public int getFoodToPriceRate() {
        return foodToPriceRate;
    }

    public void setFoodToPriceRate(int foodToPriceRate) {
        this.foodToPriceRate = foodToPriceRate;
    }
}
