package com.eric.baselib.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 基于运行时反射绑定控件对象的注解
 *
 * Target(ElementType.FIELD) 代表注解使用位置为属性
 * Retention(RetentionPolicy.RUNTIME) 代表该注解为运行时注解
 * version: 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    int value();
}
