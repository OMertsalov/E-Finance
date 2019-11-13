package com.efinance.expenses;

import java.util.List;

public interface ExpenseService {
    double getSumOfDayExpenses(Long userId);
    double getSumOfMonthExpenses(Long userId);
    double getSumOfYearExpenses(Long userId);

    List<Expense> getAllMonthExpenses(Long userId);
    List<Expense> getAllExpensesByYear(Long userId,int year);
    List<Expense> getAllExpensesByMonthAndYear(Long userId, int month, int year);

    List<Expense> showPageableAllOrderByTimeDesc(Long userId, int page, int size);
}
