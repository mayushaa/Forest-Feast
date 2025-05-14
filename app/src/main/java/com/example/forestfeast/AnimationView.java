package com.example.forestfeast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimationView extends SurfaceView implements Runnable {
    private int interval;
    private Paint penBg;
    private Figure animation;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Thread animationThread;

    public AnimationView(Context context, int interval) {
        super(context);

        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);

        penBg = new Paint();
        penBg.setColor(Color.TRANSPARENT);

        this.interval = interval;
    }

    public void setFrames(Figure figure){
        animation = figure;
        start();
    }

    public void start(){
        animationThread = new Thread(this);
        animationThread.start();
    }

    @Override
    public void run() {
        while (true){
            animation.animate();
            draw();

            Log.d("maya debugging", "animation run");

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {}
        }
    }

    protected void draw() {
        if (!holder.getSurface().isValid())
            return;

        canvas = holder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawPaint(penBg);
        animation.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }
}
