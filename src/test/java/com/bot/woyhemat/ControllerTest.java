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
    public void testTargetCannotBeNegative(){
        String reply = controller.newUser("qwer", -200, repo);
        assertEquals(reply,"Maaf target tidak boleh kurang dari 0");

    }


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

    @Test

    public void testTambahPengeluaranWithoutUser(){
        String reply = controller.tambahAktivitas("akndn", "makanan", 10000, "ayam goreng", repo, repoE);
        assertEquals(reply, "Maaf Anda belom terdaftar");
    }

    @Test
    public void testJumlahPengeluaranCannotBeNegative(){
        controller.newUser("aaa", 100000, repo);
        String reply = controller.tambahAktivitas("aaa","hiburan", -200, "nonton endgame",repo,repoE);
        assertEquals(reply, "Maaf jumlah pengeluaran tidak boleh kurang dari 0");
    }

    @Test
    public void testKategoriInfo(){
        String reply = controller.kategori();
        assertEquals(reply, "Pilih satu kategori: \n - Makanan \n - Hiburan \n - Lainnya  \n Seterusnya ketik *kategori yang dipilih* *total pengeluaran* *deskripsi*");
    }

    public void testSetTarget() {
        String reply = controller.setTarget(userIdTemp, 1000, repo);
        assertEquals(reply, "Anda telah menentukan target pengeluaran sebesar 1000");
    }
    @Test
    public void testInfo(){
        String reply = controller.info();
        assertEquals(reply, "Berikut fitur-fitur yang terdapat pada WoyHemat! :\n" +
                "- info\n" +
                "- target\n" +
                "- tambah pengeluaran\n" +
                "- laporan\n" +
                "- daftar\n" +
                "- histori pengeluaran\n" + 
                "- utang");
    }



}
