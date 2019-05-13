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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtangHandlerTest {
    UtangHandler utangHandler;

    @Autowired
    DebtRepository repoUtang;

    @Autowired
    UserRepository repoUser;

    @Autowired
    Controller control;

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
        utangHandler.tambahUtang(200, new Date(), repoUser.findById(1).get(), "test123", repoUtang);
        String reportUtang = utangHandler.getUtangUser(repoUser.findByUsername("12345").getUsername(), repoUtang);


        assertNotNull(reportUtang);
    }

    @Test
    public void testGetUtang() {
        utangHandler.tambahUtang(200, new Date(), repoUser.findById(1).get(), "test123", repoUtang);
        assertEquals("[Utang]", utangHandler.getUtangUser("12345", repoUtang).substring(0, 7));
    }

    @Test
    public void testNotify() {
        utangHandler.notifyFacadeNotif();
    }

    @Test
    public void testGetKeteranganUtang() {
        Controller control = new Controller();
        String[] testString = new String[]{"utang", "500", "5", "tes", "keterangan"};
        System.out.println(testString[0]);
        String hasil = control.getKeteranganUtang(testString);
        assertEquals("tes keterangan ", hasil);
    }

    @Test
    public void testNoUserTambahUtang() {


        Boolean hasil = control.tambahUtang(200, new Date(), "abcde", "asd");
        assertFalse(hasil);
    }

    @Test
    public void testDateToString() {
        String hasil = utangHandler.dateToString(new Date(1));
        System.out.println(hasil);
        assertEquals("01-01-1970",hasil);
    }




}
