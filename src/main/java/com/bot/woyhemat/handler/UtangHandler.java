package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.Debt;
import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UtangHandler extends Handler {



    @Override
    public void notifyFacadeNotif() {

    }


    public void tambahUtang(int amount, Date period, User user, DebtRepository repo) {
        Debt utang = new Debt(amount, period, user);
        repo.save(utang);
    }

    public String getUtangUser(String userId, DebtRepository repo) {
        System.out.println("jalan getUtanguser");
        String balasan = "[Utang] : ";
        for (Debt debt : repo.findAll()) {
            System.out.println("Loop getUtangUser");
            if (debt.getUser().getUsername().equals(userId)) {
                balasan += "userId: " + debt.getUser().getUsername() + " amount: " + debt.getAmount() + " period: " + debt.getPeriod() + " ; ";
            }
        }
        return balasan;
    }


}