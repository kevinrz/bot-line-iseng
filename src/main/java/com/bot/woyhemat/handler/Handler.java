package com.bot.woyhemat.handler;

import java.util.List;
import java.util.Observer;

public abstract class Handler {
    List<Observer> observer;
    
    /**

     Belum ada FacadeNotifikasi untuk di notify.
     */
    abstract void notifyFacadeNotif();

    public void addObserver(Observer newObserver) {
        observer.add(newObserver);
    }
    /**
    * TODO method-method untuk getData dan setData ke database
     */
     public void setData(){} 
}