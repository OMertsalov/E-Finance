package com.efinance.expenses;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public interface ExpensesRepository extends PagingAndSortingRepository<Expense,Long> {
    List<Expense> findAllByUserId(long userId, Pageable pageable);
    List<Expense> findByUserIdAndTimeBetween(Long id, Date from, Date to);
    List<Expense> findByUserIdAndTime(Long userId, Date date);
}


