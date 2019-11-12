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

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public String registration(RegistrationForm registrationForm){
        return "authorization/registration";
    }


    @PostMapping
    public String registerNewUser(RegistrationForm registrationForm, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "register_page";

        log.info("rejestracja: "+ registrationForm);
        userRepo.save(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
