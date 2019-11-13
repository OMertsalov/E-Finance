package com.efinance.limits;

import com.efinance.account.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "limits")
@Data
@NoArgsConstructor
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  double amount;
    private  Short month;
    private  Short year;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;

    public Limit(double amount, Short month, Short year, User user) {
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.user = user;
    }
}
