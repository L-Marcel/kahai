package org.kahai.framework.repositories;

import java.util.Optional;
import java.util.UUID;

import org.kahai.framework.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByEmail(String email);
};
