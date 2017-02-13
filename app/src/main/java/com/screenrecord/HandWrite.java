package com.screenrecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.main.thenhelper.R;

/**
 * Created by admin on 2015/09/29   .
 */
public class HandWrite extends View {
    Paint paint = null;
    private Bitmap originalBitmap = null;
    Bitmap new1Bitmap = null;
    private Bitmap new2Bitmap = null;
    private float clickX = 0, clickY = 0;
    private float startX = 0, startY = 0;
    private boolean isMove = true;
    private boolean isClear = false;
    int color = Color.WHITE;
    float strokeWidth = 3.0f;

    public HandWrite(Context context, AttributeSet attrs) {
        super(context, attrs);
//      originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ico).copy(Bitmap.Config.ARGB_8888, true);
        new1Bitmap = Bitmap.createBitmap(originalBitmap);
    }


    public void clear() {
        isClear = true;
        new2Bitmap = Bitmap.createBitmap(originalBitmap);
        invalidate();
    }

    public void setstyle(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(HandWriting(new1Bitmap), 0, 0, null);

    }

    public Bitmap HandWriting(Bitmap originalBitmap) {
        Canvas canvas = null;

        if (isClear) {
            canvas = new Canvas(new2Bitmap);
        } else {
            canvas = new Canvas(originalBitmap);
        }
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        if (isMove) {
            canvas.drawLine(startX, startY, clickX, clickY, paint);
        }

        startX = clickX;
        startY = clickY;

        if (isClear) {
            return new2Bitmap;
        }
        return originalBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            isMove = false;
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            isMove = true;
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

}