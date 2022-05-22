package com.nikolaev.horeca.domains;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "UserRating")
@EnableAutoConfiguration
public class UserRating {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private long userId;
    private long organizationId;
    private int personnelRate = 0;
    private int tasteRate = 0;
    private int locationRate = 0;
    private int cleanRate = 0;
    private int moodRate = 0;
    private int serviceToPriceRate = 0;
    private int foodToPriceRate = 0;
    private String comment = "";
    private final int rateCount = 7;

    private Date date;

    public double getProcessedRating(){

        double body = this.personnelRate + this.tasteRate + this.locationRate + this.cleanRate + this.moodRate + this.serviceToPriceRate + this.foodToPriceRate;
        return round(body/this.rateCount, 2);
    }
    //todo to service
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
