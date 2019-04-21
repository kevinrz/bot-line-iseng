package com.bot.woyhemat;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

public class FacadeNotifikasi {
    @Autowired
    private LineMessagingClient lineMessagingClient;


    public void balasChatDenganRandomJawaban(String replyToken, String jawaban) {
//        System.out.println("\n------\nJALAN balasChatDenganRandomJawaban\n------\n");

        TextMessage jawabanDalamBentukTextMessage = new TextMessage(jawaban);
        try {
            lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, jawabanDalamBentukTextMessage))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }
}
