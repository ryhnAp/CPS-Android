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

    private float  deltax, deltay, deltaz;
    private boolean rightfix, leftfix;

    private float degrees, pivotX, pivotY;

    private float speedX;
    private final int screenWidth;
    private final int screenHeight;

    private int side;

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

        side = screenWidth / 3;

        paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));

//        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        path = new Path();

//        r = new Rect((int)xl, (int) yu, (int) xr, (int) yb);
        deltax = 0;
        deltay = 0;
        deltaz = 0;
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

    public void update(float ax, float rx, float ry, float rz, float rzx, float rzy, double delta_t, int dir) {
        if (rx > 0) {
            deltax = -rx;
            rightfix = true;//fix
            leftfix = false;
        }else {
            deltax = rx;
            rightfix = false;
            leftfix = true;//fix
        }
        if (ry > 0) {
            deltay = -ry;
//            leftfix = false;
        }else {
            deltay = ry;
//            leftfix = true;
        }
//        if (rz > 0) {
//            xl += (rzx-xl);
//            xr += (rzx-xr);
//        }else {
//            xl += (rzx-xl);
//            xr += (rzx-xr);
//        }

//        float xln  =  xl + rx;
//        float xrn  =  xr + rx;
//        float yun  =  yu + ry;
//        float ybn  =  yb + ry;
//

        xl += rx;
        xr += rx;
//        yu += ry;
//        yb += ry;
//        float zn  =  z + rz;
//        float zn  =  z + rz;

        if (xl<0) {
            int dist = calDist(rx, screenWidth);
            xl -= rx;
            xr -= rx;
        }
        if (yb<0) {
            int dist = calDist(ry, screenHeight);
//            yb -= ry;
//            yu -= ry;
        }
        if (xr>screenWidth) {
            int dist = calDist(rx, screenWidth);
            xr -= rx;
            xl -= rx;
        }
        if (yu>screenHeight) {
            int dist = calDist(ry, screenHeight);
//            yu -= ry;
//            yb -= ry;
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
        Rect r = new Rect((int)xl, (int) yu, (int) xr, (int) yb);
//        float x = r.centerX();
//        float y = r.centerY();
//        float sinA = (float) Math.sin(Math.toRadians(45));
//        float cosA = (float) Math.cos(Math.toRadians(45));
//        float newX = r.centerX() + (x - r.centerX()) * cosA - (y - r.centerY())
//                * sinA;/*from ww w.  java2s.c  o m*/
//        float newY = r.centerY() + (y - r.centerY()) * cosA + (x - r.centerX())
//                * sinA;
//
//        float dx = newX - x;
//        float dy = newY - y;
//
//        r.offset((int) dx, (int) dy);
//......................................................
//        Rect r2 = new Rect(r);
//        Matrix mat = new Matrix();
//        mat.setRotate(45, r2.centerX(), r2.centerY());
//        mat.mapRect(r2);


//        canvas.save();
//        canvas.rotate(90);
//        paint = new Paint();
//        paint.setColor(Color.rgb(255, 255, 255));
//        canvas.drawRect(r,paint);
//        canvas.drawRect(r, paint);
//        canvas.restore();

        // Clear the path
        path.reset();

        // Define the coordinates of the rectangle
        float left = xl;
        float top = yu;
        float right = xr;
        float bottom = yb;

        // Define the skew factor
        float skewFactor = 0.5f;

        // true means add whit 0
//        float ladded = leftfix ? 0 : deltay; // true means fix
        float yladded = -160; // true means fix
//        float radded = rightfix ? 0 : deltay;
        float yradded = 0;
//        float uadded = leftfix ? 0 : deltax; // true means fix
//        float dadded = rightfix ? 0 : deltay;
        float xladded = -5;

        //left right fix cuz top of paddle
        // Move to the top left corner of the rectangle
        path.moveTo(left, top+yladded);//+ -> min, - -> add, if left fix -> 0

        // Draw the top line of the rectangle
        path.lineTo(right, top+yradded);//+ -> min, - -> add, if right fix -> 0

        // Skew the canvas
        canvas.skew(skewFactor, 0);

        // Draw the right line of the rectangle
        path.lineTo(right+xladded, bottom); //

        // Restore the canvas skew
        canvas.skew(-skewFactor, 0);

        // Draw the bottom line of the rectangle
        path.lineTo(left+xladded,  bottom+yladded);//.........................................................

        // Skew the canvas again
        canvas.skew(skewFactor, 0);

        // Draw the left line of the rectangle
        path.lineTo(left, top+yladded);//.........................................................dup

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


//....................................................................
//
//public class CalibratedGyroscopeProvider extends OrientationProvider {
//
//    /**
//     * Constant specifying the factor between a Nano-second and a second
//     */
//    private static final float NS2S = 1.0f / 1000000000.0f;
//
//    /**
//     * The quaternion that stores the difference that is obtained by the gyroscope.
//     * Basically it contains a rotational difference encoded into a quaternion.
//     *
//     * To obtain the absolute orientation one must add this into an initial position by
//     * multiplying it with another quaternion
//     */
//    private final Quaternion deltaQuaternion = new Quaternion();
//
//    /**
//     * The time-stamp being used to record the time when the last gyroscope event occurred.
//     */
//    private long timestamp;
//
//    /**
//     * This is a filter-threshold for discarding Gyroscope measurements that are below a certain level and
//     * potentially are only noise and not real motion. Values from the gyroscope are usually between 0 (stop) and
//     * 10 (rapid rotation), so 0.1 seems to be a reasonable threshold to filter noise (usually smaller than 0.1) and
//     * real motion (usually > 0.1). Note that there is a chance of missing real motion, if the use is turning the
//     * device really slowly, so this value has to find a balance between accepting noise (threshold = 0) and missing
//     * slow user-action (threshold > 0.5). 0.1 seems to work fine for most applications.
//     *
//     */
//    private static final double EPSILON = 0.1f;
//
//    /**
//     * Value giving the total velocity of the gyroscope (will be high, when the device is moving fast and low when
//     * the device is standing still). This is usually a value between 0 and 10 for normal motion. Heavy shaking can
//     * increase it to about 25. Keep in mind, that these values are time-depended, so changing the sampling rate of
//     * the sensor will affect this value!
//     */
//    private double gyroscopeRotationVelocity = 0;
//
//    /**
//     * Temporary variable to save allocations.
//     */
//    private Quaternion correctedQuaternion = new Quaternion();
//
//    /**
//     * Initialises a new CalibratedGyroscopeProvider
//     *
//     * @param sensorManager The android sensor manager
//     */
//    public CalibratedGyroscopeProvider(SensorManager sensorManager) {
//        super(sensorManager);
//
//        //Add the gyroscope
//        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        // we received a sensor event. it is a good practice to check
//        // that we received the proper event
//        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//
//            // This timestamps delta rotation to be multiplied by the current rotation
//            // after computing it from the gyro sample data.
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
//                deltaQuaternion.setX((float) (sinThetaOverTwo * axisX));
//                deltaQuaternion.setY((float) (sinThetaOverTwo * axisY));
//                deltaQuaternion.setZ((float) (sinThetaOverTwo * axisZ));
//                deltaQuaternion.setW(-(float) cosThetaOverTwo);
//
//                // Matrix rendering in CubeRenderer does not seem to have this problem.
//                synchronized (synchronizationToken) {
//                    // Move current gyro orientation if gyroscope should be used
//                    deltaQuaternion.multiplyByQuat(currentOrientationQuaternion, currentOrientationQuaternion);
//                }
//
//                correctedQuaternion.set(currentOrientationQuaternion);
//                // We inverted w in the deltaQuaternion, because currentOrientationQuaternion required it.
//                // Before converting it back to matrix representation, we need to revert this process
//                correctedQuaternion.w(-correctedQuaternion.w());
//
//                synchronized (synchronizationToken) {
//                    // Set the rotation matrix as well to have both representations
//                    SensorManager.getRotationMatrixFromVector(currentOrientationRotationMatrix.matrix,
//                            correctedQuaternion.array());
//                }
//            }
//            timestamp = event.timestamp;
//        }
//    }
//}

//......................................................................................................................................
//public class MyCustomView extends View {
//
//    private Paint paint;
//    private Path path;
//
//    public MyCustomView(Context context) {
//        super(context);
//        init();
//    }
//
//    public MyCustomView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//
//        path = new Path();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        // Clear the path
//        path.reset();
//
//        // Define the coordinates of the rectangle
//        float left = 100;
//        float top = 100;
//        float right = 400;
//        float bottom = 300;
//
//        // Define the skew factor
//        float skewFactor = 0.5f;
//
//        // Move to the top left corner of the rectangle
//        path.moveTo(left, top);
//
//        // Draw the top line of the rectangle
//        path.lineTo(right, top);
//
//        // Skew the canvas
//        canvas.skew(skewFactor, 0);
//
//        // Draw the right line of the rectangle
//        path.lineTo(right, bottom);
//
//        // Restore the canvas skew
//        canvas.skew(-skewFactor, 0);
//
//        // Draw the bottom line of the rectangle
//        path.lineTo(left, bottom);
//
//        // Skew the canvas again
//        canvas.skew(skewFactor, 0);
//
//        // Draw the left line of the rectangle
//        path.lineTo(left, top);
//
//        // Restore the canvas skew
//        canvas.skew(-skewFactor, 0);
//
//        // Close the path
//        path.close();
//
//        // Draw the path on the canvas
//        canvas.drawPath(path, paint);
//    }
//}