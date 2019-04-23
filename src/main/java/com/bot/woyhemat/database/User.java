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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public void setTotalPengeluaran(int totalPengeluaran) {
        this.totalPengeluaran = totalPengeluaran;
    }
}
