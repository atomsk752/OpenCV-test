package com.honeyrock.opencv_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomDrawView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float initX, initY, radius;
    ArrayList<Float[]> arrXY = new ArrayList<>();
    public CustomDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint MyPaint = new Paint();
        MyPaint.setStrokeWidth(10f);
        MyPaint.setStyle(Paint.Style.STROKE);
        MyPaint.setColor(Color.WHITE);
        Path path = new Path();
        path.moveTo(450,500);
        path.lineTo(450,500);
        path.lineTo(400,600);
        path.lineTo(300,600);
        path.lineTo(400,700);
        path.lineTo(350,800);
        path.lineTo(450,700);
        path.lineTo(550,800);
        path.lineTo(500,700);
        path.lineTo(600,600);
        path.lineTo(500,600);
        path.close();
        canvas.drawPath(path,MyPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float curX = event.getX();
        float curY = event.getY();
        if (action == MotionEvent.ACTION_UP){
            initX = event.getX();
            initY = event.getY();
            radius = 1;
            Log.i("touchEvent", curX+":"+curY);
            arrXY.add(new Float[]{event.getX(), event.getY()});
        }

        return true;
    }
}
