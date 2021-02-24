package com.example.cowboygame.Models;

import java.io.Serializable;

public class Player implements Serializable {
    private  String name, email, phone;

    public Player(){}

    public Player(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
