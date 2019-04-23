package com.bot.woyhemat;

import com.bot.woyhemat.database.Debt;
import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.UserRepository;
import com.bot.woyhemat.handler.Handler;
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

    @Autowired
    UserRepository repoUser;

    @Autowired
    DebtRepository repoDebt;

    UtangHandler utangHandler = new UtangHandler();

    public Controller() {
        //create handler objects
    }

    @EventMapping
    public void messageEventHandleText(MessageEvent<TextMessageContent> event) {
        System.out.println("JALAN OI");

        // Mendapatkan UserId akun LINE
        String userId = event.getSource().getUserId();

        TextMessageContent message = event.getMessage();
        String messageString = message.getText().toLowerCase();
        String[] splitMessageString = messageString.split(";");
        System.out.println("\n==================");
        System.out.println(messageString);
        System.out.println("<><>");
        System.out.println(splitMessageString[0]);
        System.out.println("==================\n");

        if (splitMessageString[0].equals("/register")) {
            String balasan = "User " + splitMessageString[1] + " ditambahkan, UserId: " + userId;

            User user = new User(userId, Integer.parseInt(splitMessageString[2]), 0);
            repoUser.save(user);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        }

        if (splitMessageString[0].equals("/utang")) {
            String balasan = "User " + userId + " ngutang " + splitMessageString[1];
            tambahUtang(Integer.parseInt(splitMessageString[1]), new Date(), userId);

//            Debt utang = new Debt(Integer.parseInt(splitMessageString[1]), new Date(), (User) repoUser.findByUsername(userId).get(0));
//            repoDebt.save(utang);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        }

        if (splitMessageString[0].equals("/lihatutang")) {
            String balasan = utangHandler.getUtangUser(userId);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage(balasan)));
        }

//        String jawaban = notif.balasChatDenganRandomJawaban("sesuatu");
//        balasChat(replyToken, jawaban);

//        lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("Hello!")));
    }

    public void tambahAktivitas(int jumlah) {
        //TODO
    }

    public void tambahUtang(int jumlah, Date waktu, String username) {
        User theUser = (User) repoUser.findByUsername(username).get(0);
        UtangHandler utangHandlerObj = utangHandler;
        utangHandlerObj.tambahUtang(jumlah, waktu, theUser);
    }


    public void setTarget(int jumlah) {
        //TODO
    }

}
