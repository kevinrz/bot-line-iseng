package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.Expenditure;
import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.ExpenditureRepository;
import com.bot.woyhemat.database.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Observer;
import java.util.List;

import java.lang.NullPointerException;

public class LaporanHandler extends Handler {

    @Override
    void notifyFacadeNotif() {
    }

    public String showPengeluaranSebulan(String userId, UserRepository users, ExpenditureRepository expenses) {
        User thisUser = users.findByUsername(userId);

        if (thisUser == null) {
            return "Maaf, anda belum terdaftar.";
        }

        List<Expenditure> thisExpenses = expenses.findByUser(thisUser);

        if (thisExpenses.isEmpty()) {
            return "Anda belum memiliki pengeluaran bulan ini.";
        }

        String reply = "";
        int total = 0;
        int counter = 1;
        reply += "Laporan pengeluaran sebulan terakhir: \n";
        
        String lastFormattedDate = "";

        for (int i = 0; i < thisExpenses.size(); i++) {
            String currentFormattedDate = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(thisExpenses.get(i).getTimestamp());
            String formattedTime = new SimpleDateFormat("HH:mm").format(thisExpenses.get(i).getTimestamp());

            if (i != 0) {
                if (!currentFormattedDate.equals(lastFormattedDate)) {
                    reply += "\n" + currentFormattedDate + "\n";
                    counter = 1;
                }
            } else {
                reply += "\n" + currentFormattedDate + "\n";
            }

            lastFormattedDate = currentFormattedDate;

            reply += counter + ")";
            reply += "\tKategori: " + thisExpenses.get(i).getCategory();
            reply += "\n\tDeskripsi: " + thisExpenses.get(i).getDescription();
            reply += "\n\tJumlah: " + thisExpenses.get(i).getAmount();
            reply += "\n\tJam: " + formattedTime;
            reply += "\n\n";
            total += thisExpenses.get(i).getAmount();
            counter++;
        }
        reply += "Total pengeluaran bulan ini: " + total;
        return reply;

        // TODO BATASI SEBULAN

    }
}