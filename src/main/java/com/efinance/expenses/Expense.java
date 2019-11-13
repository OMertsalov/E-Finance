package com.efinance.expenses;

import com.efinance.account.user.User;
import com.efinance.expenses.category.Category;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private final double amount;
    private final Date time;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private final Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private final User user;


}
