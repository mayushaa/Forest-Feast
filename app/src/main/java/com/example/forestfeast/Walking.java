package com.example.forestfeast;

import android.graphics.Bitmap;
import android.util.Log;

public class Walking extends Figure {

    private final int HEIGHT=1327;
    private final int WIDTH=2360;
    private final int NUM_PICS=3;

    private int dx;
    private int dy;
    private int status;
    private Bitmap[] arrBmp;

    public Walking(int x, int y,Bitmap[] arrBmp , int maxWidth, int maxHeigth) {
        super(x,y, arrBmp[0],maxWidth, maxHeigth);
        Log.d("WALKING","Walking");
        this.arrBmp=arrBmp;
        this.dx=x;
        this.dy=y;
        this.status=0;
    }

    public void init()
    {
        Log.d("WALKING","Walking-init");
        this.status=0;
        this.img=Bitmap.createScaledBitmap(arrBmp[this.status],HEIGHT,WIDTH,false);
    }

    public void updateBitmaps(Bitmap[] newFrames) {
        if (newFrames != null && newFrames.length == NUM_PICS) {
            this.arrBmp = newFrames;
            this.status = 0;
            this.img = Bitmap.createScaledBitmap(arrBmp[this.status], HEIGHT, WIDTH, false);
            Log.d("WALKING", "Bitmaps updated");
        } else {
            Log.e("WALKING", "Invalid bitmap array passed to updateBitmaps");
        }
    }

    @Override
    public void move() {
        Log.d("WALKING", "Walking-move");
        this.status = (this.status + 1) % NUM_PICS;
        this.dx=20;
        this.x+=dx;
        if (this.getXr()>maxWidth/2 && dx>0)
        {
            dx = 0;;
        }
        this.img = Bitmap.createScaledBitmap(arrBmp[this.status], HEIGHT, WIDTH, false);
    }

}
