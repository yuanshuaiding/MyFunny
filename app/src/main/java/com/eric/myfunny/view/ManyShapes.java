package com.eric.myfunny.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/11/15
 * desc   : 练习绘制各种图形
 * version: 1.0
 */

public class ManyShapes extends View {
    private Paint paint;

    public ManyShapes(Context context) {
        this(context, null);
    }

    public ManyShapes(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ManyShapes(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画颜色(充满当前控件,这类颜色填充方法一般用于在绘制之前设置底色，或者在绘制之后为界面设置半透明蒙版)
        canvas.drawColor(Color.GRAY);
        //画圆点
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(30);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(100, 100, paint);
        //画方点
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setColor(Color.RED);
        canvas.drawPoint(100, 100 + 50, paint);
        //画直线
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);
        canvas.drawLine(50, 100 + 50 + 50, 50 + 100, 100 + 50 + 50, paint);
        //画矩形
        paint.setStrokeWidth(1);
        paint.setColor(Color.GREEN);
        canvas.drawRect(50, 100 + 50 + 50 + 50, 50 + 100, 100 + 50 + 50 + 50 + 50, paint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(50, 100 + 50 + 50 + 50 + 60, 50 + 100, 100 + 50 + 50 + 50 + 50 + 50, 5, 5, paint);
        }
        //画空心圆
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(70,100 + 50 + 50 + 50 + 60+80,20,paint);
        //画实心圆
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(130,100 + 50 + 50 + 50 + 60+80,20,paint);
    }
}
