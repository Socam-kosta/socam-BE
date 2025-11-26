package org.example.socam_be.repository;

import org.example.socam_be.domain.user.RefreshToken;
import org.example.socam_be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);
    RefreshToken findByRefreshToken(String refreshToken);
}
