package com.example.forestfeast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public abstract class Figure {
    protected Bitmap[] frames;
    protected int status;
    protected int screenWidth;
    protected int screenHeight;
    protected int width;
    protected int height;
    protected int x;
    protected int y;

    public Figure(int x, int y, Bitmap[] frames, int width, int height, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.status = 0;

        for(int i = 0; i < frames.length; i++)
            frames[i] = Bitmap.createScaledBitmap(frames[i], width, height,false);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void animate() {
        status = (status + 1) % frames.length;
    }

    public void draw(Canvas c) {
        Log.d("mayas", String.valueOf(x));
        c.drawBitmap(frames[status], (int)(x - width / 2.0 + screenWidth / 2.0), (int)(y - height / 2.0 + screenHeight / 2.0), null);
    }
}
