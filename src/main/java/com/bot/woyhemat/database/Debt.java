package com.bot.woyhemat.database;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int amount;
    private Date period;

    @ManyToOne
    private User user;

    protected Debt() {}

    public Debt(int amount, Date period, User user) {
        this.amount = amount;
        this.period = period;
        this.user = user;
    }
}
