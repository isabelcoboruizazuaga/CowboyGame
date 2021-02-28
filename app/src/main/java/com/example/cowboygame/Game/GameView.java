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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.cowboygame.GameOver;
import com.example.cowboygame.Controller.LaserThread;
import com.example.cowboygame.Models.Player;
import com.example.cowboygame.Models.Timer;
import com.example.cowboygame.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class GameView extends SurfaceView implements SensorEventListener {
    public float xfinalLaser,yfinalLaser, xLaser,yLaser=40, xInicialLaser,yInicialLaser;
    private Player player;
    //Laser variables
    private LaserThread laserThread;
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
        laserThread=new LaserThread(this);

        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                laserThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        laserThread.join();
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
                laserThread.setRunning(true);
                laserThread.start();
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

    //Draw method
    @Override
    protected void onDraw(Canvas canvas) {
        //Background
        canvas.drawColor(Color.BLACK);
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
        canvas.drawBitmap(bpmLaser, xLaser, yLaser, null);
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
        //return isDead;
        return false;
    }
    private boolean isTimeOut(){
        if (timer.getTimeleftinMilliseconds()<=1000){
            return true;
        }
        else{
            return false;
        }
    }

   /* public void aaa(){
        xInicialLaser=x;
        yInicialLaser=y;
        for(float i= xInicialLaser;i<xfinalLaser;i++){
            xLaser=i;
            yLaser=((xLaser*(yfinalLaser-xfinalLaser))-(xInicialLaser*yfinalLaser)+(xfinalLaser*yfinalLaser))/(yInicialLaser+xInicialLaser);

            //System.out.println("X: "+ xLaser);
            System.out.println("Y: "+ yLaser);
        }
    }*/

    public void isSpriteDead(){
        synchronized (getHolder()) {
            for (int i = sprites.size() - 1; i >= 0; i--) {
                Sprite sprite = sprites.get(i);
                //If we touched a sprite it will be removed
                if (sprite.isCollision(xLaser, yLaser)) {
                    sprites.remove(sprite);
                    temps.add(new TempSprite(temps, this, xLaser, yLaser, bmpBlood));
                    //When all the enemies are killed more are generated
                    if(sprites.isEmpty()){
                        score++;
                        createSprites();
                        timeCreationInMilliseconds= timer.getTimeleftinMilliseconds();
                    }
                    break;
                }
            }
        }
    }

    //Touch event method (death animation)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            yfinalLaser=y;
            xfinalLaser=x;
            xInicialLaser=xHero;
            yInicialLaser=yHero;
            xLaser=xHero+heroWidth/2;
            yLaser=yHero+heroHeigth/2;
            /*synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    //If we touched a sprite it will be removed
                    if (sprite.isCollision(xLaser, yLaser)) {
                        sprites.remove(sprite);
                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        //When all the enemies are killed more are generated
                        if(sprites.isEmpty()){
                            score++;
                            createSprites();
                            timeCreationInMilliseconds= timer.getTimeleftinMilliseconds();
                        }
                        break;
                    }
                }
            }*/
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