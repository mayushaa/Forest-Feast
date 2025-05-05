package com.example.forestfeast;

import android.graphics.Bitmap;
import android.util.Log;

public class Swirl extends Figure {

    private final int HEIGHT=1327;
    private final int WIDTH=2360;
    private final int NUM_PICS=3;

    private int dx;
    private int dy;
    private int status;
    private Bitmap[] arrBmp;

    public Swirl(int x, int y,Bitmap[] arrBmp , int maxWidth, int maxHeigth) {
        super(x,y, arrBmp[0],maxWidth, maxHeigth);
        Log.d("SWIRL","Swirl");
        this.arrBmp=arrBmp;
        this.dx=x;
        this.dy=y;
        this.status=0;
    }

    public void init()
    {
        Log.d("SWIRL","Swirl-init");
        this.status=0;
        this.img=Bitmap.createScaledBitmap(arrBmp[this.status],HEIGHT,WIDTH,false);
    }


    @Override
    public void move() {
        Log.d("SWIRL", "Swirl-move");
        this.status = (this.status + 1) % NUM_PICS;

        this.img = Bitmap.createScaledBitmap(arrBmp[this.status], HEIGHT, WIDTH, false);
    }

}
