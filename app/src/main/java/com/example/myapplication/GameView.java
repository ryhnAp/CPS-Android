package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class GameView extends View implements SensorEventListener {
    Velocity velocity = new Velocity(0, 25);
    float accelerationVelocity;
    int dWidth, dHeight;
    Ball ball;
    Paddle paddle;
    boolean lastRebound = false;
    float delta_t = (float) 0.016;
    float paddle_v;
    float lastax;
    float velox, dist, last_x;

    float ax, ay, az;
    float gx, gy, gz;


    float gyroX ;
    float gyroY ;
    float gyroZ;

    //Paddle Part:
    float rx, ry, rz, cos = 0, sin = 0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long timestamp = 0, acceleratorSpan = 0;
    private static final double EPSILON = 0.005f;
    private double gyroscopeRotationVelocity = 0;

    private Sensor gyroscopeSensor;

    //end Paddle Part

    float ax2;
    String text;

    int dir;

    private SensorManager sensorManager;
    private SensorManager sensorManager2;

    private Sensor accelerometerSensor;

    TextView textView;

    Paint paint;

    public GameView(Context context) {
        super(context);
        ball = new Ball();
        paddle = new Paddle();
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager2 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        paint = new Paint();
        paint.setColor(Color.WHITE); // Set color to black
        paint.setTextSize(60); // Set text size to 24
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        textView = findViewById(R.id.myTextView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        ball.update(velocity, dWidth, dHeight);
        paddle.update(gyroZ, cos, sin);

        if((ball.getCenterX() - ball.getRadius() >= paddle.getLeft()-50 && ball.getCenterX() + ball.getRadius() <= paddle.getRight()+50)
                && (Math.abs(ball.getCenterY() - paddle.getTop()) <= ball.getRadius() ||
                Math.abs(ball.getCenterY() - paddle.getBottom()) <= ball.getRadius()) && !lastRebound) {
//            velocity.setY(velocity.getY()*-1);
            ball.percussion(velocity, gyroZ, accelerationVelocity);
            lastRebound = true;
        }
        else {
            lastRebound = false;
        }
//        textView.setText(String.valueOf(ax));

        canvas.drawText(String.valueOf(gyroZ), 100, 100, paint);
        canvas.drawText(String.valueOf(gyroY), 100, 200, paint);
        canvas.drawText(String.valueOf(gyroX), 100, 300, paint);
//        canvas.drawText(text, 100, 500, paint);

        ball.draw(canvas);
        paddle.draw(canvas);

        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float alpha = 0.7f;
            float low_pass = 1.2f;
            float high_pass = 20f;
            ax = event.values[0];
            rx = event.values[0];
            ry = event.values[1];
            rz = event.values[2];

            ax *= 6f;

            float deltaT = (event.timestamp - acceleratorSpan) * NS2S;
            float acceleration = (float) Math.sqrt(rx*rx + ry*ry + rz*rz);
            accelerationVelocity = acceleration * deltaT * 10;

            if((event.values[0] < low_pass && event.values[0] > 0)
                    || (event.values[0] < 0 && event.values[0] > -low_pass)
                    || event.values[0] > high_pass
                    || event.values[0] < -high_pass
                    || lastax*ax < 0)
                ax = 0;
            dist = (float) (0.5*ax*Math.pow(delta_t, 2)+velox*delta_t);
            last_x = dist;
            velox = ax*delta_t;
            lastax = ax;
            acceleratorSpan = event.timestamp;
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                rx = event.values[0];
                ry = event.values[1];
                rz = event.values[2];

                gyroX = (float) (rx*dT);
                gyroY = (float) (ry*dT);
                gyroZ = (float) (rz*dT);
                if(gyroZ > 0)
                    Log.d(TAG, "positive : " + gyroZ);
                if(gyroZ < 0)
                    Log.d(TAG, "negative : " + gyroZ);
                if (Math.abs(gyroZ) > EPSILON) {
                    double sinThetaOverTwo = Math.sin(gyroZ);
                    sin = (float) sinThetaOverTwo;
                    double cosThetaOverTwo = Math.cos(gyroZ);
                    Log.d(TAG, "hi cos sin ");
                    Log.d(TAG, "cosThetaOverTwo " + cosThetaOverTwo);
                    Log.d(TAG, "sinThetaOverTwo " + sinThetaOverTwo);
                    cos = (float) cosThetaOverTwo;
                }
                else {
                    gyroZ = 0;
                    sin = 0;
                    cos = 0;
                }
            }
            timestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}