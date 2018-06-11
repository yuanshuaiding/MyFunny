package com.eric.myfunny.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.eric.baselib.utils.Util;
import com.eric.myfunny.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/23
 * desc   : 风险雷达图
 * version: 1.0
 */
public class RiskRadarView extends View {
    private final int[] resourceIds = {R.mipmap.ic_risk_arrow0, R.mipmap.ic_risk_arrow1, R.mipmap.ic_risk_arrow2, R.mipmap.ic_risk_arrow3};
    private int mMinWidth;//刻度半径
    private int mMiniArrowId = resourceIds[0];
    private int sweepAngle = 250;
    private int[] colors = {0xff03db88, 0xffbcdc10, 0xffff870e, 0xffff4639, 0xff03db88};//渐变色
    private Paint mPaint;
    private int mOutDialLineWidth;
    private int mOutRingLineWidth;
    private int mInnerRingLineWidth;
    private int mAlpha = 51;
    private int mSpace = 10;
    private int mRedArrow = R.mipmap.red_arrow_up;
    private float mPrograss = 0;
    private float mTotalPrograss = 100;
    private Float mAnimPrograss;
    public int mLevel;
    private Bitmap miniBitmap;

    public RiskRadarView(Context context) {
        this(context, null);
    }

    public RiskRadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RiskRadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        setLevel(0);
    }

    public void setLevel(int level) {
        if (level >= resourceIds.length) {
            mLevel = 3;
        } else
            mLevel = level;
        miniBitmap = BitmapFactory.decodeResource(getResources(), resourceIds[mLevel]);
    }

    public void setPrograss(float prograss) {
        this.mPrograss = prograss;
        if (mPrograss > mTotalPrograss)
            mPrograss = mTotalPrograss;
        ValueAnimator animator = ValueAnimator.ofFloat(0, mPrograss).setDuration(1000);
        animator.addUpdateListener(animation -> updatePrograss((Float) animation.getAnimatedValue()));
        animator.start();
    }

    public void setMaxPrograss(float maxPrograss) {
        if (maxPrograss <= 0) {
            return;
        }
        this.mTotalPrograss = maxPrograss;
    }

    private void updatePrograss(Float value) {
        mAnimPrograss = value;
        postInvalidate();
    }

    private void initPaint() {
        //默认尺寸
        mMinWidth = Util.dip2px(getContext(), 100);
        //默认线条粗细
        mOutDialLineWidth = Util.dip2px(getContext(), 3);
        mOutRingLineWidth = Util.dip2px(getContext(), 5);
        mInnerRingLineWidth = Util.dip2px(getContext(), 2);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            //给定一个最小值
            width = mMinWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            //给定一个最小值
            height = width;
        }
        int size = width > height ? height : width;
        //取两者中最小值，确保为正方形
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //使用inited标识确保只绘制一次(几个基本环无需变化)
        //绘制外部刻度
        drawOutDial(canvas, mPaint);
        //绘制外部实心圆环
        drawOutRing(canvas, mPaint);
        //绘制外部深色进度条
        drawOutDarkRing(canvas, mPaint);
        //绘制内部实心圆环
        drawInnerRing(canvas, mPaint);
        //绘制内部深色进度条
        drawInnerDarkRing(canvas, mPaint);
        //绘制中间的箭头
        drawCenterArrow(canvas, mPaint);
        //绘制小指针
        drawMiniArrow(canvas, mPaint);
    }

    private void drawOutDial(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        mPaint.setAlpha(mAlpha);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        int rotateAngle = (360 - sweepAngle) / 2 + 90;
        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        sweepGradient.setLocalMatrix(matrix);
        //虚线效果
        PathEffect pathEffect = new DashPathEffect(new float[]{5, 5}, 0);
        mPaint.setPathEffect(pathEffect);
        mPaint.setShader(sweepGradient);
        mPaint.setStrokeWidth(mOutDialLineWidth);
        RectF rectF = new RectF(mOutDialLineWidth / 2, mOutDialLineWidth / 2, getWidth() - mOutDialLineWidth / 2, getHeight() - mOutDialLineWidth / 2);
        canvas.drawArc(rectF, rotateAngle, sweepAngle, false, mPaint);
    }

    private void drawOutRing(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        mPaint.setAlpha(mAlpha);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int rotateAngle = (360 - sweepAngle) / 2 + 90;
        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        sweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(sweepGradient);
        mPaint.setStrokeWidth(mOutRingLineWidth);
        int padding = mOutDialLineWidth + mSpace + mOutRingLineWidth / 2;
        RectF rectF = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawArc(rectF, rotateAngle, sweepAngle, false, mPaint);
    }

    private void drawOutDarkRing(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        mPaint.setAlpha(255);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int rotateAngle = (360 - sweepAngle) / 2 + 90;
        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        sweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(sweepGradient);
        mPaint.setStrokeWidth(mOutRingLineWidth);
        int padding = mOutDialLineWidth + mSpace + mOutRingLineWidth / 2;
        RectF rectF = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawArc(rectF, rotateAngle, sweepAngle * (mAnimPrograss / mTotalPrograss), false, mPaint);
    }

    private void drawInnerRing(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        mPaint.setAlpha(mAlpha);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int rotateAngle = (360 - sweepAngle) / 2 + 90;
        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        sweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(sweepGradient);
        mPaint.setStrokeWidth(mInnerRingLineWidth);
        int padding = mOutDialLineWidth + mSpace + mOutRingLineWidth + mSpace + mInnerRingLineWidth / 2;
        RectF rectF = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawArc(rectF, rotateAngle, sweepAngle, false, mPaint);
    }

    private void drawInnerDarkRing(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        mPaint.setAlpha(255);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int rotateAngle = (360 - sweepAngle) / 2 + 90;
        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        sweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(sweepGradient);
        mPaint.setStrokeWidth(mInnerRingLineWidth);
        int padding = mOutDialLineWidth + mSpace + mOutRingLineWidth + mSpace + mInnerRingLineWidth / 2;
        RectF rectF = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawArc(rectF, rotateAngle, sweepAngle * (mAnimPrograss / mTotalPrograss), false, mPaint);
    }

    private void drawCenterArrow(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(17);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //绘制红点
        canvas.drawPoint(getWidth() / 2, getHeight() / 2, mPaint);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mRedArrow);
        float rotateAngle = sweepAngle / 2 - (mAnimPrograss / mTotalPrograss) * sweepAngle;
        int padding = mOutDialLineWidth + mSpace + mOutRingLineWidth + mSpace + mInnerRingLineWidth / 2;
        canvas.save();
        canvas.rotate(-rotateAngle, getWidth() / 2, getHeight() / 2);
        RectF rectF = new RectF(getWidth() / 2 - 6, padding + 20, getWidth() / 2 + 6, getHeight() / 2);
        canvas.drawBitmap(bitmap, null, rectF, mPaint);
        canvas.restore();
    }

    private void drawMiniArrow(Canvas canvas, Paint mPaint) {
        mPaint.reset();
        int bitHeight = miniBitmap.getHeight();
        int bitWidth = miniBitmap.getWidth();
        float rotateAngle = sweepAngle / 2 - (mAnimPrograss / mTotalPrograss) * sweepAngle;
        int padding = mOutDialLineWidth + mSpace + mOutRingLineWidth + mSpace + mInnerRingLineWidth / 2;
        canvas.save();
        canvas.rotate(-rotateAngle, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(miniBitmap, getWidth() / 2 - bitWidth / 2, padding - bitHeight / 2, mPaint);
        canvas.restore();
    }
}
