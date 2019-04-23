package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.Debt;
import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UtangHandler extends Handler {

    @Autowired
    DebtRepository debtRepo;

    @Override
    void notifyFacadeNotif() {

    }


    public void tambahUtang(int amount, Date period, User user) {
        Debt utang = new Debt(amount, period, user);
        debtRepo.save(utang);
    }

    public String getUtangUser(String userId) {
        String balasan = "[Utang] : ";
        for (Debt debt : debtRepo.findAll()) {
            if (debt.getUser().getUsername().equals(userId)) {
                balasan += "userId: " + debt.getUser().getUsername() + " amount: " + debt.getAmount() + " period: " + debt.getPeriod() + " ; ";
            }
        }
        return balasan;
    }


}