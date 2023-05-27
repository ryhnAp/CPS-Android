package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Ball {
    private int centerX;
    private int centerY;

    private int vxNew, vyNew;

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

    public void percussion(float angle, float xx, float yy){
        double sinThetaOverTwo = Math.sin(2*angle);
        double cosThetaOverTwo = Math.cos(2*angle);
        double x_ = sinThetaOverTwo * yy;
        x_ += cosThetaOverTwo * xx;
        double y_ = -1*sinThetaOverTwo * xx;
        y_ -= cosThetaOverTwo * yy;
        vxNew = (int) x_;
        vyNew = (int) y_;
    }

    public void update(Velocity velocity, int w, int h, float angle, float v) {
        float yy = (float) Math.sin(v);
        float xx = (float) Math.cos(v);
        centerX += velocity.getX();
        centerY += velocity.getY();
        percussion(angle, xx, yy);
            velocity.setX(vxNew);
            velocity.setY(vyNew);

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
