package com.eric.myfunny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.eric.baselib.ioc.ViewUtils;
import com.eric.myfunny.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2018/10/12
 * desc   : 协作布局demo
 * version: 1.0
 */
public class CoordinatorLayoutDemoActivity extends BaseActivity {
    public static void action(@NonNull BaseActivity activity) {
        activity.startActivity(new Intent(activity, CoordinatorLayoutDemoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        ViewUtils.inject(this);
    }
}
