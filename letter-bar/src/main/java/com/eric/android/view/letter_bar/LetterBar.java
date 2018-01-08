package com.eric.android.view.letter_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/11/09
 * desc   : 26个英文字母侧边条
 * version: 1.0
 */

public class LetterBar extends View {
    private final Paint mPaint;
    private int mColor = Color.BLACK;
    private int mHighlightColor = Color.RED;
    private float mSize = sp2px(14);

    private static final String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int mLetterIndex = -1;
    private boolean showBg;//显示背景色
    private OnLetterChangeListner onLetterChangeListener;//选中字母后的回调

    public LetterBar(Context context) {
        this(context, null);
    }

    public LetterBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterBar);
        if (typedArray != null) {
            mColor = typedArray.getColor(R.styleable.LetterBar_android_textColor, mColor);
            mHighlightColor = typedArray.getColor(R.styleable.LetterBar_highlightColor, mHighlightColor);
            mSize = typedArray.getDimensionPixelSize(R.styleable.LetterBar_android_textSize, (int) mSize);
            typedArray.recycle();
        }
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setTextSize(mSize);
        mPaint.setAntiAlias(true);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
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
        if (showBg) {
            mPaint.setColor(Color.parseColor("#10000000"));
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
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
        String letter = mLetters[mLetterIndex];
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //开始触摸
                showBg = true;
                //手指滑动
                if (onLetterChangeListener != null) {
                    onLetterChangeListener.onLetterChanged(letter);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起
                mLetterIndex = -1;
                showBg = false;
                if (onLetterChangeListener != null) {
                    onLetterChangeListener.onLetterChoosed(letter);
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setOnLetterChangeListener(OnLetterChangeListner listener) {
        this.onLetterChangeListener = listener;
    }

    public interface OnLetterChangeListner {
        void onLetterChanged(String letter);

        void onLetterChoosed(String letter);
    }
}
