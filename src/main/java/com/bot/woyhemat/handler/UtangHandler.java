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

    @GetMapping(value="/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getAllUtang() {
        String respon = "";
        for (Debt x : debtRepo.findAll()) {
            respon += x.getAmount() + " " + x.getPeriod() + "\n";
        }
        return respon;
    }
}