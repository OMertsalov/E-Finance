package com.efinance.email;

import com.efinance.account.user.User;
import com.efinance.account.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ResetPassController {

    private UserRepository userRepo;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    public ResetPassController(UserRepository userRepo
            ,EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public String getForgotPage(){
        return "authorization/forgot";
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public String sendRestorePassToken(ModelAndView modelAndView,
                                       @RequestParam("email") String email, HttpServletRequest request){

        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setResetToken(UUID.randomUUID().toString());
            userRepo.save(user);
            String appUrl =request.getScheme() + "://" +
                    request.getServerName() +
                    ("http".equals(request.getScheme()) && request.getServerPort() == 80
                            || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":"
                            + request.getServerPort() )
                    +"/reset?token="
                    +user.getResetToken();
            emailService.sentResetToken(user.getEmail(), appUrl);
            modelAndView.addObject("Tmessage", "A password reset link has been sent to " + email);
        }
        else
            modelAndView.addObject("Tmessage", email + " doesn't exist, you can create new account");

        return "authorization/forgot";
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String getResetPage(Model model, @RequestParam("token") String token){

        Optional<User> user = userRepo.findByResetToken(token);
        if (user.isPresent())  // Token found in DB
            model.addAttribute("token", user.get().getResetToken());
        else  // Token not found in DB
            model.addAttribute("errorMessage", "Oops!  This is an invalid password reset link.");

    return "authorization/reset_password";
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetPassword(Model model, @RequestParam Map<String, String> requestParams){
        String token = requestParams.get("token");
        String password = requestParams.get("password");
        Optional<User> user = userRepo.findByResetToken(token);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(password));
            user.get().setResetToken(null);
            userRepo.save(user.get());
            return "redirect:/login";
        }
        else
            model.addAttribute("message", "Oops!  This is an invalid password reset link.");

        return "authorization/reset_password";
    }
}
