package com.efinance.statistics;

import com.efinance.account.user.User;
import com.efinance.expenses.Expense;
import com.efinance.expenses.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Slf4j
@Controller
@RequestMapping("/stats/byCategory")
public class StatisticsController {


    private ExpenseService expenseService;

    public StatisticsController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/currentMonth")
    public String showCurrentMonthStats(ModelMap modelMap, @AuthenticationPrincipal User user){
        List<Expense> expenses = expenseService.getAllMonthExpenses(user.getId());

        Map<String, Double> expenseMap = getCategorySumPair(expenses);
        modelMap.addAttribute("expenseMap", expenseMap);

        StatisticForm statForm = new StatisticForm();
        modelMap.addAttribute("statForm", statForm);

        return "statistics";
    }

    @GetMapping("/selectedTime")
    public String showStatsByMonthAndYear(ModelMap modelMap,
                                              StatisticForm statForm, @AuthenticationPrincipal User user){

        List<Expense> expenses = null;
        if(statForm.getIsYear())  //get spending for whole year
            expenses = expenseService.getAllExpensesByYear(user.getId(),statForm.getYear());
        else
            expenses = expenseService.getAllExpensesByMonthAndYear(user.getId()
                    ,statForm.getMonth(), statForm.getYear());

        Map<String, Double> expenseMap = getCategorySumPair(expenses);

        modelMap.addAttribute("expenseMap", expenseMap);
        modelMap.addAttribute("statForm", statForm);

        return "statistics";
    }

    private Map<String, Double> getCategorySumPair(List<Expense> expenseList) {
        return expenseList.stream().
                collect(groupingBy(expense -> expense.getCategory().getName(),
                        summingDouble(Expense::getAmount)));
    }

}
