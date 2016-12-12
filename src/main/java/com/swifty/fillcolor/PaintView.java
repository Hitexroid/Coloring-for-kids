package com.swifty.fillcolor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.swifty.fillcolor.util.DrawUtils;
import com.swifty.fillcolor.util.FloodFill;

import java.util.Arrays;

@SuppressWarnings("unused")
public class PaintView extends View {
    private static final int ALPHA_TRESHOLD = 224;
    private LifecycleListener lifecycleListener;
    private Paint paint;
    private State state;

    public interface LifecycleListener {
        void onPreparedToLoad();
    }

    private static class State {
        private int color;
        private int height, width;
        private Bitmap outlineBitmap;
        private byte[] paintMask;
        private Bitmap paintedBitmap;
        private int[] pixels;
        private byte[] workingMask;

        private State() {
        }
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        state = new State();
        paint = new Paint();
    }

    public PaintView(Context context) {
        this(context, null);
    }

    public synchronized void setLifecycleListener(LifecycleListener l) {
        this.lifecycleListener = l;
    }

    public synchronized Object getState() {
        return state;
    }

    public synchronized void setState(Object o) {
        state = (State) o;
    }

    public void loadFromBitmap(Bitmap originalOutlineBitmap) {
        int w;
        int h;
        State state = new State();
        synchronized (this) {
            w = this.state.width;
            h = this.state.height;
            state.color = this.state.color;
            state.width = w;
            state.height = h;
        }
        int n = w * h;
        Bitmap resizedBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        DrawUtils.convertSizeClip(originalOutlineBitmap, resizedBitmap);
        state.outlineBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        state.paintMask = new byte[n];
        int[] pixels = new int[n];
        resizedBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i2 = 0; i2 < 90; i2++) {
            int iEnd = ((i2 + 1) * n) / 90;
            for (int i = (i2 * n) / 90; i < iEnd; i++) {
                int alpha = 255 - DrawUtils.brightness(pixels[i]);
                state.paintMask[i] = alpha < ALPHA_TRESHOLD ? (byte) 1 : (byte) 0;
                pixels[i] = alpha << 24;
            }
        }
        state.outlineBitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        state.paintedBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        state.paintedBitmap.eraseColor(-1);
        state.workingMask = new byte[n];
        state.pixels = new int[n];
        Arrays.fill(state.pixels, -1);
        synchronized (this) {
            this.state = state;
        }

    }

    public synchronized boolean isInitialized() {
        return state.paintedBitmap != null;
    }

    public synchronized void setPaintColor(int color) {
        state.color = color;
        paint.setColor(color);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        synchronized (this) {
            if (state.width == 0 || state.height == 0) {
                state.width = w;
                state.height = h;
                if (lifecycleListener != null) {
                    lifecycleListener.onPreparedToLoad();
                }
            }
        }
    }

    protected synchronized void onDraw(Canvas canvas) {
        if (state.paintedBitmap != null) {
            canvas.drawBitmap(state.paintedBitmap, 0.0f, 0.0f, paint);
        }
        if (state.outlineBitmap != null) {
            canvas.drawBitmap(state.outlineBitmap, 0.0f, 0.0f, paint);
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == 0) {
            paint((int) e.getX(), (int) e.getY());
        }
        return true;
    }

    private synchronized void paint(int x, int y) {
        System.arraycopy(state.paintMask, 0, state.workingMask, 0, state.width * state.height);
        FloodFill.fillRaw(x, y, state.width, state.height, state.workingMask, state.pixels, state.color);
        state.paintedBitmap.setPixels(state.pixels, 0, state.width, 0, 0, state.width, state.height);
        invalidate();
    }
}
