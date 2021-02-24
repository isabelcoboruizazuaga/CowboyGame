package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cowboygame.R;

public class GameOver extends AppCompatActivity {
    TextView tv1,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Toast.makeText(this,"aaaaaaa",Toast.LENGTH_LONG);

        tv1=findViewById(R.id.textView);
        tv2=findViewById(R.id.textView2);

        Intent intent=getIntent();
        float timeBegin = intent.getFloatExtra("timeBegin",0);
        float timeEnd = intent.getFloatExtra("timeEnd",0);



        tv1.setText(String.valueOf(timeBegin));
        tv2.setText(String.valueOf(timeEnd));

        System.out.println("Took : " + ((timeEnd - timeBegin) / 1000));

        Toast.makeText(this,String.valueOf(System.currentTimeMillis()), Toast.LENGTH_LONG).show();
        System.out.println(System.currentTimeMillis());



    }
}