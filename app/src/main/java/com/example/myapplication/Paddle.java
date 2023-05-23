package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {
    private float xl;
    private float xr;

    private float yu;

    private float yb;

    private float speedX;
    private final int screenWidth;
    private final int screenHeight;

    public Paddle() {
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        xl = screenWidth / 3;
        xr = screenWidth * 2 / 3;

        yu = 13 * screenHeight / 16;
        yb = 13 * screenHeight / 16 + 20;
    }

    public float getLeft() {
        return xl;
    }
    public float getTop() {
        return yu;
    }
    public float getRight() {
        return xr;
    }
    public float getBottom() {
        return yb;
    }

    private int calDist(float transport, float coefficient){
        float cent_move = transport;
        float ratio = (float) (cent_move/0.5);
        int pixel_move = (int) (ratio * coefficient);
        return pixel_move;
    }

    public void update(float ax, float rx, float ry, float rz, double delta_t, int dir) {
        float cent_move = ax;
        float ratio = (float) (cent_move/0.5);
        int pixel_move = (int) (ratio * (float)screenWidth);

        float xln  =  xl + rx;
        float xrn  =  xr + rx;
        float yun  =  yu + ry;
        float ybn  =  yb + ry;

        xl += rx;
        xr += rx;
        yu += ry;
        yb += ry;
//        float zn  =  z + rz;
//        float zn  =  z + rz;

        if (xln<0) {
            int dist = calDist(rx, screenWidth);
            xl -= rx;
            xr -= rx;
        }
        if (ybn<0) {
            int dist = calDist(ry, screenHeight);
            yb -= ry;
            yu -= ry;
        }
        if (xrn>screenWidth) {
            int dist = calDist(rx, screenWidth);
            xr -= rx;
            xl -= rx;
        }
        if (yun>screenHeight) {
            int dist = calDist(ry, screenHeight);
            yu -= ry;
            yb -= ry;
        }

//        float pixel_move = ax;
//
//        if((xl >= screenWidth && pixel_move > 0) || (xr <= 0 && pixel_move < 0))
//            return;
//
//        xl += pixel_move;
//        xr += pixel_move;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));
        canvas.drawRect((int)xl, (int) yu, (int) xr, (int) yb, paint);
    }

    public void setSpeed(float v) {
        speedX = v;
    }
}
