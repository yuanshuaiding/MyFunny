package com.eric.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/11/16
 * desc   : xxxx描述
 * version: 1.0
 */

public class ColorfulPieChart extends View {
    public ColorfulPieChart(Context context) {
        this(context, null);
    }

    public ColorfulPieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulPieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            //设置默认宽度
            width = 200;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            //设置默认高度
            height = 200;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
