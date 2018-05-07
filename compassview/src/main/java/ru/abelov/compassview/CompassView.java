package ru.abelov.compassview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CompassView extends SurfaceView implements SurfaceHolder.Callback {
    boolean isRunning = false;
    private CompassDrawThread drawThread;
    private Context mContext;

    public CompassView(Context context) {
        super(context);
        mContext = context;
        getHolder().addCallback(this);
        setZOrderOnTop(true);    // necessary
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        drawThread = new CompassDrawThread(mContext, getHolder());
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getHolder().addCallback(this);
        if (!isInEditMode()) {
            setZOrderOnTop(true);    // necessary
        }
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        drawThread = new CompassDrawThread(mContext, getHolder());
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        getHolder().addCallback(this);
        setZOrderOnTop(true);    // necessary
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        drawThread = new CompassDrawThread(mContext, getHolder());
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int whats = 0;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!isRunning) {
            drawThread.start();
            isRunning = true;
        } else {
            drawThread.onResume();
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.onPause();
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

}
