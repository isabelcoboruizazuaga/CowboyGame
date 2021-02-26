package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cowboygame.Controller.BDController;
import com.example.cowboygame.Controller.BDGame;
import com.example.cowboygame.Controller.BDPlayer;
import com.example.cowboygame.Models.Game;
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.R;

import java.util.ArrayList;

public class GameOver extends AppCompatActivity {
    TextView tv1,tv2;
    private BDGame bdGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        tv1=findViewById(R.id.textView);
        tv2=findViewById(R.id.textView2);

        //The game data are collected
        Intent intent=getIntent();
        long timer = intent.getLongExtra("timer",0);
        int score = intent.getIntExtra("score",0);
        Player player = (Player) intent.getSerializableExtra("player");

        //Database initialization
         bdGame = new BDGame(this);

        //The last id is recovered
        ArrayList<Game> games= bdGame.obtainAllGames("-1");
        int lastId;
        try{
            Game lastGame= games.get(games.size()-1);
            lastId= lastGame.getIdGame();
        }catch (IndexOutOfBoundsException e){
            lastId=0;
        }
        Game.setNextID(lastId+1);

        //New Game creation
        //Game gam=new Game(score,System.currentTimeMillis(),timer,player.getEmail());
        Game game= new Game(score,System.currentTimeMillis(),timer,player.getEmail());
        long nlines = bdGame.addGame(game);
            if(nlines<=0){
                Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
            }


        Toast.makeText(this,String.valueOf(System.currentTimeMillis()), Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, BestGames.class));
    }
}