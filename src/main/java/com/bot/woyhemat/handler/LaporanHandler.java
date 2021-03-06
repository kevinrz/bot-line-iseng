package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.Expenditure;
import com.bot.woyhemat.database.ExpenditureRepository;
import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.UserRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class LaporanHandler {

    public String showPengeluaranSebulan(String userId, UserRepository users, ExpenditureRepository expenses) {
        User thisUser = users.findByUsername(userId);
        int thisTarget = thisUser.getTarget();

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
        int currentMonth = LocalDate.now().getMonthValue();
        System.out.println("current " + currentMonth);
        System.out.println("data " + thisExpenses.get(0).getTimestamp().getMonth());

        for (int i = 0; i < thisExpenses.size(); i++) {
            if (thisExpenses.get(i).getTimestamp().getMonth() + 1 != currentMonth) break;

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
            reply += "\n\t\tDeskripsi: " + thisExpenses.get(i).getDescription();
            reply += "\n\t\tJumlah: " + thisExpenses.get(i).getAmount();
            reply += "\n\t\tJam: " + formattedTime;
            reply += "\n\n";
            total += thisExpenses.get(i).getAmount();
            counter++;
        }
        int selisih = thisTarget - total;


        reply += "Total pengeluaran bulan ini: " + total;
        reply += "\nTarget pengeluaran anda: " + thisTarget;

        if (selisih < 0) {
            reply += "\n\nTotal pengeluaran anda bulan ini sudah melebihi target anda!";
        } else {
            reply += "\n\nSisa pengeluaran yang bisa anda lakukan: " + selisih;
        }

        return reply;

    }
}