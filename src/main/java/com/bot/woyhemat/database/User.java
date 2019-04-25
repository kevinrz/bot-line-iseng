package com.bot.woyhemat.database;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private int target;
    private int totalPengeluaran;

    protected User() {}

    public User(String username, int target, int totalPengeluaran) {
        this.username = username;
        this.target = target;
        this.totalPengeluaran = totalPengeluaran;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, username='%s', target='%d', totalPengeluaran=%d]",
                id, username, target, totalPengeluaran);
    }
    public void setTarget(int target){
        this.target = target;
    }

    public void tambahPengeluaranKeTotal(int amount){
        this.totalPengeluaran = this.totalPengeluaran + amount;
    }

    public String getUsername() {
        return username;
    }
}
