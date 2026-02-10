package com.cinema.repository;

import com.cinema.model.PasswordResetToken;
import com.cinema.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for PasswordResetToken
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUtilisateur(Utilisateur utilisateur);

    @Modifying
    void deleteByExpiryDateLessThan(LocalDateTime now);
}
