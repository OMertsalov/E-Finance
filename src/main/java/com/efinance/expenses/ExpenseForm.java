package com.efinance.expenses;

import com.efinance.account.user.User;
import com.efinance.expenses.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.sql.Date;
import java.util.Calendar;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseForm {

    @DecimalMin(value = "0.1", message = "Enter the amount")
    @Digits(integer = 10, fraction = 2, message = "should be digits here max 10 digits")
    private double amount;
    private Date time;
    private Category category;
    private Money money;

    public enum Money{
        USD(3.88), PLN(1), EUR(4.28);
        public double multiplier;
        Money(double coef){
            multiplier=coef;
        }
    }


    public Expense toExpense(User user){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        time = new Date(calendar.getTimeInMillis());

        return new Expense(amount*money.multiplier, time ,category, user);
    }
}
