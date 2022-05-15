package com.nikolaev.horeca.domains;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Organizations")
@EnableAutoConfiguration
public class Organization {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private double rating;
    private double starRating;
    private int userRatesCount;
    private String name;
    private String location;
    private String specs;
    private String email;
    private String website;
    private String vkLink;
    private String fbLink;
    private String instLink;

    private transient List<Integer> starsMarkup;


    public Organization() {
        starsMarkup = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStarsMarkup(){
        List<Integer> starMarkup = new ArrayList<>();
        for (double i = 0; i < 5; i += 0.5){
            int vari = 0;
            if(i < starRating){
                vari = 1;
                starMarkup.add(vari);
            } else {
                starMarkup.add(vari);
            }
        }
        System.out.println(starMarkup);
        this.starsMarkup = starMarkup;
    }

    public List<Integer> getStarsMarkup() {
        return starsMarkup;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getStarRating() {
        return starRating;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public int getUserRatesCount() {
        return userRatesCount;
    }

    public void setUserRatesCount(int userRatesCount) {
        this.userRatesCount = userRatesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVkLink() {
        return vkLink;
    }

    public void setVkLink(String vkLink) {
        this.vkLink = vkLink;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getInstLink() {
        return instLink;
    }

    public void setInstLink(String instLink) {
        this.instLink = instLink;
    }
}
