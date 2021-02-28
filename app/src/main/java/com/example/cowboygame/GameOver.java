package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cowboygame.Controller.BDController;
import com.example.cowboygame.Controller.BDGame;
import com.example.cowboygame.Controller.BDPlayer;
import com.example.cowboygame.Models.Game;
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.Models.Timer;
import com.example.cowboygame.R;

import java.util.ArrayList;
import java.util.Random;

public class GameOver extends AppCompatActivity {
    private TextView tv_scoreGO,tv_timeGO;
    private BDGame bdGame;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Random random = new Random();

        //Layout initialization
        tv_scoreGO=findViewById(R.id.tv_scoreGO);
        tv_timeGO=findViewById(R.id.tv_timeGO);

        //The game data are collected
        Intent intent=getIntent();
        long timer = intent.getLongExtra("timer",0);
        int score = intent.getIntExtra("score",0);
         player = (Player) intent.getSerializableExtra("player");

        //The game time is calculated
        long timePlayed= Timer.INITIALTIME-timer;

        int minutes= (int) timePlayed/60000;
        int seconds= (int) (timePlayed % 60000)/1000;

        String gameTime="";

        if (seconds<10) {
            gameTime="" + minutes + ":" +"0" +seconds;
        }else{
            gameTime="" + minutes + ":" +seconds;
        }

        //The layout is filled
        tv_scoreGO.setText(String.valueOf(score));
        tv_timeGO.setText(String.valueOf(gameTime));

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
        Game.setNextID(lastId);

        //New Game creation

        Game game= new Game(score,System.currentTimeMillis(),timer,player.getEmail());
        try{
            long nlines = bdGame.addGame(game);
        }catch (Exception e){
            int randomId = random.nextInt(9999999) + 10000;
            game.setIdGame(randomId);

            long nlines = bdGame.addGame(game);
            if(nlines<=0){
                Toast.makeText(this,"Error al registrar la partida",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void retry(View view) {
        finish();
    }

    public void launchBestGames(View view) {
        startActivity(new Intent(this, BestGames.class));
    }
}