package com.bot.woyhemat;

import com.bot.woyhemat.database.DebtRepository;
import com.bot.woyhemat.database.User;
import com.bot.woyhemat.database.UserRepository;
import com.bot.woyhemat.handler.UtangHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WoyHematApplicationTests {
    UtangHandler utangHandler;

    @Autowired
    DebtRepository repoUtang;

    @Autowired
    UserRepository repoUser;

    @Before
    public void setUp() {
        utangHandler = new UtangHandler();

        repoUser.save(new User("12345", 200, 300));
    }


    @Test
    public void contextLoads() {
    }

    @Test
    public void testTambahUtangWork() {
        utangHandler.tambahUtang(200, new Date(), repoUser.findByUsername("12345").get(0), repoUtang);


        assertNotNull(repoUtang.findAll());
    }

    @Test
    public void testGetUtang() {
        utangHandler.tambahUtang(200, new Date(), repoUser.findByUsername("12345").get(0), repoUtang);
        assertEquals("[Utang]", utangHandler.getUtangUser("12345", repoUtang).substring(0, 7));
    }

}
