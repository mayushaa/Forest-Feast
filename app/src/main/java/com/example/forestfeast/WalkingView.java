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

public class WalkingView extends SurfaceView implements Runnable {

    private final int INTERVAL=200;//זמן המתנה בין תמונה לאחרת
    private Bitmap[] arrBmp;
    private int width;
    private int height;
    private Paint penBg;
    private Bitmap imgPlate, imgPlate2;
    private Walking b1;
    private SurfaceHolder holder;
    private Canvas c;
    private Thread t;
    private boolean go;

    public WalkingView(Context context, int w, int h, Bitmap[] frames) {
        super(context);
        Log.d("WALKING", "WalkingView");

        // Get the SurfaceHolder and set it to transparent
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true); // Ensure this SurfaceView is on top and supports transparency

        this.go = true; // To control the animation loop
        this.width = w;
        this.height = h;
        this.arrBmp = frames;

        // Configure the Paint object for transparency if needed
        this.penBg = new Paint();
        this.penBg.setColor(Color.TRANSPARENT); // This can be kept or adjusted as needed

        // Initialize the animation object
        b1 = new Walking(-120, 75 / 2, arrBmp, this.width, this.height);
        b1.init();

        // Start the drawing thread
        this.t = new Thread(this);
        t.start(); // Execute the `run` method
    }

    public void updateFrames(Bitmap[] newFrames) {
        this.arrBmp = newFrames;
        b1.updateBitmaps(newFrames); // Update the frames in the Walking class
    }

    //ריצת תהליך הרקע
    @Override
    public void run() {
        Log.d("WALKING","WalkingView-run");
        while(this.go)//כל עוד לא ביקשו לעצור
        {
            b1.move();//הזז רקדן
            draw();//צייר  holder
            try {
                Thread.sleep(INTERVAL);//המתן לפני מעבר לתמונה הבאה
            } catch (InterruptedException e) {
                //e.printStackTrace();//dont care
            }
        }
    }
    public void draw()
    {
        Log.d("WALKING","WalkingView-draw");
        if (this.holder.getSurface().isValid())//אם holder פנוי לציור
        {
            this.c=holder.lockCanvas();//נהל למניעה שאחר יוכל להכנס ולבצע שינויים במקביל
            // Clear the previous frame
            this.c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            this.c.drawPaint(penBg);//background צייר צבע רקע
            this.b1.draw(this.c);// צייר דמות
            holder.unlockCanvasAndPost(this.c);//שמור שינויים ובטל נעילה
        }
    }

}
