package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Ball {
    private int centerX;
    private int centerY;
    private final int radius = 30;
    public Ball() {
        centerX = 800;
        centerY = 1400;
    }

    int getCenterX() {
        return centerX;
    }

    int getCenterY() {
        return centerY;
    }

    int getRadius() {
        return radius;
    }

    public void update(Velocity velocity, int w, int h) {
        centerX += velocity.getX();
        centerY += velocity.getY();
        if ((centerX + radius > w) || (centerX - radius < 0)) {
            velocity.setX(velocity.getX()*-1);
        }
        if ((centerY + radius > h) || (centerY - radius < 0)) {
            velocity.setY(velocity.getY()*-1);
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }
}
