package com.example.forestfeast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Walking extends Figure {
    private final double speed = 2400;
    private final double dt = 1 / 60.0;

    public Walking(int x, int y, Bitmap[] frames, int width, int height, int screenWidth, int screenHeight) {
        super(x, y, frames, width, height, screenWidth, screenHeight);
    }

//    public void updateBitmaps(Bitmap[] newFrames) {
//        if (newFrames != null && newFrames.length == NUM_PICS) {
//            this.arrBmp = newFrames;
//            this.status = 0;
//            this.img = Bitmap.createScaledBitmap(arrBmp[this.status], HEIGHT, WIDTH, false);
//            Log.d("WALKING", "Bitmaps updated");
//        } else {
//            Log.e("WALKING", "Invalid bitmap array passed to updateBitmaps");
//        }
//    }

    @Override
    public void animate() {
        super.animate();

//        if (x + width / 2 < screenWidth / 2)
        x += (int) (speed * dt);
        Log.d("mayas", "xc " + x);
    }
}
