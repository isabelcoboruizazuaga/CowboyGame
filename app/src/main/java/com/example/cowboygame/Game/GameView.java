package com.example.cowboygame.Game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.cowboygame.GameOver;
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class GameView extends SurfaceView implements SensorEventListener {
    Player player;
    //Score
    Paint paint= new Paint();
    private int score=0;
    //Timer variables
    private CountDownTimer countDownTimer;
    private long timeleftinMilliseconds= 180000;
    private boolean timeFlowing;
    private String timeLeft;
    //Images and sizes
    private Bitmap bmpBlood, bpmHero;
    float x=40,y=40,width,height,heroHeigth=0,heroWidth=0;
    //Loop and sprites
    private GameLoopThread gameLoopThread;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private long lastClick;
    private long timeCreationInMilliseconds;

    public GameView(Context context,float width, float height, Player player) {
        super(context);
        createPaint();
        this.player=player;
        //Time initialization
        startStop();
        //Height and width assignation
        this.width=width;
        this.height=height-30;
        //Accelerometer initialization
        SensorManager sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        //Game Thread
        gameLoopThread = new GameLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //Sprites creation and sprites thread created
                createSprites();
                timeCreationInMilliseconds=timeleftinMilliseconds;
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
        //Hero and blood initialization
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood);
        bpmHero = BitmapFactory.decodeResource(getResources(), R.drawable.conrad);
    }

    //Method to refill the sprites array
    private void createSprites() {
        sprites.add(createSprite(R.drawable.bandit_council));
        sprites.add(createSprite(R.drawable.bandit_grunt));
        sprites.add(createSprite(R.drawable.bandit_council));
        sprites.add(createSprite(R.drawable.bandit_grunt));
        sprites.add(createSprite(R.drawable.bandit_council));
        sprites.add(createSprite(R.drawable.bandit_grunt));
        sprites.add(createSprite(R.drawable.bandit_council));
        sprites.add(createSprite(R.drawable.bandit_grunt));
    }

    //Method to create the sprites
    private Sprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this, bmp);
    }

    //Paints initialization
    private void createPaint(){
        //Timer paint
        paint.setColor(getResources().getColor(R.color.white));
        paint.setTextSize(50);
    }

    //Draw method
    @Override
    protected void onDraw(Canvas canvas) {
        //Background
        canvas.drawColor(Color.BLACK);
        //Sprites drawing and movement
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }

        try{
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
            //Timer, it is actualiced constantly
            canvas.drawText(timeLeft,50,50,paint);
            canvas.drawText(String.valueOf(score),width-50,50,paint);

            //If the hero is touched by a bandit the game is over
            if ((isHeroDead())||(isTimeOut())){
                //The screen is cleaned and the thread, activity and timer are stopped
                sprites.clear();
                startStop();
                gameLoopThread.interrupt();
                ((Activity)getContext()).finish();

                //The new activity is launched
                Intent intent= new Intent(getContext(), GameOver.class);
                intent.putExtra("timer",timeleftinMilliseconds);
                intent.putExtra("score",score);
                intent.putExtra("player",player);
                getContext().startActivity(intent);
            }
        }
        }catch (ConcurrentModificationException e){
        }
        //Hero, his position is actualized constantly
        canvas.drawBitmap(bpmHero,x,y,null);
        heroHeigth=bpmHero.getScaledHeight(canvas)-8;
        heroWidth=bpmHero.getScaledWidth(canvas)-8;
    }

    private boolean isHeroDead(){
        boolean isDead= false;
        //When the game is created the first time the hero will be inmortal for a few moments, also when the sprites are refilled
        if (timeCreationInMilliseconds-timeleftinMilliseconds>2000) {
            synchronized (getHolder()) {
                //The sprites are get
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    //If they are in the same position as the hero he is considered dead
                    if (sprite.isCollision(x, y) || sprite.isCollision(x + heroWidth, y + heroHeigth) || sprite.isCollision(x + heroWidth, y) || sprite.isCollision(x, y + heroHeigth)) {
                        isDead = true;
                    }
                }
            }
        }
        return isDead;
    }
    private boolean isTimeOut(){
        if (timeleftinMilliseconds<=1000){
            return true;
        }
        else{
            return false;
        }
    }


    //Touch event method (death animation)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    //If we touched a sprite it will be removed
                    if (sprite.isCollision(x, y)) {
                        sprites.remove(sprite);
                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        //When all the enemies are killed more are generated
                        if(sprites.isEmpty()){
                            score++;
                            createSprites();
                            timeCreationInMilliseconds= timeleftinMilliseconds;
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }

    /*
     *ACCELEROMETER METHODS
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            //Sensor values are assigned
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                x= x - event.values[0];
                y = y + event.values[1];
                //When it arrives to the horizontal border it stops
                if(x<=0|| x+ bpmHero.getWidth()>=width){
                    x= x + event.values[0];
                }
                //Same happens in vertical border
                if(y<=0 || y+ bpmHero.getWidth()>=height){
                    y= y -event.values[1];
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
    *TIMER METHODS
     */
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
}