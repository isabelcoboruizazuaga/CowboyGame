package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.cowboygame.Controller.AdapterGames;
import com.example.cowboygame.Controller.BDGame;
import com.example.cowboygame.Models.Game;
import com.example.cowboygame.Models.Player;

import java.util.ArrayList;

public class PlayerGames extends AppCompatActivity {
    private ArrayList<Game> games= new ArrayList<Game>();
    private RecyclerView recView;
    private  Player player;
    private BDGame bdGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_games);

        //Player Object
        Intent intent=getIntent();
         player = (Player) intent.getSerializableExtra("player");

        //Database initialization
        bdGame = new BDGame(this);

        //RecyclerView initialization
        recView = findViewById(R.id.rv_playerGames);

        //Assignment of the Layout to the Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        //int idGame, int score,long hour, long time, String email

        fillRecycler();
    }

    public void fillRecycler(){
        games= bdGame.obtainAllGames(player.getEmail());

        //Assignment of the Recycler View adapter with the games list
        AdapterGames adapter = new AdapterGames(games);
        recView.setAdapter(adapter);
    }
}