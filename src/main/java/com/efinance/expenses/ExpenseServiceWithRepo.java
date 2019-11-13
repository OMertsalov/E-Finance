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
    public double getSumOfMonthExpenses(Long userId) {
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
    public List<Expense> getAllMonthExpenses(Long userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date from  = new Date(calendar.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = new Date(calendar.getTimeInMillis());
        return expensesRepo.findByUserIdAndTimeBetween(userId, from, to);
    }

    @Override
    public double getSumOfYearExpenses(Long userId) {
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
    public List<Expense> getAllExpensesByYear(Long userId, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date from = new Date(calendar.getTimeInMillis());
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = new Date(calendar.getTimeInMillis());
        return expensesRepo.findByUserIdAndTimeBetween(userId, from, to);
    }

    @Override
    public List<Expense> getAllExpensesByMonthAndYear(Long userId, int month, int year) {
        month -= 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        Date from = new Date(calendar.getTimeInMillis());
        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = new Date(calendar.getTimeInMillis());
        return expensesRepo.findByUserIdAndTimeBetween(userId, from, to);
    }

    @Override
    public double getSumOfDayExpenses(Long userId) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        double sum=0;
        sum = expensesRepo.findByUserIdAndTime(userId, date)
                .stream().mapToDouble(Expense::getAmount).sum();
        return sum;
    }

    @Override
    public double getSumOfWeekExpenses(Long userId)  {
        Calendar c = Calendar.getInstance();
        Date to = new Date(c.getTimeInMillis());
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-6);
        Date from = new Date(c.getTimeInMillis());
        double sum=0;
        sum = expensesRepo.findByUserIdAndTimeBetween(userId, from, to).stream().
                mapToDouble(expenses -> expenses.getAmount()).sum();
        return sum;
    }

    @Override
    public List<Expense> showPageableAllOrderByTimeDesc(Long userId, int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("time").descending());
        return expensesRepo.findAllByUserId(userId,pageable);
    }
}
