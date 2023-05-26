package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

public class Paddle {
    private float xl;
    private float xr;

    private float yu;

    private float yb;

    private float  delta_x, delta_y, delta_z;
    private boolean rightfix, leftfix;

    private float degrees, pivotX, pivotY;

    private float speedX;
    private final int screenWidth;
    private final int screenHeight;

    private int sideX, sideY;

    private Paint paint;
    private Path path;

//    private final Rect r;

    public Paddle() {
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        xl = screenWidth / 3 ;
        xr = screenWidth * 2 / 3;

        yu = 13 * screenHeight / 16;
        yb = 13 * screenHeight / 16 +20;

        sideX = screenWidth / 3;
        sideY = 20;

        paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setStyle(Paint.Style.FILL);

        path = new Path();

        delta_x = 0;
        delta_y = 0;
        delta_z = 0;
        rightfix = false;
        leftfix = false;
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

    public void update(float ax, float rx, float ry, float rz, float deltax, float deltay, double delta_t, int dir) {
        delta_x = sideX - deltax*sideX;
        delta_y = deltay * sideX;

        if (rx > 0) {
            rightfix = true;//fix
            leftfix = false;
        }else {
            rightfix = false;
            leftfix = true;//fix
        }

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


    }

    public void draw(Canvas canvas) {

        // Clear the path
        path.reset();

        // Define the coordinates of the rectangle

        // Define the skew factor
        float skewFactor = 0.1f;

        // true means add whit 0 // true means fix
        float yladded = leftfix ? 0 : delta_y;
        float yradded = rightfix? 0 : delta_y;
        float xladded = leftfix ? 0 : delta_x;
        float xradded = rightfix? 0 : delta_x;

        // Move to the top left corner of the rectangle
        path.moveTo(xl+xladded, yu+yladded);//+ -> min, - -> add, if xl fix -> 0

        // Draw the top line of the rectangle
        path.lineTo(xr+xradded, yu+yradded);//+ -> min, - -> add, if xr fix -> 0

        // Skew the canvas
        canvas.skew(skewFactor, 0);

        // Draw the right line of the rectangle
        path.lineTo(xr+xradded, yb+yradded);

        // Restore the canvas skew
        canvas.skew(-skewFactor, 0);

        // Draw the bottom line of the rectangle
        path.lineTo(xl+xladded,  yb+yladded);

        // Skew the canvas again
        canvas.skew(skewFactor, 0);

        // Draw the left line of the rectangle
        path.lineTo(xl+xladded, yu+yladded);

        // Restore the canvas skew
        canvas.skew(-skewFactor, 0);

        // Close the path
        path.close();

        // Create a matrix and set it to rotate
//        Matrix matrix = new Matrix();
//        matrix.postRotate(45, xl, yu);
//        matrix.postRotate(degrees);
//
//// Get the pivot point
//        float pivotX = path.getBounds().exactCenterX();
//        float pivotY = path.getBounds().exactCenterY();
// Apply the rotation to your path
//        path.transform(matrix);
        
        // Draw the path on the canvas
        canvas.drawPath(path, paint);

    }

    public void setSpeed(float v) {
        speedX = v;
    }
}
