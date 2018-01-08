package com.eric.myfunny.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/11/10
 * desc   : 围绕手指触摸点的字母索引控件
 * version: 1.0
 */

public class AroundLetterView extends View {
    public AroundLetterView(Context context) {
        this(context, null);
    }

    public AroundLetterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AroundLetterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
