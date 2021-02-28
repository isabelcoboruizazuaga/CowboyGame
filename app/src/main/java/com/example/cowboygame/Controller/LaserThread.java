package com.example.cowboygame.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cowboygame.Game.GameView;
import com.example.cowboygame.R;

import java.util.Random;

public class LaserThread extends Thread {

    static final long FPS = 120;
    private GameView view;
    private boolean running = false;
    private int troncoXDependencia;

    public LaserThread(GameView view) {
        this.view = view;
        this.troncoXDependencia = troncoXDependencia;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;

        while (running) {
            startTime = System.currentTimeMillis();
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {

                //view.xfinalLaser;
                if(view.xfinalLaser>view.xInicialLaser+5){
                    view.xLaser++;
                }else{
                    view.xLaser--;

                }
                if(view.yfinalLaser>view.yInicialLaser+5){
                    view.yLaser++;

                }else{
                    view.yLaser--;
                }

                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {
            }
        }
    }
}
