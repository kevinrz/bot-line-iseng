package com.bot.woyhemat;

import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.ExpenditureRepository;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@LineMessageHandler
 public class Controller {
    //handlers
    @Autowired
    LineMessagingClient lineMessagingClient;

    // REFACTOR THIS
    AktivitasHandler aktivitasHandler = new AktivitasHandler();
    LaporanHandler laporanHandler = new LaporanHandler();
    UtangHandler utangHandler = new UtangHandler();

    @Autowired
    public UserRepository  userRepo;

    @Autowired
    public DebtRepository repoDebt;

    @Autowired
    public ExpenditureRepository expenses;

    @Autowired
    ExpenditureRepository expenseRepo;


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
            String reply = newUser(userId, Integer.parseInt(splitMessageString[1]),userRepo);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

        } else if (splitMessageString[0].equals("tambah") && splitMessageString[1].equals("pengeluaran")) {
            String reply = kategori();
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

            // String reply = tambahAktivitas(userId, splitMessageString);
            // lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

        } else if (splitMessageString[0].equals("makanan") || splitMessageString[0].equals("hiburan") || splitMessageString[0].equals("lainnya")) {
            // example: makanan 10000 Ayam Goreng
            String kategori = splitMessageString[0];
            int jumlah = Integer.parseInt(splitMessageString[1]);
            String deskripsi = "";
            for (int i = 2; i < splitMessageString.length; i++) {
                deskripsi += splitMessageString[i];
            }
            String reply = tambahAktivitas(userId, kategori, jumlah, deskripsi, userRepo, expenses);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

        } else if (splitMessageString[0].equals("info")) {
            String reply = info();
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));


        } else if (splitMessageString[0].equals("laporan")) {
            String reply = showPengeluaranSebulan(userId, userRepo, expenseRepo);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));

        } else if (splitMessageString[0].equals("target")) {
            String reply = setTarget(userId, Integer.parseInt(splitMessageString[1]));
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));
        }
        // utang <jumlah> <Deadline utang dalma hari> <keterangan>
        // utang 5000 5 ke paijo
        else if (splitMessageString[0].equals("utang")) {
            System.out.println("masuk utang"); // LOG
//            String balasan = "User " + userId + " ngutang " + splitMessageString[1];



            LocalDate tanggal = LocalDate.now();
            tanggal = tanggal.plusDays(Integer.parseInt(splitMessageString[2]));
            Date tanggalDate = asDate(tanggal);

            String balasan = "Berhasil menambahkan utang sebesar " + splitMessageString[1] + ", jatuh tempo dalam "
                    + splitMessageString[2] + " hari, tulis 'lihatutang' tanpa tanda kutip untuk melihat semua utang";


            String keterangan =getKeteranganUtang(splitMessageString);

            tambahUtang(Integer.parseInt(splitMessageString[1]),tanggalDate, userId, keterangan, repoDebt);

            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        }

        // lihatutang
        else if (splitMessageString[0].equals("lihatutang")) {
            System.out.println("masuk lihatutang"); // LOG
            String balasan = utangHandler.getUtangUser(userId, repoDebt);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        } else {
            String reply = salah();
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(reply)));
        }


    }

    // Convert LocalDate to Date
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    // Mendapatkan keterangan utang
    public String getKeteranganUtang(String[] string) {
        String hasil = "";
        for (int i = 3; i < string.length; i++) {
            hasil += string[i] + " ";
        }

        return hasil;
    }


    //+pengeluaran template = tambah pengeluaran *kategori* *amount* *deskripsi*
    
    public String tambahAktivitas(String userId, String kategori, int jumlah, String deskripsi, UserRepository repoU, ExpenditureRepository repoE) {
        if(repoU.findByUsername(userId) == null) {
            return "Maaf Anda belom terdaftar";
        }
        else {
            User thisUser = repoU.findByUsername(userId);
            aktivitasHandler.tambahPengeluaran(userId, kategori, jumlah, deskripsi,thisUser,repoE);
            return "Pengeluaran berhasil di tambah";

        }
    }

    // register template : daftar *target*
    // eg daftar 1000000
    public String newUser(String userId, int target, UserRepository repo){
        if(repo.findByUsername(userId) != null){

            return "Maaf Anda sudah terdaftar";    
        } else {
            User newUser = new User(userId,target,0);
            repo.save(newUser);
            return "Anda telah terdaftar!";
        }     
    }

    // method yang dijalankan ketika menambahkan utang ke suatu user
    // method ini menggunakan utangHandler
    public void tambahUtang(int jumlah, Date waktu, String username, String keterangan, DebtRepository repo) {
        System.out.println("JALAN TAMBAH UTANG"); // LOG
        User theUser = userRepo.findByUsername(username);
        UtangHandler utangHandlerObj = utangHandler;
        utangHandlerObj.tambahUtang(jumlah, waktu, theUser, keterangan, repo);
    }


    public String showPengeluaranSebulan(String username, UserRepository userRepo, ExpenditureRepository expenseRepo) {
        String reply = laporanHandler.showPengeluaranSebulan(username, userRepo, expenseRepo);
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

    public String infoTambah(){
        return "Cara menggunakan fitur tambah:\n" +
                "*kategogori* *jumlahuang* *deskripsi";
    }
    public String infoUtang(){
        return "Cara menggunakan fitur Utang:\n" +
                "*kategogori* *jumlahuang* *deskripsi";
    }




    public String salah() {

        return "Fitur tidak tersedia";
    }

    public String setTarget(String userId, int jumlah) {
        User user = userRepo.findByUsername(userId);
        user.setTarget(jumlah);
        return "Anda telah menentukan target pengeluaran sebesar " + jumlah;
    }

    public String kategori(){
        return "Pilih satu kategori: \n - Makanan \n - Hiburan \n - Lainnya  \n Seterusnya ketik *kategori yang dipilih* *total pengeluaran* *deskripsi*";
    }


}
