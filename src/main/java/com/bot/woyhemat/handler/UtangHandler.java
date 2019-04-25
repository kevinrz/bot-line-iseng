package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.Debt;
import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.User;

import java.util.Date;

/**
 * Class Handler yang menangani semua yang berhubungan dengan utang seperti
 * melihat semua utang dan menambahkan utang user
 *
 * @author Kevin Raikhan Zain
 */
public class UtangHandler extends Handler {


    @Override
    public void notifyFacadeNotif() {

    }

    /**
     * Menambahkan objek Debt ke DebtRepository
     *
     * @param amount jumlah utang
     * @param period objek Date yang menunjukan kapan utang jatuh tempo
     * @param user   objek user yang berhutang
     * @param repo   repository objek yang di pass dari Controller
     */
    public void tambahUtang(int amount, Date period, User user, DebtRepository repo) {
        Debt utang = new Debt(amount, period, user);
        repo.save(utang);
    }

    /**
     * Menampilkan semua utang yang dimiliki user
     *
     * @param userId user id akun line
     * @param repo   objek DebtRepository repo yang di pass dari controller
     * @return semua catatan utang user tersebut dalam bentuk String
     */
    public String getUtangUser(String userId, DebtRepository repo) {
        System.out.println("jalan getUtanguser"); // LOG
        String balasan = "[Utang] : \n";

        for (Debt debt : repo.findAll()) {
            System.out.println("Loop getUtangUser"); // LOG
            if (debt.getUser().getUsername().equals(userId)) {
                balasan += "- Jumlah: " + debt.getAmount() + ", Jatuh Tempo: " + debt.getPeriod() + " \n";
            }
        }
        return balasan;
    }


}