package com.efinance.limits;

import com.efinance.account.user.User;
import com.efinance.expenses.ExpenseForm.Money;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;


@Data
@NoArgsConstructor
public class LimitForm {

    @DecimalMin(value = "0.00", message = "minimal value is 0")
    @Digits(message = "Two digits after point", fraction = 2, integer = 10)
    private double amount;
    private Short month;//0 means that gonna set year's limit
    private Boolean isYear;
    private Short year;
    private Money money;

    public Limit toLimit(User user) {
        if (isYear)
            return new Limit(amount * money.multiplier, (short) 0, year, user);
        else
            return new Limit(amount * money.multiplier, month, year, user);
    }

}
