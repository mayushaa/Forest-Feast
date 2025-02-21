package com.example.forestfeast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public abstract class Figure {

    protected int x;
    protected int y;
    protected Bitmap img;
    static int maxWidth;// width of screen
    static int maxHeight;//height of screen

    public Figure(int x, int y, Bitmap img, int maxWidth, int maxHeight) {
        Log.d("MIXING", "Figure");
        this.x = x;
        this.y = y;
        if (img == null)
            Log.d("MIXING", "img is null");

        this.img = img;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public int getXr() {
        return x + img.getWidth();
    }

    public int getYd() {
        return y + img.getHeight();
    }


    public void draw(Canvas c) {
        Log.d("MIXING", "Figure-draw");
        c.drawBitmap(this.img, this.x, this.y, null);
    }

    public void move() {
    }

}
