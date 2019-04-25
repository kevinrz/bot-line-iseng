package com.bot.woyhemat;

import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.UserRepository;
import com.bot.woyhemat.handler.AktivitasHandler;
import com.bot.woyhemat.handler.LaporanHandler;
import com.bot.woyhemat.handler.UtangHandler;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@LineMessageHandler
public class Controller {
    //handlers
    @Autowired
    LineMessagingClient lineMessagingClient;

    AktivitasHandler aktivitasHandler = new AktivitasHandler();
    LaporanHandler laporanHandler = new LaporanHandler();
    UtangHandler utangHandler = new UtangHandler();

    @Autowired
    UserRepository userRepo;

    @Autowired
    DebtRepository repoDebt;


    @EventMapping
    public void messageEventHandleText(MessageEvent<TextMessageContent> event) {
        System.out.println("JALAN OI"); //LOG
        String userId = event.getSource().getUserId();
        TextMessageContent message = event.getMessage();
        String messageString = message.getText().toLowerCase();
        String[] splitMessageString = messageString.split(" ");
        System.out.println(messageString); //LOG
        System.out.println(splitMessageString[0]); //LOG
        if (splitMessageString[0].equals("daftar")) {
            String reply = newUser(userId, Integer.parseInt(splitMessageString[1]));
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));
        } else if (splitMessageString[0].equals("tambah") && splitMessageString[1].equals("pengeluaran")) {
            String reply = tambahAktivitas(userId, splitMessageString);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));
        } else if (splitMessageString[0].equals("info")) {
            String reply = info();
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

        } else if (splitMessageString[0].equals("laporan")) {
            String reply = showPengeluaranSebulan(userId);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

        } else if (splitMessageString[0].equals("target")) {
            String reply = setTarget(userId, Integer.parseInt(splitMessageString[1]));
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));
        } else if (splitMessageString[0].equals("utang")) {
            System.out.println("masuk utang");
            String balasan = "User " + userId + " ngutang " + splitMessageString[1];
            tambahUtang(Integer.parseInt(splitMessageString[1]), new Date(), userId, repoDebt);

//            Debt utang = new Debt(Integer.parseInt(splitMessageString[1]), new Date(), (User) repoUser.findByUsername(userId).get(0));
//            repoDebt.save(utang);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        } else if (splitMessageString[0].equals("lihatutang")) {
            System.out.println("masuk lihatutang");
            String balasan = utangHandler.getUtangUser(userId, repoDebt);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        } else {
            String reply = salah();
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));
        }


    }


    //+pengeluaran template = tambah pengeluaran *kategori* *amount* *deskripsi*
    // example: tambah pengeluaran Makanan 10000 Ayam Goreng
    public String tambahAktivitas(String userId, String[] info) {
        String kategori = info[2];
        int jumlah = Integer.parseInt(info[3]);
        String deskripsi = "";
        for (int i = 4; i < info.length; i++) {
            deskripsi += info[i];
        }
        aktivitasHandler.tambahPengeluaran(userId, kategori, jumlah, deskripsi, userRepo);
        return "Pengeluaran berhasil di tambah";

    }

    // register template : daftar *target*
    // eg daftar 1000000
    public String newUser(String userId, int target) {
        User newUser = new User(userId, target, 0);
        userRepo.save(newUser);
        return "Anda telah terdaftar!";
    }

    //
    public void tambahUtang(int jumlah, Date waktu, String username, DebtRepository repo) {
        System.out.println("JALAN TAMBAH UTANG");
        User theUser = userRepo.findByUsername(username);
        UtangHandler utangHandlerObj = utangHandler;
        utangHandlerObj.tambahUtang(jumlah, waktu, theUser, repo);
    }


    public String showPengeluaranSebulan(String username) {
        String reply = "Laporan pengeluaran sebulan terakhir: \n" + laporanHandler.showPengeluaranSebulan(username);
        return reply;
    }

    public String info() {
        return "Berikut fitur-fitur yang terdapat pada WoyHemat! :\n" +
                "- info\n" +
                "- target\n" +
                "- tambah pengeluaran\n" +
                "- laporan\n" +
                "- daftar\n";
    }

    public String salah() {
        return "Fitur tidak tersedia";
    }

    public String setTarget(String userId, int jumlah) {
        User user = userRepo.findByUsername(userId);
        user.setTarget(jumlah);
        return "Anda telah menentukan target pengeluaran sebesar " + jumlah;
    }

}
