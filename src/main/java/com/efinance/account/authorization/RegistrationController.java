package com.efinance.account.authorization;

import com.efinance.account.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/registration")
public class RegistrationController {


    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private RegistrationValidator regValidator;

    public RegistrationController(UserRepository userRepo,
                                  PasswordEncoder passwordEncoder,
                                  RegistrationValidator regValidator) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.regValidator=regValidator;
    }


    @GetMapping
    public String registration(RegistrationForm registrationForm){
        return "authorization/registration";
    }


    @PostMapping
    public String registerNewUser(@Valid RegistrationForm registrationForm, BindingResult bindingResult){
        regValidator.validate(registrationForm, bindingResult);
        if(bindingResult.hasErrors())
            return "authorization/registration";

        log.info("rejestracja: "+ registrationForm);
        userRepo.save(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
