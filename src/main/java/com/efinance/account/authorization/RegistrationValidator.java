package com.efinance.account.authorization;

import com.efinance.account.user.User;
import com.efinance.account.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@Slf4j
public class RegistrationValidator implements Validator {

    @Autowired
    UserRepository userRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegistrationForm form = (RegistrationForm) o;
        User user = userRepo.findByLogin(form.getLogin());
        log.info("Sprawdzanie unikatowosci username: "+ form);
        if(user!=null)
             //second argument login used on thymeleaf
            errors.rejectValue("login", "login" ,"Username already exists");

        Optional<User> optionalUser = userRepo.findByEmail(form.getEmail());
        if(optionalUser.isPresent())
            errors.rejectValue("email", "email", "Account with this email already exists");

    }
}
