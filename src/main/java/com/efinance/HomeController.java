package com.efinance;

import com.efinance.account.user.User;
import com.efinance.expenses.Expense;
import com.efinance.expenses.ExpenseForm;
import com.efinance.expenses.ExpensesRepository;
import com.efinance.expenses.category.CategoriesRepository;
import com.efinance.expenses.category.Category;
import com.efinance.limits.Limit;
import com.efinance.limits.LimitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/home")
public class HomeController {


    private CategoriesRepository categoriesRepo;
    private ExpensesRepository expensesRepo;
    private LimitRepository limitRepo;

    public HomeController(CategoriesRepository categoriesRepo,
                          ExpensesRepository expensesRepo, LimitRepository limitRepo) {
        this.categoriesRepo = categoriesRepo;
        this.expensesRepo = expensesRepo;
        this.limitRepo = limitRepo;
    }

    @GetMapping
    public String mainPage(Model model, @AuthenticationPrincipal User user){

        model.addAttribute("nameOfLogedUser", user.getFirstName());
        model.addAttribute("loginOfLogedUser", user.getLogin());

        ExpenseForm expenseForm = new ExpenseForm();
        model.addAttribute("expenseForm", expenseForm);

        Pageable pageable = PageRequest.of(0,10,Sort.by("time").descending());
        List<Expense> userExpenses = expensesRepo.findAllByUserId(user.getId(),pageable);
        model.addAttribute("expenses", userExpenses);

        List<Category> categories = new ArrayList<>();
        categoriesRepo.findAll().forEach(categories::add);
        model.addAttribute("categories", categories);
        model.addAttribute("currencies", ExpenseForm.Money.values());

        double monthExpenses = getMonthExpenses(user.getId());
        double monthLimit = getMonthLimit(user.getId());
        double monthRemains = monthLimit-monthExpenses;
        if(monthRemains<0)
            monthRemains=0;


        double yearExpenses  = getYearExpenses(user.getId());
        double yearLimit = getYearLimit(user.getId());
        double yearRemains = yearLimit - yearExpenses;
        if(yearRemains <0)
            yearRemains=0;

        double dayExpenses = getDayExpenses(user.getId());

        double averageMonthExpenses=monthExpenses/ Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1;

        model.addAttribute("monthExpenses", String.format("%.2f", monthExpenses));
        model.addAttribute("monthRemains", String.format("%.2f", monthRemains));
        model.addAttribute("yearExpenses", String.format("%.2f", yearExpenses));
        model.addAttribute("yearRemains", String.format("%.2f", yearRemains));
        model.addAttribute("dayExpenses", String.format("%.2f", dayExpenses));
        model.addAttribute("averageMonthExpenses", String.format("%.2f", averageMonthExpenses));

       return "home";
    }

    public double getYearLimit(Long userId) {
        Optional<Limit> yearLimitData = limitRepo.findByUserIdAndMonthAndYear(userId, (short)0,
                (short) Calendar.getInstance().get(Calendar.YEAR));
        return yearLimitData.isPresent() ? yearLimitData.get().getAmount() : 0;
    }


    public double getMonthLimit(Long userId) {
        Optional<Limit> monthLimitData = limitRepo.findByUserIdAndMonthAndYear(userId,
                (short)(Calendar.getInstance().get(Calendar.MONTH)+1),
                (short) Calendar.getInstance().get(Calendar.YEAR));
        return monthLimitData.isPresent() ? monthLimitData.get().getAmount() : 0;
    }

    public double getDayExpenses(Long userId) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        return expensesRepo.findByUserIdAndTime(userId, date)
                .stream().mapToDouble(Expense::getAmount).sum();
    }

    public double getMonthExpenses(Long userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date from  = new Date(calendar.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = new Date(calendar.getTimeInMillis());
        double sum=0;
        sum = expensesRepo.findByUserIdAndTimeBetween(userId, from, to).stream().
                mapToDouble(expenses -> expenses.getAmount()).sum();
        return sum;
    }

    public double getYearExpenses(Long userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date from = new Date(calendar.getTimeInMillis());
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = new Date(calendar.getTimeInMillis());
        double sum=0;
        sum = expensesRepo.findByUserIdAndTimeBetween(userId, from, to).stream().
                mapToDouble(expenses -> expenses.getAmount()).sum();
        return sum;
    }
}
