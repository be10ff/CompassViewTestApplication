package ru.abelov.compassview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class CompassDrawThread extends Thread {
    Context mContext;
    Bitmap arrow;

    Matrix matrix;
    private boolean running = false;
    private SurfaceHolder surfaceHolder;

    private Object mPauseLock = new Object();

    boolean newMath;

    GISensors sensors;

//    public CompassDrawThread(SurfaceHolder surfaceHolder) {
//        this.surfaceHolder = surfaceHolder;
//        arrow = BitmapFactory.decodeResource(App.Instance().getResources(), R.drawable.arrow);
//    }

    public CompassDrawThread(Context context, SurfaceHolder surfaceHolder, boolean newMath) {
        this.surfaceHolder = surfaceHolder;
        mContext = context;
        arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
        sensors =  GISensors.Instance(mContext);
        matrix = new Matrix();
        this.newMath = newMath;
    }

    @Override
    public synchronized void start() {
        super.start();
        running = true;
    }

    // Two methods for your Runnable/Thread class to manage the thread properly.

    public void onPause() {
        synchronized (mPauseLock) {
            running = false;
        }
    }

    public void onResume() {
        synchronized (mPauseLock) {
            running = true;
            mPauseLock.notifyAll();
        }
    }

//    https://i.stack.imgur.com/eyibk.jpg
//вот собственно и ответ. мне надо посчитать угол между вертикальной плоскотью содержащей Y и направлением на север


    //короч для вертикально телефона считать угол уже между Z и севером
    //и отрисовывать не как стрелку, а как шкалу

//    2. это потому что нас интересуют углы между вертикальными плоскостями в которых лежат оси тилипона и такой же вертикальной плоскостью содержащей направление на север

    //ак вот. я могу поверх изображения с камеры рисовать в нужном масштабе(богрешность - радиус, угол зрения - atan (погрешность/расстояние доточки)

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            try {
                sleep(300);
                float arrow_width = arrow.getWidth();
                float arrow_height = arrow.getHeight();
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null) continue;
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                float azimuth = 0;
                if(newMath){
                    azimuth = -(float)sensors.getAzimuth();
                } else {
                    float[] orientation = sensors.getOrientation();
                    azimuth = -orientation[0];
                }

                canvas.rotate(azimuth, canvas.getWidth() / 2, canvas.getHeight() / 2);
                canvas.drawBitmap(arrow, new Rect(0, 0, (int) arrow_width, (int) arrow_height), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);

                //ORIENTATION
//                float[] orientation =
                matrix.reset();

                // 90 потому как стрелка на битмапе уже повернута


                double yaw = sensors.getGravity().getYaw();
                double pitch = sensors.getGravity().getPitch();
                double roll = sensors.getGravity().getRoll();

//                Log.i(GISensors.SENSOR_TAG, "yaw = " + yaw + ", pitch = " + pitch + ", roll = " + roll);


//                matrix.setRotate((float)(azimuth - 90), image.getWidth()/2, image.getHeight()/2);
//                matrix.postTranslate((int)(m_map.m_view.centerX() + (1+length)*size*Math.sin(Math.toRadians(azimuth)) - image.getWidth()/2 - map_location[0]), (int)(m_map.m_view.centerY() - (1+length)*size*Math.cos(Math.toRadians(azimuth))- image.getHeight()/2 - map_location[1]));
//
//                canvas.drawBitmap(image, matrix, null);


            } catch (Exception e) {
                String res = e.toString();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        synchronized (mPauseLock) {
            while (!running) {
                try {
                    mPauseLock.wait();
                } catch (InterruptedException e) {

                }
            }
        }
    }
}
