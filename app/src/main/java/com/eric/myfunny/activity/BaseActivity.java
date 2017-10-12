package com.eric.myfunny.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.eric.baselib.utils.Util;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 基础Activity，封装样式和一些基础操作
 * version: 1.0
 */
public class BaseActivity extends AppCompatActivity {
    static {
        //用于控制5.0以下的系统可以使用Vector Drawable
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void toast(String msg) {
        Util.toast(this, msg);
    }


}
