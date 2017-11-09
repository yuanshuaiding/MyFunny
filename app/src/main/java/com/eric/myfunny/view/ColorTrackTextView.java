package com.eric.myfunny.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.eric.myfunny.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/11/07
 * desc   : 对外提供接口可以不断修改字体颜色的文本控件
 * version: 1.0
 */

public class ColorTrackTextView extends AppCompatTextView {
    //绘制方向
    public static final int DIRECT_LEFT_RIGHT = 0;
    public static final int DIRECT_RIGTH_LEFT = 1;
    private final Rect bounds;
    private int mOriginColor = Color.BLACK;
    private int mTrackColor = Color.RED;
    private Paint mOriginPaint;
    private Paint mTrackPaint;
    private float mProgress;
    private int mDirection = DIRECT_LEFT_RIGHT;

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取颜色值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        if (typedArray != null) {
            mOriginColor = typedArray.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
            mTrackColor = typedArray.getColor(R.styleable.ColorTrackTextView_trackColor, getTextColors().getDefaultColor());
            typedArray.recycle();
        }
        setTextColor(mOriginColor);
        setTrackColor(mTrackColor);
        initPaint();
        bounds = new Rect();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mOriginColor = color;
    }

    private void initPaint() {
        if (mOriginPaint == null)
            mOriginPaint = new Paint();
        mOriginPaint.setColor(mOriginColor);
        mOriginPaint.setTextSize(getTextSize());
        mOriginPaint.setStyle(Paint.Style.FILL);
        mOriginPaint.setAntiAlias(true);
        if (mTrackPaint == null)
            mTrackPaint = new Paint();
        mTrackPaint.setColor(mTrackColor);
        mTrackPaint.setTextSize(getTextSize());
        mTrackPaint.setStyle(Paint.Style.FILL);
        mTrackPaint.setAntiAlias(true);
    }

    public void setTrackColor(int trackColor) {
        this.mTrackColor = trackColor;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setDirection(int direction) {
        this.mDirection = direction;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //取消原来的绘制流程，改为自己实现
        //super.onDraw(canvas);
        canvas.save();
        String text = getText().toString();
        //1.获取绘制起点和基线
        mOriginPaint.getTextBounds(text, 0, text.length(), bounds);
        int dx = (getWidth() - bounds.width()) / 2 + getPaddingLeft() - getPaddingRight();
        int dy = (bounds.bottom - bounds.top) / 2;

        Paint.FontMetricsInt fm = mOriginPaint.getFontMetricsInt();
        int baseline = getPaddingTop() + dy + ((fm.bottom - fm.top) / 2 - fm.bottom);
        //2.绘制原始文本
        canvas.drawText(text, dx, baseline, mOriginPaint);
        canvas.restore();

        //3.绘制渐变色文本(根据绘制方向计算绘制区域)
        canvas.save();
        //设置渐变区域
        int middle = (int) (mProgress * (getWidth() - dx)) + dx;
        if (mDirection == DIRECT_RIGTH_LEFT) {
            middle = getWidth() - middle;
            //裁剪出渐变区域
            canvas.clipRect(middle, 0, getWidth(), getHeight());
        } else {
            //裁剪出渐变区域
            canvas.clipRect(0, 0, middle, getHeight());
        }
        canvas.drawText(text, dx, baseline, mTrackPaint);
        canvas.restore();
    }

}
