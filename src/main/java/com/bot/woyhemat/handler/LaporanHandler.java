package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.Expenditure;
import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.ExpenditureRepository;
import com.bot.woyhemat.database.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Observer;
import java.util.List;

public class LaporanHandler extends Handler {
    UserRepository users;
    ExpenditureRepository expenses;

    /**
     * Method untuk notify semua observer
     * TODO implementasi menunggu facade notifikasi
     */
    @Override
    void notifyFacadeNotif() {
    //     for (Observer observer : observer) {
    //         observer.update();
    //     }
    }

    public String showPengeluaranSebulan(String userId) {
        User thisUser = users.findByUsername(userId);

        List<Expenditure> thisExpenses = expenses.findByUser(thisUser);

        String reply = "";
        for (int i = 0; i < thisExpenses.size(); i++) {
            reply += "Category: " + thisExpenses.get(i).getCategory();
            reply += "\nDescription: " + thisExpenses.get(i).getCategory();
            SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
            reply += "\nTime: " + formattedDate;
            reply += "\nAmount: " + thisExpenses.get(i).getAmount();
            reply += "\n\n";
        }
        return reply;

        // TODO BATASI SEBULAN

    }
}