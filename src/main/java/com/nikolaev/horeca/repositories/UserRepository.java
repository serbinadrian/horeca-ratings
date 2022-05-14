package com.nikolaev.horeca.repositories;

import com.nikolaev.horeca.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    User findByName(String name);
    User findByEmail(String email);
    User getByName(String name);
}