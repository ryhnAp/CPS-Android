package com.example.ball;

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
    Velocity velocity = new Velocity(25, 32);
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
    float rx, ry, rz, cos = 1, sin = 0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long timestamp = 0;
    private static final double EPSILON = 1.0f;
    private double gyroscopeRotationVelocity = 0;

    private Sensor gyroscopeSensor;

    //end Paddle Part

    float ax2;
    String text;

    int dir;

    private SensorManager sensorManager;
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
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

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
        paddle.update(rx, cos, sin);

        if((ball.getCenterX() - ball.getRadius() >= paddle.getLeft()-50 && ball.getCenterX() + ball.getRadius() <= paddle.getRight()+50)
                && (Math.abs(ball.getCenterY() - paddle.getTop()) <= ball.getRadius() ||
                Math.abs(ball.getCenterY() - paddle.getBottom()) <= ball.getRadius()) && !lastRebound) {
            velocity.setY(velocity.getY()*-1);
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
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                rx = event.values[0];
                ry = event.values[1];
                rz = event.values[2];

                gyroX = (float) (rx*dT * (180 / Math.PI));
                gyroY = (float) (ry*dT * (180 / Math.PI));
                gyroZ = (float) (rz*dT * (180 / Math.PI));
                Log.d(TAG, "Gyroscope X: " + gyroX + " degrees/s");
                Log.d(TAG, "Gyroscope Y: " + gyroY + " degrees/s");
                Log.d(TAG, "Gyroscope Z: " + gyroZ + " degrees/s");
                if (gyroZ > EPSILON) {
                    double sinThetaOverTwo = Math.sin(gyroZ);
                    sin = (float) sinThetaOverTwo;
                    double cosThetaOverTwo = Math.cos(gyroZ);
                    Log.d(TAG, "cosThetaOverTwo " + cosThetaOverTwo);
                    cos = (float) cosThetaOverTwo;
                }
            }
            timestamp = event.timestamp;
//            if (timestamp != 0) {
//                final float dT = (event.timestamp - timestamp) * NS2S;
//                // Axis of the rotation sample, not normalized yet.
//                float axisX = event.values[0];
//                float axisY = event.values[1];
//                float axisZ = event.values[2];
//
//                // Calculate the angular speed of the sample
//                gyroscopeRotationVelocity = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
//
//                // Normalize the rotation vector if it's big enough to get the axis
//                if (gyroscopeRotationVelocity > EPSILON) {
//                    axisX /= gyroscopeRotationVelocity;
//                    axisY /= gyroscopeRotationVelocity;
//                    axisZ /= gyroscopeRotationVelocity;
//                }
//
//                // Integrate around this axis with the angular speed by the timestep
//                // in order to get a delta rotation from this sample over the timestep
//                // We will convert this axis-angle representation of the delta rotation
//                // into a quaternion before turning it into the rotation matrix.
//                double thetaOverTwo = gyroscopeRotationVelocity * dT / 2.0f;
//                double sinThetaOverTwo = Math.sin(thetaOverTwo);
//                double cosThetaOverTwo = Math.cos(thetaOverTwo);
//                rx = ((float) (sinThetaOverTwo * axisX));
//                ry = ((float) (sinThetaOverTwo * axisY));
//                rz = ((float) (sinThetaOverTwo * axisZ));
//                cos = ((float) (cosThetaOverTwo * axisZ));
//                sin = ((float) (sinThetaOverTwo * axisZ));
////                deltaQuaternion.setW(-(float) cosThetaOverTwo);
//            }
//            timestamp = event.timestamp;
//
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}
