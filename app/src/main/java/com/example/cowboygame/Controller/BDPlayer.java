package com.example.cowboygame.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.cowboygame.Models.Player;

import java.util.ArrayList;
import java.util.List;

public class BDPlayer extends BDController {

    public BDPlayer(@Nullable Context context) {
        super(context);
    }

    public long addPlayer(Player p){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;
        if(db != null){
            ContentValues player = new ContentValues();

            player.put("name", p.getName());
            player.put("email", p.getEmail());
            player.put("phone", p.getPhone());

            id = db.insert(PLAYERS_TABLE, null, player);
        }
        db.close();
        return id;
    }

    public long deletePlayer(String email){
        long nLines = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLines = db.delete(PLAYERS_TABLE,"email=" + email,null);
        }
        db.close();
        return nLines;
    }

    public long modifyPlayer(Player p){
        long nLines =-1;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            ContentValues player = new ContentValues();

            player.put("name", p.getName());
            player.put("email", p.getEmail());
            player.put("phone", p.getPhone());
            nLines = db.update(PLAYERS_TABLE,player, "email="+p.getEmail(),null);
        }
        db.close();

        return nLines;
    }

    public Player searchPlayer(String email){
        SQLiteDatabase db=getReadableDatabase();
        Player player=null;

        if(db!=null){
            //name VARCHAR(100), email VARCHAR(100) PRIMARY KEY, phone VARCHAR(20
            String[] campos = {"name","email","phone"};
            Cursor c = db.query(PLAYERS_TABLE,campos,"email='"+email+"'",null,null,null,null);          if(c.getCount()!=0){
                c.moveToFirst();
                player=new Player(c.getString(0),
                        c.getString(1),
                        c.getString(2));
            } else{
                player=null;
            }
            c.close();
        }
        db.close();
        return player;

    }

    public ArrayList<Player> obtainPlayers(){
        SQLiteDatabase db=getReadableDatabase();
        Player player;
        List<Player> playerList=new ArrayList<>();
        if(db!=null){
            String[] campos = {"name","email","phone"};
            Cursor c = db.query(PLAYERS_TABLE,campos,null,null,null,null,null);
            if(c.moveToFirst()){
                do{
                    player=new Player(c.getString(0),
                            c.getString(1),
                            c.getString(2));
                    playerList.add(player);
                }while(c.moveToNext());
            }
            c.close();
        }
        db.close();
        return (ArrayList) playerList;
    }

}
