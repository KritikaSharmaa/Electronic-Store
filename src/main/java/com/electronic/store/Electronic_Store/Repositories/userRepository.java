package com.electronic.store.Electronic_Store.Repositories;

import com.electronic.store.Electronic_Store.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<List<User>> findByNameContaining(String keyword);
}
