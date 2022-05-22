package com.nikolaev.horeca.services;

import com.nikolaev.horeca.domains.Organization;
import com.nikolaev.horeca.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void insertParams(String search, String sort, String[] filters) {

        System.out.println("search query: " + search);
        System.out.println("sort query: " + sort);
        System.out.println("filters query: " + Arrays.toString(filters));

        this.search = Objects.requireNonNullElse(search, "");
        this.sort = Objects.requireNonNullElse(sort, "sortRateDesc");
        if(filters != null){
            this.filters = new ArrayList<>(List.of(filters));
        }
        else {
            this.filters = new ArrayList<>();
        }

        this.newParamValue = true;
    }

    public boolean isChanged(String search, String sort, String[] filters){
        String currentSearch = Objects.requireNonNullElse(search, "");
        String currentSort = Objects.requireNonNullElse(sort, "sortRateDesc");
        List<String> currentFilters = new ArrayList<>();
        if(filters != null){
            currentFilters = new ArrayList<>(List.of(filters));
        }

        return !Objects.equals(this.search, currentSearch) || !Objects.equals(this.sort, currentSort) || this.filters != currentFilters;
    }

    public void build() {

        this.data = new ArrayList<>();
        List<Organization> data = switch (this.sort) {
            case "sortRateDesc" -> organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
            case "sortRateAsc" -> organizationRepository.findAll(Sort.by(Sort.Direction.ASC, "rating"));
            case "sortAlphabetDesc" -> organizationRepository.findAll(Sort.by("name"));
            case "sortAlphabetAsc" -> organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
            default -> new ArrayList<>();
        };

        System.out.println("found rows: " + data.size());
        int size = this.filters.size();
        if (size != 0 || !this.search.equals("")) {
            List<Integer> filters = getFilters();
            for (Organization organization : data) {
                boolean searchPassed = organization.getName().toLowerCase().contains(this.search.toLowerCase());
                boolean instPassed = true;
                boolean fbPassed = true;
                boolean vkPassed = true;
                if (filters.get(0) == 1) {
                    instPassed = !organization.getInstLink().equals("");
                }
                if (filters.get(1) == 1) {
                    fbPassed = !organization.getFbLink().equals("");
                }
                if (filters.get(2) == 1) {
                    vkPassed = !organization.getVkLink().equals("");
                }
                if (searchPassed && fbPassed && instPassed && vkPassed) {
                    this.data.add(organization);
                }
            }
        } else {
            this.data = data;
        }
        this.isSet = true;
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
            case "sortRateDesc" -> sort.set(0, 1);
            case "sortRateAsc" -> sort.set(1, 1);
            case "sortAlphabetDesc" -> sort.set(2, 1);
            case "sortAlphabetAsc" -> sort.set(3, 1);
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
