package com.example.forestfeast;

import android.graphics.Bitmap;
import android.util.Log;

public class Mixing extends Figure {

    private final int HEIGHT=1327;
    private final int WIDTH=2360;
    private final int NUM_PICS=4;

    private int dx;
    private int dy;
    private int status;
    private Bitmap[] arrBmp;

    public Mixing(int x, int y,Bitmap[] arrBmp , int maxWidth, int maxHeigth) {
        super(x,y, arrBmp[0],maxWidth, maxHeigth);
        Log.d("MIXING","Mixing");
        this.arrBmp=arrBmp;
        this.dx=x;
        this.dy=y;
        this.status=0;
    }

    public void init()
    {
        Log.d("MIXING","Mixing-init");
        this.status=0;
        this.img=Bitmap.createScaledBitmap(arrBmp[this.status],HEIGHT,WIDTH,false);
    }


    @Override
    public void move() {
        Log.d("MIXING", "Mixing-move");
        this.status = (this.status + 1) % NUM_PICS;

        this.img = Bitmap.createScaledBitmap(arrBmp[this.status], HEIGHT, WIDTH, false);
    }

}
