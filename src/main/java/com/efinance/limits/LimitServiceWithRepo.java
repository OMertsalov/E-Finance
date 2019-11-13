package com.efinance.limits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class LimitServiceWithRepo implements LimitService{

    @Autowired
    private LimitRepository limitRepo;


    @Override
    public double getMonthLimit(Long userId){
        Optional<Limit> monthLimit = limitRepo.getByUserIdAndMonthAndYear(userId,
                (short)(Calendar.getInstance().get(Calendar.MONTH)+1),
                (short) Calendar.getInstance().get(Calendar.YEAR) );
        return monthLimit.isPresent() ? monthLimit.get().getAmount() : 0;
    }

    @Override
    public double getYearLimit(Long userId){
        Optional<Limit> yearLimit = limitRepo.getByUserIdAndMonthAndYear(userId,(short)0,
                (short) Calendar.getInstance().get(Calendar.YEAR));
        return yearLimit.isPresent() ? yearLimit.get().getAmount() : 0;
    }

    @Override
    public void saveOrUpdate(Limit limit, Long userId){
        Optional<Limit> limitRow = limitRepo
                .getByUserIdAndMonthAndYear(userId,limit.getMonth(),limit.getYear());
        if(limitRow.isPresent()) {
            // if already exist, update just amount
            limitRow.get().setAmount(limit.getAmount());
            limitRepo.save(limitRow.get());
        }
        else limitRepo.save(limit);
    }


}
