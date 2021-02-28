package com.example.cowboygame.Controller;

import com.example.cowboygame.Game.GameView;

public class LaserThread extends Thread {

    static final long FPS = 120;
    private GameView view;
    private boolean running = false;
    int laser;

    public LaserThread(GameView view, int laser) {
        this.view = view;
        this.laser=laser;
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
                switch (laser){
                    case 1:
                        if(view.xfinalLaser1 >view.xInitialLaser1 +5){
                            view.xLaser1++;
                        }else{
                            view.xLaser1--;
                        }
                        if(view.yfinalLaser1 >view.yInitialLaser1 +5){
                            view.yLaser1++;

                        }else{
                            view.yLaser1--;
                        }
                        break;
                    case 2:
                        if(view.xfinalLaser2 >view.xInitialLaser2 +5){
                        view.xLaser2++;
                    }else{
                        view.xLaser2--;
                    }
                        if(view.yfinalLaser2 >view.yInitialLaser2 +5){
                            view.yLaser2++;

                        }else{
                            view.yLaser2--;
                        }
                        break;
                    case 3:
                        if(view.xfinalLaser3 >view.xInitialLaser3 +5){
                            view.xLaser3++;
                        }else{
                            view.xLaser3--;
                        }
                        if(view.yfinalLaser3 >view.yInitialLaser3 +5){
                            view.yLaser3++;

                        }else{
                            view.yLaser3--;
                        }
                        break;
                }
                //view.xfinalLaser;


                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {
            }
        }
    }
}
