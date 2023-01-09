package com.instagramclone.respository.auth;

import com.instagramclone.entity.auth.OTVC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtvcRepository extends JpaRepository<OTVC, Long> {
    Optional<OTVC> findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
}
