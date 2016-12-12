package com.swifty.fillcolor.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.RectF;

public final class DrawUtils {
    public static void convertSizeClip(Bitmap src, Bitmap dest) {
        Canvas canvas = new Canvas(dest);
        RectF srcRect = new RectF(0.0f, 0.0f, (float) src.getWidth(), (float) src.getHeight());
        RectF destRect = new RectF(0.0f, 0.0f, (float) dest.getWidth(), (float) dest.getHeight());
        Matrix mDestSrc = new Matrix();
        mDestSrc.setRectToRect(destRect, srcRect, ScaleToFit.FILL);
        Matrix mSrcDest = new Matrix();
        mDestSrc.invert(mSrcDest);
        canvas.drawBitmap(src, mSrcDest, new Paint(4));
    }

    public static void convertSizeFill(Bitmap src, Bitmap dest) {
        Canvas canvas = new Canvas(dest);
        RectF srcRect = new RectF(0.0f, 0.0f, (float) src.getWidth(), (float) src.getHeight());
        RectF destRect = new RectF(0.0f, 0.0f, (float) dest.getWidth(), (float) dest.getHeight());
        Matrix m = new Matrix();
        m.setRectToRect(srcRect, destRect, ScaleToFit.FILL);
        canvas.drawBitmap(src, m, new Paint(4));
    }

    public static int brightness(int color) {
        return (color >> 16) & 255;
    }
}
