package com.example.ball;

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

    private boolean checkSize(){
        return Math.abs(x2-x1)* Math.abs(x2-x1) + Math.abs(y2-y1)* Math.abs(y2-y1) > sideX*sideX;
    }

    public void update(float rx, float deltax, float deltay) {
        delta_x = sideX - deltax*sideX;
        delta_y = deltay * sideX;

        if(checkSize())
            return;

        if (rx > 0) {
            rightfix = true;
            leftfix = false;
        } else if (rx == 0) {
            rightfix = true;
            leftfix = true;
        } else {
            rightfix = false;
            leftfix = true;
        }

        float yladded = leftfix ? 0 : delta_y;
        float yradded = rightfix? 0 : delta_y;
        float xladded = leftfix ? 0 : delta_x;
        float xradded = rightfix? 0 : delta_x;
        Log.d(TAG, "rx: " + rx);
        if(rightfix) {
            x1 = x1 + xladded;
            y1 = y1 + yladded;
        }
        else{
            x1 = sideX;
            y1 = 13 * screenHeight / 16f;
        }

        if(leftfix) {
            x2 = x2 + xradded;
            y2 = y2 + yradded;
        }
        else{
            x2 = 2*sideX;
            y2 = 13 * screenHeight / 16f;
        }

        Log.d(TAG, "hi hi 1: " + yladded);
        Log.d(TAG, "hi hi 2: " + yradded);
        Log.d(TAG, "hi hi 3: " + xladded);
        Log.d(TAG, "hi hi 4: " + xradded);

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
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public void setSpeed(float v) {
        speedX = v;
    }
}
