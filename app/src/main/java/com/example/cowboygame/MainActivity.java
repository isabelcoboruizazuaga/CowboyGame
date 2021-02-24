package com.example.cowboygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity{
    //Music
    MediaPlayer mySong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Screen configuration
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Height and width assignation
        Display display = getWindowManager().getDefaultDisplay();
        float width = display.getWidth();
        float height = display.getHeight();

        //Game creation
        GameView gv= new GameView(this,width,height);
        setContentView(gv);

        mySong= MediaPlayer.create(MainActivity.this,R.raw.song);
        mySong.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySong.release();
        finish();
    }
}