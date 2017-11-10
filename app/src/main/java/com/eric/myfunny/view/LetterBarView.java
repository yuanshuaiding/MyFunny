package com.eric.myfunny.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.eric.myfunny.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/11/09
 * desc   : 26个英文字母侧边条
 * version: 1.0
 */

public class LetterBarView extends View {
    private final Paint mPaint;
    private int mColor = Color.BLACK;
    private int mHighlightColor = Color.RED;
    private int mSize = 14;
    private static final String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int mLetterIndex = -1;
    private boolean showBg;//显示背景色

    public LetterBarView(Context context) {
        this(context, null);
    }

    public LetterBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterBarView);
        if (typedArray != null) {
            mColor = typedArray.getColor(R.styleable.LetterBarView_android_textColor, mColor);
            mHighlightColor = typedArray.getColor(R.styleable.LetterBarView_highlightColor, mHighlightColor);
            mSize = typedArray.getDimensionPixelSize(R.styleable.LetterBarView_android_textSize, mSize);
            typedArray.recycle();
        }
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setTextSize(mSize);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //只需要测量一个字母所需的宽度即可,M较胖，就选它
        int letterWidth = (int) mPaint.measureText("M") + getPaddingLeft() + getPaddingRight();
        setMeasuredDimension(letterWidth, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景色
        if(showBg){
            mPaint.setColor(Color.parseColor("#10000000"));
            canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
        }
        //绘制字母，需要先计算baseline
        int letterHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int base = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        for (int i = 0; i < mLetters.length; i++) {
            float dx = getWidth() / 2 - mPaint.measureText(mLetters[i]) / 2;
            int dy = getPaddingTop() + i * letterHeight + letterHeight / 2;
            int baseline = dy + base;
            if (mLetterIndex == i) {
                mPaint.setColor(mHighlightColor);
                canvas.drawText(mLetters[i], dx, baseline, mPaint);
            } else {
                mPaint.setColor(mColor);
                canvas.drawText(mLetters[i], dx, baseline, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        //计算该坐标对应的字母索引
        mLetterIndex = (int) ((y - getPaddingTop()) / (getHeight() - getPaddingTop() - getPaddingBottom()) * mLetters.length);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //开始触摸
                showBg = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //手指滑动
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起
                mLetterIndex = -1;
                showBg = false;
                invalidate();
                break;
        }
        return true;
    }
}
