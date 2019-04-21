package com.bot.woyhemat;

//handlers

//public Controller(){
//        //create handler objects
//        }
//
//public void tambahAktivitas(int jumlah){
//        //TODO
//        }
//
//public void tambahUtang(int jumlah, int masaLenggang){
//        //TODO
//        }
//
//public void setTarget(int jumlah){
//        //TODO
//        }


import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.UserRepository;
import com.bot.woyhemat.handler.AktivitasHandler;
import com.bot.woyhemat.handler.Handler;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;


@LineMessageHandler
@org.springframework.stereotype.Controller
@RestController
public class Controller{
    @Autowired
    UserRepository repoUser;
    private ArrayList<User> userCoba = new ArrayList<>();


    @Autowired
    private LineMessagingClient lineMessagingClient;

    private FacadeNotifikasi notif = new FacadeNotifikasi();
    private Handler aktivitasHandler = new AktivitasHandler();

    @EventMapping
    // Inti dari bales pesannya disini
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        String pesan = messageEvent.getMessage().getText().toLowerCase();
        String replyToken = messageEvent.getReplyToken();
        User coba = new User(pesan, 3000, 200);
        repoUser.save(coba);
        String jawaban = notif.balasChatDenganRandomJawaban("sesuatu");
        balasChat(replyToken, jawaban);
    }

//    @GetMapping("/register")
//    public String cv() {
//        return "haha";
//
//
//    }

    private void balasChat(String replyToken, String jawaban) {

        TextMessage jawabanDalamBentukTextMessage = new TextMessage(jawaban);
        try {
            lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, jawabanDalamBentukTextMessage))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }

    @GetMapping(value="/register", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {

        return "This is register page";
    }

    @GetMapping(value="/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHello() {
        repoUser.findAll();
        String respon = "";
        for (User x : repoUser.findAll()) {
            respon += x.toString();
        }
        return respon;
    }

}
