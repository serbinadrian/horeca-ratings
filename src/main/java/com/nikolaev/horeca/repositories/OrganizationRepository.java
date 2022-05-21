package com.nikolaev.horeca.repositories;

import com.nikolaev.horeca.domains.Organization;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String name);
    Organization getByName(String name);

    List<Organization> findAllByNameIsLikeIgnoreCase(String query, Sort sort);
}
