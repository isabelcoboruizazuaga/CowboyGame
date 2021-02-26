package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cowboygame.Controller.AdapterPlayers;
import com.example.cowboygame.Controller.BDController;
import com.example.cowboygame.Controller.BDPlayer;
import com.example.cowboygame.Models.Player;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerSelection extends AppCompatActivity {

    ArrayList<Player> players;
    RecyclerView recView;
    FloatingActionButton fab_newPlayer;

    BDController database;
    BDPlayer bdPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);


        fab_newPlayer= findViewById(R.id.fab_newPlayer);
        fab_newPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), NewPlayer.class));
            }
        });

        //Database initialization
        database= new BDController(this);
        bdPlayer=new BDPlayer(this);

        //RecyclerView initialization
        recView = findViewById(R.id.rv_player);

        //Assignment of the Layout to the Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        fillRecycler();
        /*
        Player p1= new Player("manolo","p1@gmail.com","12345");
        players.add(p1);
         p1= new Player("sdsasd","p1@gmail.com","12345");
        players.add(p1);
         p1= new Player("ma1111nolo","p1@gmail.com","12345");
        players.add(p1);
         p1= new Player("manrrrrolo","p1@gmail.com","sds21");
        players.add(p1);
         p1= new Player("man2222olo","p1@gmail.asas","213213");
        players.add(p1);
         p1= new Player("22222","p1@sddsa.com","123123");
        players.add(p1);
         p1= new Player("3333","p1@sad.com","12345");
        players.add(p1);*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecycler();
    }

    public void fillRecycler(){
        players= bdPlayer.obtainPlayers();
        //Assignment of the Recycler View adapter with the user list
        AdapterPlayers adapter = new AdapterPlayers(players);
        recView.setAdapter(adapter);
    }
}