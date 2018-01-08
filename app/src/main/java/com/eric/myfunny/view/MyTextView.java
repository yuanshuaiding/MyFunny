package com.eric.myfunny.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.eric.myfunny.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/18
 * desc   : 自定义View-可展开收起的TextView控件：
 * </br>可以指定显示行数，超出后则显示展开收起按钮
 * version: 1.0
 */

public class MyTextView extends View {
    private final String TAG = this.getClass().getSimpleName();
    private Drawable expandArrow;
    private Drawable shrinkArrow;
    private int position;
    private int shrinkLines;

    /**
     * 调用时机：在Java代码中通过new的方式实例化
     */
    public MyTextView(Context context) {
        this(context, null);
    }

    /**
     * 调用时机：该控件在layout布局文件中使用且没有指定android:style属性，系统会自动调用该构造方法
     */
    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 调用时机：该控件在layout布局文件中使用且指定android:style属性，系统会自动调用该构造方法
     */
    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性组
        //TypedArray myAttrs = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        //获取指定自定义属性
//        shrinkLines = myAttrs.getInt(R.styleable.MyTextView_shrinkLines, 3);
//        position = myAttrs.getInt(R.styleable.MyTextView_positions, 0);
//        shrinkArrow = myAttrs.getDrawable(R.styleable.MyTextView_arrowShrink);
//        expandArrow = myAttrs.getDrawable(R.styleable.MyTextView_arrowExpand);
//        Log.d(TAG, "MyTextView: shrinkLines-->" + shrinkLines);
//        Log.d(TAG, "MyTextView: position-->" + position);
//        Log.d(TAG, "MyTextView: shrinkArrow-->" + shrinkArrow);
//        Log.d(TAG, "MyTextView: expandArrow-->" + expandArrow);
        //回收资源
        //myAttrs.recycle();
    }

    /**
     * 自定义View的测量方法，用于确定布局的宽高，由父布局调用，可以在此通过setMeasuredDimension方法修改你真正需要的尺寸
     *
     * @param widthMeasureSpec:该32位int值包含2部分信息：前2位存储模式（如：MeasureSpec.AT_MOST），后30位存储值,代表了父布局带当前布局的尺寸限制
     * @param heightMeasureSpec                                                       同上
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1.获取测量模式（精确或自适应，自适应指的是wrap_content）
        //获取的值是32位widthMeasureSpec的前2位，即模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //MeasureSpec.AT_MOST:在layout布局中使用wrap_content指定尺寸
        //MeasureSpec.EXACTLY:在layout布局中使用具体数值指定尺寸，如：100dp,或使用match_parent
        //MeasureSpec.UNSPECIFIED:使布局尽可能的大，实际开发中很少用，多出现在系统控件：如ScrollView,ListView,RecyclerView,在这些控件中测量子控件时会用该模式
        int width = 0;
        int height = 0;
        //2.通过不同的测量模式来计算控件实际需要的空间大小
        //获取的值是32位widthMeasureSpec的后30位，即值
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //调用resolveSize确保最后计算的尺寸满足父布局限制
        width=resolveSize(width,widthMeasureSpec);
        height=resolveSize(height,heightMeasureSpec);
        //通过该内置方法最终完成设置控件的宽高值
        setMeasuredDimension(width, height);
    }

    /**
     * 用于绘制自定义View元素
     *
     * @param canvas:系统提供的画布接口，可以直接操作
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap();
        //canvas.drawText();
        //canvas.drawArc();
    }
}
