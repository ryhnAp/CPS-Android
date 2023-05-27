package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Ball {
    private float centerX;
    private float centerY;

    private float ground = (float) 9.81;

    private float vxNew, vyNew;

    private boolean firstFallen = false;

    private final int radius = 30;
    public Ball() {
        centerX = 540;
        centerY = 5;
    }

    float getCenterX() {
        return centerX;
    }

    float getCenterY() {
        return centerY;
    }

    int getRadius() {
        return radius;
    }

    public void percussion(Velocity velocity, float angle, float v){
        float yy = (float) Math.sin(v);
        float xx = (float) Math.cos(v);
//
//        if (!firstFallen) {
//            firstFallen = true;
//            velocity.setY(velocity.getY()*-1*ground);
//        }
        double sinThetaOverTwo = Math.sin(2*angle);
        double cosThetaOverTwo = Math.cos(2*angle);
        Log.d(TAG, "#1 sinThetaOverTwo : " + sinThetaOverTwo);
        Log.d(TAG, "#1 cosThetaOverTwo : " + cosThetaOverTwo);
        double x_ = sinThetaOverTwo * yy;
        x_ += cosThetaOverTwo * xx;
        double y_ = -1*sinThetaOverTwo * xx;
        y_ -= cosThetaOverTwo * yy;
        Log.d(TAG, "#1 x_ : " + x_);
        Log.d(TAG, "#1 y_ : " + y_);
        vxNew = -1*(float) x_*10;
        vyNew = -1*(float) y_*10;

        velocity.setX(vxNew);
        velocity.setY(vyNew);
        centerX += velocity.getX();
        centerY += velocity.getY();
        Log.d(TAG, "#1 yy : " + yy);
        Log.d(TAG, "#1 xx : " + xx);
        Log.d(TAG, "#1 vxnew : " + vxNew);
        Log.d(TAG, "#1 vynew : " + vyNew);

    }

    public void update(Velocity velocity, int w, int h) {
        centerX += velocity.getX();
        centerY += velocity.getY();
//
//        if (!firstFallen){
//            centerX += velocity.getX();
//            centerY += velocity.getY()*ground;
//            velocity.setY(centerY);
//        }

        if ((centerX + radius > w) || (centerX - radius < 0)) {
            velocity.setX(velocity.getX()*-1);
//            velocity.setX(vxNew);
        }
        if ((centerY + radius > h) || (centerY - radius < 0)) {
            velocity.setY(velocity.getY()*-1);
//            velocity.setY(vyNew);
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }
}
