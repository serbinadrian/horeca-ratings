package com.nikolaev.horeca.services;

import com.nikolaev.horeca.domains.Organization;
import com.nikolaev.horeca.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SearchAndFilterService {
    @Autowired
    OrganizationRepository organizationRepository;
    List<Organization> data;
    String search;
    String sort;
    List<String> filters;
    boolean newParamValue = false;
    boolean isSet = false;

    public List<Organization> getData() {
        return data;
    }

    public void insertParams(String search, String sort, List<String> filters) {
//        if(!Objects.equals(search, "")){
//            this.search = search;
//        }else{
//            this.search = "";
//        }
//        if(!Objects.equals(sort, "rateDesc")){
//            this.sort = search;
//        }else{
//            this.sort = "rateDesc";
//        }
//        if(filters != null){
//            this.filters = filters;
//        }else{
//            this.filters = new ArrayList<>();
//        }
        System.out.println(search);
        System.out.println(sort);
        System.out.println(filters);

        this.sort = "rateDesc";
        this.search = "";
        this.filters = new ArrayList<>();

        this.newParamValue = true;
    }

    public void build() {

        this.data = new ArrayList<>();
        List<Organization> data = new ArrayList<>();
        switch (this.sort) {
            case "rateDesc" -> data = organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
            case "rateAsc" -> data = organizationRepository.findAll(Sort.by(Sort.Direction.ASC, "rating"));
            case "alphabetDesc" -> data = organizationRepository.findAll(Sort.by("name"));
            case "alphabetAsc" -> data = organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        }
        int size = this.filters.size();
        if (size != 0) {
            List<Integer> filters = getFilters();
            for (Organization organization : data) {
                boolean passed = true;
                if (organization.getName().toLowerCase().contains(this.search)) {
                    if (filters.get(0) == 1) {
                        passed = !organization.getInstLink().equals("");
                    }
                    if (filters.get(1) == 1) {
                        passed = !organization.getFbLink().equals("");
                    }
                    if (filters.get(2) == 1) {
                        passed = !organization.getVkLink().equals("");
                    }
                }
                if (passed) {
                    this.data.add(organization);
                }
            }
        } else {
            this.data = data;
        }
    }


    public String getSearch() {
        return this.search;
    }

    public List<Integer> getSort() {
        List<Integer> sort = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sort.add(0);
        }
        switch (this.sort) {
            case "rateDesc" -> sort.set(0, 1);
            case "rateAsc" -> sort.set(1, 1);
            case "alphabetDesc" -> sort.set(2, 1);
            case "alphabetAsc" -> sort.set(3, 1);
        }
        return sort;
    }

    public List<Integer> getFilters() {
        List<Integer> filters = new ArrayList<>();
        int size = this.filters.size();
        for (int i = 0; i < 3; i++) {
            filters.add(0);
        }
        if (size != 0) {
            for (String filter : this.filters) {
                switch (filter) {
                    case "isInst" -> filters.set(0, 1);
                    case "isFb" -> filters.set(1, 1);
                    case "isVk" -> filters.set(2, 1);
                }
            }
        }
        return filters;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }
}
