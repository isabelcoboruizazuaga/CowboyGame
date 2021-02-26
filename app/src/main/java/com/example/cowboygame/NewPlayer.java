package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cowboygame.Controller.BDController;
import com.example.cowboygame.Controller.BDPlayer;
import com.example.cowboygame.Models.Player;

public class NewPlayer extends AppCompatActivity {
    EditText et_name, et_phone, et_email;
    BDController database;
    BDPlayer bdPlayer;
    long nlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        //Layout items initialization
        et_email=findViewById(R.id.et_email);
        et_name=findViewById(R.id.et_name);
        et_phone=findViewById(R.id.et_phone);


        //Database initialization
        database= new BDController(this);
        bdPlayer=new BDPlayer(this);
    }

    public void addPlayer(View view) {
        String email, name, phone;
        email= et_email.getText().toString();
        name =et_name.getText().toString();
        phone=et_phone.getText().toString();

        if(email.length()<5||name.length()<3||phone.length()<9){
            Toast.makeText(this,"Todos los campos deben estar rellenos",Toast.LENGTH_LONG).show();
        }else{
            Player player=new Player(name,email,phone);
            nlines=bdPlayer.addPlayer(player);
            if(nlines<=0){
                Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    public void cancel(View view) {
        finish();
    }
}