package com.bot.woyhemat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.bot.woyhemat.database.ExpenditureRepository;
import com.bot.woyhemat.database.UserRepository;
import com.linecorp.bot.model.profile.UserProfileResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest{


    String userIdTemp = "asdf";


    Controller controller = new Controller();

    @Autowired
    UserRepository repo;

    @Autowired
    ExpenditureRepository repoE;


    @Test
    public void testSaveUser(){
        controller.newUser(userIdTemp, 1000, repo);
        assertNotNull(repo.findAll());
    }

    @Test
    public void testSaveUserAlreadyRegistered(){
        String reply = controller.newUser(userIdTemp, 1000, repo);
        String reply2 = controller.newUser(userIdTemp, 1000, repo);
        assertEquals(reply2, "Maaf Anda sudah terdaftar");
    }

    @Test
    public void testTambahPengeluaran(){
        controller.tambahAktivitas(userIdTemp, "makanan", 1000, "ayam goreng", repo, repoE);
        assertNotNull(repoE.findAll());
    }

	// @Test
	// public void addObserver() throws Exception{

	// }
}
