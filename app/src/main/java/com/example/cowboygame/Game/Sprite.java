package com.example.cowboygame.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cowboygame.Game.GameView;

import java.util.Random;
public class Sprite {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 2 back, 3 left, 0 front, 1 right
    int[] DIRECTION_TO_ANIMATION_MAP = { 2, 3, 0, 1 };
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 4;
    private static final int MAX_SPEED = 15;
    private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;
    public Sprite(GameView gameView, Bitmap bmp) {
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.gameView = gameView;
        this.bmp = bmp;
        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);
        xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
    }
    private void update() {
        if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }
    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }
    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public boolean isCollision(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
}