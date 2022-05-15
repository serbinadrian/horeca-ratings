package com.nikolaev.horeca.repositories;

import com.nikolaev.horeca.domains.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    boolean existsByComment(String comment);
    List<UserRating> findAllByUserId(long id);
    List<UserRating> findAllByOrganizationId(long id);
}
