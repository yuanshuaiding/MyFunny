package com.eric.baselib.ioc;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.eric.baselib.R;
import com.eric.baselib.utils.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 视图操作工具类：
 * 1. 解析属性注解绑定控件对象
 * 2. 解析方法注解绑定控件事件
 * version: 1.0
 */

public class ViewUtils {
    /**
     * 将activity里的控件绑定到activity里带注解的属性上，同时如有事件注解，则把事件绑定到指定控件上
     */
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }


    /**
     * 将指定view里的控件对象绑定到holder里带注解的属性上，同时如有事件注解，则把事件绑定到指定控件上
     */
    public static void inject(View view, Object holder) {
        inject(new ViewFinder(view), holder);
    }

    private static void inject(ViewFinder finder, Object holder) {
        //绑定控件
        injectFields(finder, holder);
        //绑定事件
        injectEvents(finder, holder);
    }

    private static void injectFields(ViewFinder finder, Object holder) {
        if (finder == null || holder == null) return;
        try {
            Class<?> clz = holder.getClass();
            Field[] fields = clz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    BindView bindView = field.getAnnotation(BindView.class);
                    if (bindView != null) {
                        int viewId = bindView.value();
                        View view = finder.getView(viewId);
                        try {
                            field.set(holder, view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void injectEvents(ViewFinder finder, final Object holder) {
        if (finder == null || holder == null) return;
        try {
            Class<?> clz = holder.getClass();
            Method[] methods = clz.getDeclaredMethods();
            if (methods != null && methods.length > 0) {
                for (final Method method : methods) {
                    OnClicked onclick = method.getAnnotation(OnClicked.class);
                    NetCheck netCheck = method.getAnnotation(NetCheck.class);
                    if (onclick != null) {
                        int[] viewIds = onclick.value();
                        if (viewIds != null && viewIds.length > 0) {
                            for (int viewId : viewIds) {
                                View view = finder.getView(viewId);
                                if (view != null) {
                                    view.setOnClickListener(new DeclaredOnClickListener(holder, method, netCheck));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        private final Object mHolder;
        private final Method mMethod;
        private final NetCheck mNetCheck;

        public DeclaredOnClickListener(Object holder, Method method, NetCheck netCheck) {
            this.mHolder = holder;
            this.mMethod = method;
            this.mNetCheck = netCheck;
        }

        @Override
        public void onClick(View view) {
            if (mNetCheck != null && view != null) {
                String msg = view.getContext().getString(R.string.msg_no_network);
                String value = mNetCheck.value();
                if (!TextUtils.isEmpty(value)) {
                    msg = value;
                }
                //监测网络状态,不可用则toast提示
                if (!Util.isNetAvailable(view.getContext())) {
                    Util.toast(view, msg);
                    return;
                }
            }
            try {
                mMethod.setAccessible(true);
                Class<?>[] paramTypes = mMethod.getParameterTypes();
                if (paramTypes != null && paramTypes.length > 0) {
                    //调用有参数的方法
                    mMethod.invoke(mHolder, view);
                } else {
                    //调用无参方法
                    mMethod.invoke(mHolder);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
