package com.efinance.expenses;

import java.util.List;

public interface ExpenseService {
    double getDayExpenses(Long userId);
    double getMonthExpenses(Long userId);
    double getYearExpenses(Long userId);
    List<Expense> showPageableAllOrderByTimeDesc(Long userId, int page, int size);
}
