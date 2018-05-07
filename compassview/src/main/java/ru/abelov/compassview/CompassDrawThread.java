package ru.abelov.compassview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import ru.abelov.compassview.R;

public class CompassDrawThread extends Thread {
    Context mContext;
    Bitmap arrow;
    private boolean running = false;
    private SurfaceHolder surfaceHolder;

    private Object mPauseLock = new Object();

//    public CompassDrawThread(SurfaceHolder surfaceHolder) {
//        this.surfaceHolder = surfaceHolder;
//        arrow = BitmapFactory.decodeResource(App.Instance().getResources(), R.drawable.arrow);
//    }

    public CompassDrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        mContext = context;
        arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
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
                float[] orientation = GISensors.Instance(mContext).getOrientation();
                canvas.rotate(-orientation[0], canvas.getWidth() / 2, canvas.getHeight() / 2);
                canvas.drawBitmap(arrow, new Rect(0, 0, (int) arrow_width, (int) arrow_height), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);
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
