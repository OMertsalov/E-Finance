package com.efinance.limits;

public interface LimitService {
    double getMonthLimit(Long userId);

    double getYearLimit(Long userId);

    void saveOrUpdate(Limit limit, Long userId);
}
