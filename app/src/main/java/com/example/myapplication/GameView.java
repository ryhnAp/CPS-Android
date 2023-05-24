package com.example.myapplication;

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


    //Paddle Part:
    float rx, ry, rz, rzx, rzy;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long timestamp=0;
    private static final double EPSILON = 0.1f;
    private double gyroscopeRotationVelocity = 0;

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
        paddle.update(dist, rx, ry, rz, rzx, rzy, delta_t, dir);

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

        canvas.drawText(String.valueOf(ax), 100, 100, paint);
        canvas.drawText(String.valueOf(dist), 100, 300, paint);
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

            // Isolate the force of gravity with the low-pass filter.
//            gx = alpha * gx + (1 - alpha) * event.values[0];
//            gy = alpha * gy + (1 - alpha) * event.values[1];
//            gz = alpha * gz + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
//            lastax = ax;
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

//            float xChange = ax - event.values[0];
//            ax = (event.values[0] - gx);
//            ax2 = (float) (ax*delta_t/2);
//            ay = event.values[1] - gy;
//            az = event.values[2] - gz;
//            ax*=1f;
//            dist = ax*delta_t/2;

//            ax = event.values[0];

            dist = (float) (0.5*ax*Math.pow(delta_t, 2)+velox*delta_t);
            last_x = dist;
            velox = ax*delta_t;
            lastax = ax;
//
//
//
//            if (lastax*ax > 0){
//                dir = 1;
//            } if(lastax*ax < 0) dir = 0;
//            if(lastax*ax == 0) dir = 0;


//            velocity = 2;

//            if(ax*lastax < 0)
//                ax = 0;

//            ax = (event.values[0] / 9.8f) * dWidth / 2 + dWidth / 2;


        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            rx = event.values[0];
//            ry = event.values[1];
//            rz = event.values[2];

            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                gyroscopeRotationVelocity = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
                if (gyroscopeRotationVelocity > EPSILON) {
                    axisX /= gyroscopeRotationVelocity;
                    axisY /= gyroscopeRotationVelocity;
                    axisZ /= gyroscopeRotationVelocity;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                double thetaOverTwo = gyroscopeRotationVelocity * dT / 2.0f;
                double sinThetaOverTwo = Math.sin(thetaOverTwo);
                double cosThetaOverTwo = Math.cos(thetaOverTwo);
                rx = ((float) (sinThetaOverTwo * axisX));
                ry = ((float) (sinThetaOverTwo * axisY));
                rz = ((float) (sinThetaOverTwo * axisZ));
                rzx = ((float) (cosThetaOverTwo * axisZ));
                rzy = ((float) (sinThetaOverTwo * axisZ));
//                deltaQuaternion.setW(-(float) cosThetaOverTwo);
            }
            timestamp = event.timestamp;

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}
