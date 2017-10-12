package com.eric.baselib.ioc;

import android.app.Activity;
import android.view.View;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 封装findViewById方法的辅助类
 * version: 1.0
 */
class ViewFinder {
    private View mView;
    private Activity mActivity;

    ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    ViewFinder(View view) {
        this.mView = view;
    }

    View getView(int viewId) {
        View view = null;
        if (mActivity != null)
            view = mActivity.findViewById(viewId);
        else if (mView != null)
            view = mView.findViewById(viewId);
        return view;
    }
}
