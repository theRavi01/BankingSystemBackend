package com.banking.system.auth;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.system.entity.Role;
import com.banking.system.entity.User;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);
    public User findByOtp(String code);
}
