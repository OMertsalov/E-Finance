package com.efinance.account.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByLogin(String login);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
}
