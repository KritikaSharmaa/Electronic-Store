package com.electronic.store.Electronic_Store.Repositories;

import com.electronic.store.Electronic_Store.Entities.RefreshToken;
import com.electronic.store.Electronic_Store.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
