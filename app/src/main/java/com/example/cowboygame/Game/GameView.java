package com.example.cowboygame.Game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import com.example.cowboygame.GameOver;
import com.example.cowboygame.Controller.LaserThread;
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.Models.Timer;
import com.example.cowboygame.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SensorEventListener {
    private Player player;
    private int count=0;
    //Background variables
    Bitmap lastBackground= BitmapFactory.decodeResource(getResources(), R.drawable.background1);
    private boolean changeBack=true;
    //Laser variables
    private LaserThread laserThread1,laserThread2,laserThread3;
    public float xfinalLaser1, yfinalLaser1, xLaser1, yLaser1, xInitialLaser1, yInitialLaser1;
    public float xfinalLaser2, yfinalLaser2, xLaser2, yLaser2, xInitialLaser2, yInitialLaser2;
    public float xfinalLaser3, yfinalLaser3, xLaser3, yLaser3, xInitialLaser3, yInitialLaser3;
    public Bitmap bpmLaser;
    //Score
    Paint paint= new Paint();
    private int score=0;
    //Timer variables
    Timer timer;
    //Images and sizes
    private Bitmap bmpBlood, bpmHero;
    public float xHero=40, yHero=40,width,height,heroHeigth=0,heroWidth=0;
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
        timer= new Timer();
        timer.startStop();
        //Height and width assignation
        this.width=width;
        this.height=height-30;
        //Accelerometer initialization
        SensorManager sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        //Game Thread
        gameLoopThread = new GameLoopThread(this);
        laserThread1 =new LaserThread(this,1);
        laserThread2 =new LaserThread(this,2);
        laserThread3 =new LaserThread(this,3);

        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                laserThread1.setRunning(false);
                laserThread2.setRunning(false);
                laserThread3.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        laserThread2.join();
                        laserThread1.join();
                        laserThread3.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //Sprites creation
                createSprites();
                //Time getted
                timeCreationInMilliseconds=timer.getTimeleftinMilliseconds();
                //Thread started
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                laserThread1.setRunning(true);
                laserThread1.start();
                laserThread2.setRunning(true);
                laserThread2.start();
                laserThread3.setRunning(true);
                laserThread3.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
        //Hero and blood initialization
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood);
        bpmHero = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bpmLaser = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
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

    //Background change
    private void changeBackground(Canvas canvas){
        Bitmap bpmBackground= lastBackground;
        Random random = new Random();
        int randomId = random.nextInt(4) + 1;

        if(changeBack==true) {
            switch (randomId) {
                case 1:
                    bpmBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
                    break;
                case 2:
                    bpmBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background2);
                    break;
                case 3:
                    bpmBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
                    break;
                case 4:
                    bpmBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background4);
                    break;
            }
        }
        lastBackground=bpmBackground;
        canvas.drawBitmap(bpmBackground,0,0,null);
        changeBack=false;
    }

    //Draw method
    @Override
    protected void onDraw(Canvas canvas) {
        //Background
        canvas.drawColor(Color.BLACK);
        changeBackground(canvas);

        //Sprites drawing and movement
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        isSpriteDead();

        try{
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
            //Timer, it is actualiced constantly
            canvas.drawText(timer.getTimeLeft(),50,50,paint);
            canvas.drawText(String.valueOf(score),width-50,50,paint);

            //If the hero is touched by a bandit the game is over
            if ((isHeroDead())||(isTimeOut())){
                //The screen is cleaned and the thread, activity and timer are stopped
                sprites.clear();
                timer.startStop();
                gameLoopThread.interrupt();
                ((Activity)getContext()).finish();

                //The new activity is launched
                Intent intent= new Intent(getContext(), GameOver.class);
                intent.putExtra("timer",timer.getTimeleftinMilliseconds());
                intent.putExtra("score",score);
                intent.putExtra("player",player);
                getContext().startActivity(intent);
            }
        }
        }catch (ConcurrentModificationException e){
        }
        //Hero, his position is actualized constantly
        canvas.drawBitmap(bpmHero, xHero, yHero,null);
        heroHeigth=bpmHero.getScaledHeight(canvas)-8;
        heroWidth=bpmHero.getScaledWidth(canvas)-8;

        //Laser
        canvas.drawBitmap(bpmLaser, xLaser1, yLaser1, null);
        canvas.drawBitmap(bpmLaser, xLaser2, yLaser2, null);
        canvas.drawBitmap(bpmLaser, xLaser3, yLaser3, null);
    }

    private boolean isHeroDead(){
        boolean isDead= false;
        //When the game is created the first time the hero will be inmortal for a few moments, also when the sprites are refilled
        if (timeCreationInMilliseconds-timer.getTimeleftinMilliseconds()>2000) {
            synchronized (getHolder()) {
                //The sprites are get
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    //If they are in the same position as the hero he is considered dead
                    if (sprite.isCollision(xHero, yHero) || sprite.isCollision(xHero + heroWidth, yHero + heroHeigth) || sprite.isCollision(xHero + heroWidth, yHero) || sprite.isCollision(xHero, yHero + heroHeigth)) {
                        isDead = true;
                    }
                }
            }
        }
        return isDead;
    }

    private boolean isTimeOut(){
        if (timer.getTimeleftinMilliseconds()<=1000){
            return true;
        }
        else{
            return false;
        }
    }

    private  void isSpritesEmpty(){
        if(sprites.isEmpty()){
            createSprites();
            timeCreationInMilliseconds= timer.getTimeleftinMilliseconds();
            changeBack=true;
        }
    }

    //Enemy death animation
    public void isSpriteDead(){
        synchronized (getHolder()) {
            for (int i = sprites.size() - 1; i >= 0; i--) {
                Sprite sprite = sprites.get(i);
                //If we touched a sprite it will be removed
                if (sprite.isCollision(xLaser1, yLaser1)) {
                    sprites.remove(sprite);
                    temps.add(new TempSprite(temps, this, xLaser1, yLaser1, bmpBlood));
                    score++;
                    //The laser is made disappear
                    xLaser1=xInitialLaser1=-1;
                    yLaser1= yInitialLaser1=-1;
                    xfinalLaser1=-10;
                    yfinalLaser1=10;
                    //When all the enemies are killed more are generated
                    isSpritesEmpty();
                    break;
                }
                if (sprite.isCollision(xLaser2, yLaser2)) {
                    sprites.remove(sprite);
                    temps.add(new TempSprite(temps, this, xLaser2, yLaser2, bmpBlood));
                    score++;
                    //The laser is made disappear
                    xLaser2=xInitialLaser2=-1;
                    yLaser2=yInitialLaser2=-1;
                    xfinalLaser2=-10;
                    yfinalLaser2=10;
                    //When all the enemies are killed more are generated
                    isSpritesEmpty();
                    break;
                }
                if (sprite.isCollision(xLaser3, yLaser3)) {
                    sprites.remove(sprite);
                    temps.add(new TempSprite(temps, this, xLaser3, yLaser3, bmpBlood));
                    score++;
                    //The laser is made disappear
                    xLaser3=xInitialLaser3=0;
                    yLaser3=yInitialLaser3=0;
                    xfinalLaser3=-10;
                    yfinalLaser3=10;
                    //When all the enemies are killed more are generated
                    isSpritesEmpty();
                    break;
                }
            }
        }
    }

    //Touch event method (fires the laser)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            switch (count){
                case 0:
                    xInitialLaser1 =xHero;
                    yInitialLaser1 =yHero;
                    yfinalLaser1 =y;
                    xfinalLaser1 =x;
                    xLaser1 =xHero+heroWidth/2;
                    yLaser1 =yHero+heroHeigth/2;
                    break;
                case 1:
                    xInitialLaser2 =xHero;
                    yInitialLaser2 =yHero;
                    yfinalLaser2 =y;
                    xfinalLaser2 =x;
                    xLaser2 =xHero+heroWidth/2;
                    yLaser2=yHero+heroHeigth/2;
                    break;
                case 2:
                    xInitialLaser3 =xHero;
                    yInitialLaser3 =yHero;
                    yfinalLaser3 =y;
                    xfinalLaser3 =x;
                    xLaser3 =xHero+heroWidth/2;
                    yLaser3 =yHero+heroHeigth/2;
                    break;
            }
            count++;
            if(count==3) count=0;
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
                xHero = xHero - event.values[0];
                yHero = yHero + event.values[1];
                //When it arrives to the horizontal border it stops
                if(xHero <=0|| xHero + bpmHero.getWidth()>=width){
                    xHero = xHero + event.values[0];
                }
                //Same happens in vertical border
                if(yHero <=0 || yHero + bpmHero.getWidth()>=height){
                    yHero = yHero -event.values[1];
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}