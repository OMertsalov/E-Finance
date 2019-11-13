package com.efinance.expenses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class ExpenseServiceWithRepo implements ExpenseService {

    @Autowired
    private ExpensesRepository expensesRepo;

    @Override
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

    @Override
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

    @Override
    public double getDayExpenses(Long userId) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        return expensesRepo.findByUserIdAndTime(userId, date)
                .stream().mapToDouble(Expense::getAmount).sum();
    }

    @Override
    public List<Expense> showPageableAllOrderByTimeDesc(Long userId,int page,int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("time").descending());
        return expensesRepo.findAllByUserId(userId,pageable);
    }
}
