package com.eric.myfunny.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.eric.baselib.utils.Util;
import com.eric.myfunny.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/23
 * desc   : 仿QQ计步器，效果见screenShots/qqstep.gif
 * version: 1.0
 */

public class QQStepView extends View {
    private static final String TAG = "QQStepView";
    private final Paint mPaint;//画笔
    private final Rect textBounds;
    private float mCurrentStep;
    private float mProgressStep;
    private int mOuterCicleColor;
    private int mInnerCicleColor;
    private int mTextColor;
    private int mTextSize;
    private int mLineWidth;
    private RectF mDrawRect;//绘制范围
    private float mMaxStep = 100;
    private float mDuration;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 1.构造函数，获取自定义属性
     */
    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        if (typedArray != null) {
            mOuterCicleColor = typedArray.getColor(R.styleable.QQStepView_outterColor, Color.BLUE);
            mInnerCicleColor = typedArray.getColor(R.styleable.QQStepView_innerColor, Color.RED);
            mTextColor = typedArray.getColor(R.styleable.QQStepView_android_textColor, Color.BLACK);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.QQStepView_android_textSize, 18);
            mLineWidth = (int) typedArray.getDimension(R.styleable.QQStepView_lineWidth, 5);
            mMaxStep = typedArray.getFloat(R.styleable.QQStepView_maxStep, mMaxStep);
            mCurrentStep = typedArray.getFloat(R.styleable.QQStepView_step, 0);
            mDuration = typedArray.getFloat(R.styleable.QQStepView_duration, 1000);
            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setColor(mOuterCicleColor);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        textBounds = new Rect();
    }

    /**
     * 测量自定义View的尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //给定一个最小值
            width = Util.dip2px(getContext(), 200);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //给定一个最小值
            height = Util.dip2px(getContext(), 200);
        }

        int size = width > height ? height : width;
        //取两者中最小值，确保为正方形
        setMeasuredDimension(size, size);
    }

    /**
     * 在该回调方法中绘制所需图形
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        //1.绘制外圆弧
        if (mDrawRect == null)
            mDrawRect = new RectF(mLineWidth / 2, mLineWidth / 2, getWidth() - mLineWidth / 2, getHeight() - mLineWidth / 2);

        //绘制起始位置为0时是从右侧最顶端开始
        mPaint.setColor(mOuterCicleColor);
        canvas.drawArc(mDrawRect, 135, 270, false, mPaint);
        //2.绘制内圆弧
        mPaint.setColor(mInnerCicleColor);
        float rate = mProgressStep / mMaxStep;
        if (rate > 1)
            rate = 1;
        float sweepAngle = 270 * (rate);
        canvas.drawArc(mDrawRect, 135, sweepAngle, false, mPaint);
        //2.绘制文本,难点是找到绘制的起始点
        int dx = getWidth() / 2;
        int dy = getHeight() / 2;
        String mText = ((int) mProgressStep) + "";
        if (!TextUtils.isEmpty(mText)) {
            mPaint.reset();
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            mPaint.setAntiAlias(true);
            //计算文字尺寸
            mPaint.getTextBounds(mText, 0, mText.length(), textBounds);
            int txtWidth = textBounds.width();
            //Log.d(TAG, "文字宽度：" + txtWidth);
            dx = dx - txtWidth / 2;
            int y = (mPaint.getFontMetricsInt().bottom - mPaint.getFontMetricsInt().top) / 2 - mPaint.getFontMetricsInt().bottom;
            //Log.d(TAG, "文字高度：" + txtHeight);
            dy = dy + y;
            canvas.drawText(mText, dx, dy, mPaint);
        }
    }

    /**
     * 设置当前值
     */
    private void initSteps(float steps) {
        mProgressStep = steps;
        invalidate();
    }

    /**
     * 重新设置当前进度
     */
    public void updateSteps(float mSteps) {
        mCurrentStep = mSteps;
        if (mCurrentStep > mMaxStep)
            mCurrentStep = mMaxStep;
        ValueAnimator animator = ValueAnimator.ofFloat(0, mCurrentStep).setDuration((long) mDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                initSteps((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    /**
     * 设置最大值
     */
    public void setMaxSteps(float steps) {
        mMaxStep = steps;
    }

    /**
     * 设置动画时长
     *
     * @param duration 毫秒
     */
    public void setDuration(float duration) {
        this.mDuration = duration;
    }
}
