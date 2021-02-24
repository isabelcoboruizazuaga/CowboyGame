package com.example.cowboygame.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.cowboygame.Models.Player;

public class BDController extends SQLiteOpenHelper {

    private static Player player;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BD_Juego.db";

    protected static final String PLAYERS_TABLE = "Players";
    private static final String create_table_players = "CREATE TABLE " + PLAYERS_TABLE +" ( name VARCHAR(100), email VARCHAR(100) PRIMARY KEY, phone VARCHAR(20))";

    protected static final String GAMES_TABLE = "Games";
    private static final String foreign_key_game= "FOREIGN KEY (email) REFERENCES "+ PLAYERS_TABLE +"(email)";
    private static final String create_table_games = "CREATE TABLE "+ GAMES_TABLE + " (idGame INT PRIMARY KEY, score INT, hour LONG, time LONG" + foreign_key_game+ ")";


    public BDController(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_players);
        //db.execSQL(create_table_games);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }
}
