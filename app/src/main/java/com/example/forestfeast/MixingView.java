package com.example.forestfeast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.PixelFormat;

public class MixingView extends SurfaceView implements Runnable {

    private final int INTERVAL = 400;
    private final Bitmap[] arrBmp;
    private int width;
    private int height;
    private Paint penBg;
    private Bitmap imgPlate, imgPlate2;
    private Mixing b1;
    private SurfaceHolder holder;
    private Canvas c;
    private Thread t;
    private boolean go;

    public MixingView(Context context, int w, int h) {
        super(context);
        Log.d("MIXING", "MixingView");

        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);

        this.go = true;
        this.width = w;
        this.height = h;

        this.penBg = new Paint();
        this.penBg.setColor(Color.TRANSPARENT);

        arrBmp = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.ani_mix_1),
                BitmapFactory.decodeResource(getResources(), R.drawable.ani_mix_2),
                BitmapFactory.decodeResource(getResources(), R.drawable.ani_mix_3),
                BitmapFactory.decodeResource(getResources(), R.drawable.ani_mix_4),
        };

        b1 = new Mixing(-120, 75 / 2, arrBmp, this.width, this.height);
        b1.init();

        this.t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        Log.d("MIXING", "MixingView-run");
        while (this.go)
        {
            b1.move();
            draw();
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
            }
        }
    }

    public void draw() {
        Log.d("MIXING", "MixingView-draw");
        if (this.holder.getSurface().isValid())
        {
            this.c = holder.lockCanvas();
            this.c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            this.c.drawPaint(penBg);
            this.b1.draw(this.c);
            holder.unlockCanvasAndPost(this.c);
        }
    }
}
