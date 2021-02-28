package com.example.cowboygame.Models;

import android.os.CountDownTimer;

public class Timer {
    public  static final long INITIALTIME=90000;
    private CountDownTimer countDownTimer;
    private long timeleftinMilliseconds=90000;
    private boolean timeFlowing;
    private String timeLeft;

    public void Timer(){
        timeleftinMilliseconds= INITIALTIME;
        timeFlowing=false;
    }

    public void startStop(){
        if (timeFlowing){
            stopTimer();
        }else{
            startTimer();
        }
    }
    public void startTimer(){
        countDownTimer = new CountDownTimer(timeleftinMilliseconds,1000){
            @Override
            public void onTick(long l){
                timeleftinMilliseconds= l;
                updateTimer();
            }
            @Override
            public void onFinish(){}
        }.start();
        timeFlowing=true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        timeFlowing= false;
    }

    public void updateTimer(){
        int minutes= (int) timeleftinMilliseconds/60000;
        int seconds= (int) (timeleftinMilliseconds % 60000)/1000;

        if (seconds<10) {
            timeLeft="" + minutes + ":" +"0" +seconds;
        }else{
            timeLeft="" + minutes + ":" +seconds;
        }
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public long getTimeleftinMilliseconds() {
        return timeleftinMilliseconds;
    }

    public boolean isTimeFlowing() {
        return timeFlowing;
    }

    public String getTimeLeft() {
        return timeLeft;
    }
}
