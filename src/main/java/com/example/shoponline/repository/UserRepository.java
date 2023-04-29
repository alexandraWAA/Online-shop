package com.example.shoponline.repository;

import com.example.shoponline.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByUsername(String username);

    void deleteByUsername(String username);
    Optional<User> findByUsernameIgnoreCase(String username);

}
