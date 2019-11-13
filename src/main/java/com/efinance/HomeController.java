package com.efinance;

import com.efinance.account.user.User;
import com.efinance.expenses.Expense;
import com.efinance.expenses.ExpenseForm;
import com.efinance.expenses.ExpenseService;
import com.efinance.expenses.category.CategoriesRepository;
import com.efinance.expenses.category.Category;
import com.efinance.limits.LimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.*;

@Controller
@Slf4j
@RequestMapping("/home")
public class HomeController {


    private CategoriesRepository categoriesRepo;
    private ExpenseService expenseService;
    private LimitService limitService;

    public HomeController(CategoriesRepository categoriesRepo
            ,ExpenseService expenseService, LimitService limitService) {
        this.categoriesRepo = categoriesRepo;
        this.expenseService = expenseService;
        this.limitService = limitService;
    }

    @GetMapping
    public String mainPage(Model model, @AuthenticationPrincipal User user){

        model.addAttribute("nameOfLogedUser", user.getFirstName());
        model.addAttribute("loginOfLogedUser", user.getLogin());

        ExpenseForm expenseForm = new ExpenseForm();
        model.addAttribute("expenseForm", expenseForm);

        List<Expense> userExpenses = expenseService
                .showPageableAllOrderByTimeDesc(user.getId(),0,10);
        model.addAttribute("expenses", userExpenses);

        List<Category> categories = new ArrayList<>();
        categoriesRepo.findAll().forEach(categories::add);
        model.addAttribute("categories", categories);
        model.addAttribute("currencies", ExpenseForm.Money.values());

        double monthExpenses = expenseService.getSumOfMonthExpenses(user.getId());
        double monthLimit = limitService.getMonthLimit(user.getId());
        double monthRemains = monthLimit-monthExpenses;
        if(monthRemains<0)
            monthRemains=0;


        double yearExpenses  = expenseService.getSumOfYearExpenses(user.getId());
        double yearLimit = limitService.getYearLimit(user.getId());
        double yearRemains = yearLimit - yearExpenses;
        if(yearRemains <0)
            yearRemains=0;

        double dayExpenses = expenseService.getSumOfDayExpenses(user.getId());

        double averageMonthExpenses=monthExpenses/ Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1;

        model.addAttribute("monthExpenses", String.format("%.2f", monthExpenses));
        model.addAttribute("monthRemains", String.format("%.2f", monthRemains));
        model.addAttribute("yearExpenses", String.format("%.2f", yearExpenses));
        model.addAttribute("yearRemains", String.format("%.2f", yearRemains));
        model.addAttribute("dayExpenses", String.format("%.2f", dayExpenses));
        model.addAttribute("averageMonthExpenses", String.format("%.2f", averageMonthExpenses));

       return "home";
    }

}
