package com.efinance.limits;

import com.efinance.account.user.User;
import com.efinance.expenses.category.CategoriesRepository;
import com.efinance.expenses.ExpenseForm.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/limits")
public class LimitController {

   private LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping("/set")
    public String showLimitsPage(Model model, @AuthenticationPrincipal User user){
        LimitForm limitForm = new LimitForm();
        model.addAttribute("limitForm", limitForm);

        double yearLimit = limitService.getYearLimit(user.getId());
        model.addAttribute("yearLimit",yearLimit);

        double monthLimit = limitService.getMonthLimit(user.getId());
        model.addAttribute("monthLimit", monthLimit);

        model.addAttribute("currencies", Money.values());

        return "limits";
    }

    @PostMapping("/new")
    public String addNewLimit(@Valid LimitForm limitForm, BindingResult errors,
                              @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.limitForm",
                    errors);
            redirectAttributes.addFlashAttribute("limitForm", limitForm);
            log.error("błąd przy dodawaniu limitu");
        }
        else{
            Limit limit = limitForm.toLimit(user);
            limitService.saveOrUpdate(limit,user.getId());
        }
        return "redirect:/limits/set";
    }
}
