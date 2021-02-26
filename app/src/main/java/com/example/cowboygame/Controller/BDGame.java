package com.example.cowboygame.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.cowboygame.Models.Game;

import java.util.ArrayList;
import java.util.List;

public class BDGame extends BDController {

    public BDGame(@Nullable Context context) {
        super(context);
    }

    public long addGame(Game g) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;
        if (db != null) {
            ContentValues game = new ContentValues();

            game.put("idGame", g.getIdGame());
            game.put("email", g.getEmail());
            game.put("hour", g.getHour());
            game.put("score", g.getScore());
            game.put("time", g.getTime());

            id = db.insert(GAMES_TABLE, null, game);
        }
        db.close();
        return id;
    }

    public long deleteGame(String idGame) {
        long nLines = 0;

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            nLines = db.delete(PLAYERS_TABLE, "idGame=" + idGame, null);
        }
        db.close();
        return nLines;
    }

    public long deletePlayerGames(String email) {
        long nLines = 0;

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            nLines = db.delete(PLAYERS_TABLE, "email=" + email, null);
        }
        db.close();
        return nLines;
    }

    public long modifyGame(Game g) {
        long nLines = -1;

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues game = new ContentValues();

            game.put("idGame", g.getIdGame());
            game.put("email", g.getEmail());
            game.put("hour", g.getHour());
            game.put("score", g.getScore());
            game.put("time", g.getTime());
            nLines = db.update(GAMES_TABLE, game, "idGame=" + g.getIdGame(), null);
        }
        db.close();

        return nLines;
    }

    public Game searchGame(String idGame) {
        SQLiteDatabase db = getReadableDatabase();
        Game game = null;

        if (db != null) {
            String[] campos = {"idGame", "score", "hour", "time", "email"};
            Cursor c = db.query(GAMES_TABLE, campos, "idGame='" + idGame + "'", null, null, null, null);
            if (c.getCount() != 0) {
                c.moveToFirst();
                game = new Game(
                        c.getInt(1),
                        c.getLong(2),
                        c.getLong(3),
                        c.getString(4));
            } else {
                game = null;
            }
            c.close();
        }
        db.close();
        return game;

    }

    public ArrayList<Game> obtainAllGames(@Nullable String email) {
        SQLiteDatabase db = getReadableDatabase();
        Game game;
        List<Game> gameList = new ArrayList<>();
        if (db != null) {
            String[] campos = {"idGame", "score", "hour", "time", "email"};
            Cursor c;
            if (email!=null) {
                if(email.equals("-1")){
                    c = db.query(GAMES_TABLE, campos, null, null, null, null, "idGame"+" DESC");
                }else{
                    c = db.query(GAMES_TABLE, campos, "email='" + email + "'", null, null, null, "score"+" DESC,"+" time" +" ASC");
                }
            }else{
                 c = db.query(GAMES_TABLE, campos, null, null, null, null, "score"+" DESC"+", time" +" ASC");

            }
            if (c.moveToFirst()) {
                do {
                    game = new Game(
                            c.getInt(1),
                            c.getLong(2),
                            c.getLong(3),
                            c.getString(4));
                    gameList.add(game);
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return (ArrayList) gameList;
    }
}


