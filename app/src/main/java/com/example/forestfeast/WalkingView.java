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

public class WalkingView extends AnimationView {
    public WalkingView(Context context, int interval) {
        super(context, interval);
    }

//    public void updateFrames(Bitmap[] newFrames) {
//        this.arrBmp = newFrames;
//        b1.updateBitmaps(newFrames); // Update the frames in the Walking class
//    }
}
