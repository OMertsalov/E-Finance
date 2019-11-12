package com.efinance.account.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByLogin(String login);
    User findByLoginOrEmail(String login, String email);
}
