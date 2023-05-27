package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

public class Paddle {
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    private float  delta_x, delta_y, delta_z;
    private boolean rightfix, leftfix;

    private float degrees, pivotX, pivotY;

    private float speedX;
    private final int screenWidth;
    private final int screenHeight;

    private int sideX, sideY;

    private Paint paint;


//    private final Rect r;

    public Paddle() {
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        x1 = screenWidth / 3f ;
        x2 = screenWidth * 2 / 3f;

        y1 = 13 * screenHeight / 16f;
        y2 = 13 * screenHeight / 16f;

        sideX = screenWidth / 3;
        sideY = 20;

        paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setStyle(Paint.Style.FILL);


        delta_x = 0;
        delta_y = 0;
        delta_z = 0;
        rightfix = false;
        leftfix = false;
    }

    public float getLeft() {
        return x1;
    }
    public float getTop() {
        return y1;
    }
    public float getRight() {
        return x2;
    }
    public float getBottom() {
        return y2;
    }

    private int calDist(float transport, float coefficient){
        float cent_move = transport;
        float ratio = (float) (cent_move/0.5);
        int pixel_move = (int) (ratio * coefficient);
        return pixel_move;
    }

    public void update(float  gyroZ, float deltax, float deltay) {
        delta_x = deltax*sideX/2;
        delta_y = deltay*sideX/2;

        float centerX, centerY;
        centerX = (x1+x2)/2;
        centerY = (y1+y2)/2;

//        if(checkSize())
//            return;

        if (gyroZ != 0) {
            int c = gyroZ<0 ? -1 : 1;
            x1 = centerX - delta_x;
            y1 = centerY - delta_y;
            x2 = centerX + delta_x;
            y2 = centerY + delta_y;
        }



        Log.d(TAG, "delta x : " + delta_x);
        Log.d(TAG, "delta y : " + delta_y);
        Log.d(TAG, "x1: " + x1);
        Log.d(TAG, "x2: " + x2);
        Log.d(TAG, "y1: " + y1);
        Log.d(TAG, "y2: " + y2);

//        xl += rx;
//        xr += rx;


//        if (xl<0) {
//            int dist = calDist(rx, screenWidth);
////            xl -= rx;
////            xr -= rx;
//        }
//        if (yb<0) {
//            int dist = calDist(ry, screenHeight);
////            yb -= ry;
////            yu -= ry;
//        }
//        if (xr>screenWidth) {
//            int dist = calDist(rx, screenWidth);
////            xr -= rx;
////            xl -= rx;
//        }
//        if (yu>screenHeight) {
//            int dist = calDist(ry, screenHeight);
////            yu -= ry;
////            yb -= ry;
//        }

//        float pixel_move = ax;
//
//        if((xl >= screenWidth && pixel_move > 0) || (xr <= 0 && pixel_move < 0))
//            return;
//
//        xl += pixel_move;
//        xr += pixel_move;
    }

    public void draw(Canvas canvas) {
        paint.setStrokeWidth(15f);
        paint.setColor(Color.rgb(255, 255, 255));
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public void setSpeed(float v) {
        speedX = v;
    }
}