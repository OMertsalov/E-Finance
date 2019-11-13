package com.efinance.account.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;



@Getter
@Entity
@Table(name="user")
@ToString
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Column(name = "first_name", nullable = true, length = 30)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = true, length = 30)
    private String lastName;

    @Setter
    @Column(name = "login", nullable = false, length = 30)
    private String login;

    @Setter
    @Column(name = "password", nullable = false, length = 30)
    private String password;

    //Email
    @Setter
    @Column(name = "email", nullable = false, length = 40)
    private String email;

    @Setter
    @Column(name = "month_report")
    private boolean monthReport;

    @Setter
    @Column(name = "weekly_report")
    private boolean weeklyReport;

    @Setter
    @Column(name = "reset_token")
    private String resetToken;


    public User() {
    }

    public User(String firstName, String lastName, String login, String password, String email, boolean monthReport, boolean weeklyReport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.email = email;
        this.monthReport = monthReport;
        this.weeklyReport = weeklyReport;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }



    @Override
    public String getUsername() {
        return login;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
