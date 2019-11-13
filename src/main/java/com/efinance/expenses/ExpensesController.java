package com.efinance.expenses;

import com.efinance.account.user.User;
import com.efinance.expenses.category.CategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/expenses")
@Slf4j
public class ExpensesController {

    private ExpensesRepository expensesRepo;
    private CategoriesRepository categoriesRepo;

    public ExpensesController(ExpensesRepository expensesRepo, CategoriesRepository categoriesRepo) {
        this.expensesRepo = expensesRepo;
        this.categoriesRepo = categoriesRepo;
    }

    @PostMapping("/new")
    public String newExpense(@Valid ExpenseForm expenseForm, BindingResult bindingResult,
                             @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        //TODO pobieranie aktualnego kursu walut

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.expenseForm", bindingResult);
            redirectAttributes.addFlashAttribute("expenseForm", expenseForm);
            log.error("błąd przy dodawaniu wytraty");
        }
        else{
            Expense expense = expenseForm.toExpense(user);
            log.info(expense.toString());
            expensesRepo.save(expense);
        }

        return "redirect:/home";
    }

}
