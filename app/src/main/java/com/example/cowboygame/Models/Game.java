package com.example.cowboygame.Models;

import java.io.Serializable;

public class Game implements Serializable {
    public static int nextID=1;
    private String email;
    private long hour, time;
    private int score,idGame;

    public Game(){}

    public Game(int score,long hour, long time, String email) {
        this.idGame=getNewId();
        this.email = email;
        this.hour = hour;
        this.time = time;
        this.score = score;
    }

    static int getNewId() {
        int newId = nextID;
        nextID++;
        return newId;
    }

    public String getEmail() {
        return email;
    }

    public long getHour() {
        return hour;
    }

    public long getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public int getIdGame() {
        return idGame;
    }

    public static void setNextID(int nextID) {
        Game.nextID = nextID;
    }
}
