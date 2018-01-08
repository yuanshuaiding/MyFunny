package com.eric.baselib.views.snack;

import com.eric.baselib.R;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/12
 * desc   : 内置的几种snackbar状态
 * version: 1.0
 */

public enum SnackType {
    /**
     * 红色,错误
     */
    ERROR(R.drawable.ic_snack_error, R.color.snack_error),

    /**
     * 红色,警告
     */
    WARNING(R.drawable.ic_snack_warn, R.color.snack_warn),

    /**
     * 绿色,成功
     */
    SUCCESS(R.drawable.ic_snack_ok, R.color.snack_ok);

    private int resIcon;
    private int backgroundColor;

    SnackType(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
