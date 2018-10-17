package com.eric.myfunny.behavior;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2018/10/16
 * desc   : 应用于recycleview的behavior
 * version: 1.0
 */
public class HeaderScrollBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull RecyclerView child, @NonNull View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }
}
