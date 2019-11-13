package com.efinance.limits;

import com.efinance.account.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends PagingAndSortingRepository<Limit, Long> {
    Optional<Limit> findByUserIdAndMonthAndYear(long userId, short month, short year);
    Optional<Limit> getByUserIdAndMonthAndYear(Long id, short month, short year);
    Optional<Limit> getByMonthAndYear(Short month, Short year);
}
