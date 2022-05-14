package com.nikolaev.horeca.repositories;

import com.nikolaev.horeca.domains.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String name);
    Organization getByName(String name);
}
