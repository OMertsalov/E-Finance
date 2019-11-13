package com.efinance.email;

import com.efinance.account.user.User;
import com.efinance.account.user.UserRepository;
import com.efinance.expenses.ExpenseService;
import com.efinance.limits.LimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Locale;

@Service
@Slf4j
public class EmailService {

    private JavaMailSender javaMailSender;
    private ExpenseService expenseService;
    private LimitService limitService;
    private UserRepository userRepo;

    public EmailService(JavaMailSender javaMailSender, ExpenseService expenseService,
                        LimitService limitService, UserRepository userRepo) {
        this.javaMailSender = javaMailSender;
        this.expenseService = expenseService;
        this.limitService = limitService;
        this.userRepo = userRepo;
    }

    //second, minute, hour, day of month, month, day(s) of week
    //(*) means match any
    //*/X means "every X"
    //? ("no specific value")
    @Scheduled(cron = "0 0 19 28-31 * ?")
    public void sendMonthReport() throws MailException
    {
        Calendar c = Calendar.getInstance();
        if(!(c.get(Calendar.DAY_OF_MONTH) == c.getActualMaximum(Calendar.DAY_OF_MONTH)))
            return;
        Iterable<User> users = userRepo.findAll();
        for(User user : users) {
            if(user.isMonthReport()) {
                log.info("sending monthly expenses email: "+ user.getEmail());
                double sumOfMonthExpenses = expenseService.getSumOfMonthExpenses(user.getId());
                double limit = limitService.getMonthLimit(user.getId());

                String mailContent = "Hello, your month report: \n"+
                        "Waisted: " + sumOfMonthExpenses+ "\n"+
                        "Limit: " + limit + "\n"+
                        "Balance according your month limit: " + (limit - sumOfMonthExpenses)+"\n" +
                        "Balance according to year limit: "
                        +(limitService.getYearLimit(user.getId())-expenseService
                                                                    .getSumOfYearExpenses(user.getId())
                        +"Average expenses of current month: "
                        +sumOfMonthExpenses/c.getActualMaximum(Calendar.DAY_OF_MONTH)+" PLN per day");


                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(user.getEmail());
                mail.setSubject("Your month report for "
                        + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH ));
                mail.setText(mailContent);

                javaMailSender.send(mail);
            }
        }
    }

    @Scheduled(cron = "0 0 19 ? * SUN")
    public void sendWeeklyReport() throws MailException
    {
        Calendar c = Calendar.getInstance();
        Iterable<User> users = userRepo.findAll();
        for(User user : users){
            if(user.isWeeklyReport()) {
                log.info("sending weekly report email: "+ user.getEmail());
                double weeklySpending = expenseService.getSumOfWeekExpenses(user.getId());
                double limit = limitService.getMonthLimit(user.getId());

                String mailContent = "Hello, your weekly report: \n"+
                        "Waisted: " + weeklySpending+ "\n"+
                        "Month limit: " + limit + "\n"+
                        "Balance according to year limit: "
                        +(limitService.getYearLimit(user.getId())
                                                    -expenseService.getSumOfYearExpenses(user.getId()))
                        +"\n Average weekly spending: "+ Math.round(weeklySpending/7)+" PLN per day";

                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(user.getEmail());
                mail.setSubject("Your weekly report for current week");
                mail.setText(mailContent);
                javaMailSender.send(mail);
            }
        }
    }

    public void sentResetToken(String to, String url){
        String mailContent = "To reset your password, click the url below:\n" + url;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Reset request");
        mail.setText(mailContent);
        javaMailSender.send(mail);
        log.info("ResetToken sent to: "+to);
    }
}
