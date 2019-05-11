package com.bot.woyhemat.handler;

import com.bot.woyhemat.Controller;
import com.bot.woyhemat.database.Expenditure;
import com.bot.woyhemat.database.ExpenditureRepository;
import com.bot.woyhemat.database.User;
import java.sql.Timestamp;
import java.util.Observer;

public class AktivitasHandler extends Handler {
    



    /**
     * Method untuk notify semua observer
     * TODO implementasi menunggu facade notifikasi
     */
    @Override
    void notifyFacadeNotif() {
        // for (Observer observer : observer) {
        //     observer.update();
        // }
    }

    /**
     * TODO tambahkan pengeluaran ke user di database, implementasi masih menunggu database
     */
    public void tambahPengeluaran(String userId, String katergori, int jumlah, String deskripsi, User thisUser, ExpenditureRepository expenses) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        System.out.println("COBA  ");
        System.out.println(thisUser);
        Expenditure pengeluaran = new Expenditure(katergori,deskripsi,time,jumlah,thisUser);
        expenses.save(pengeluaran);
        thisUser.tambahPengeluaranKeTotal(jumlah);
    }

//    void hapusPengeluaran() {
//        //TODO
//    }

}
