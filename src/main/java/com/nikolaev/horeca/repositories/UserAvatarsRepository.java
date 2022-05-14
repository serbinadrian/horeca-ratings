package com.nikolaev.horeca.repositories;

import com.nikolaev.horeca.domains.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvatarsRepository extends JpaRepository<UserAvatar, Long> {
    UserAvatar getByUserId(long id);
}
