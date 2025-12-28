package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.annotation
 * @Description: 用于标识某个方法需要自动填充处理
 * @Author: Simon
 * @CreateDate: 2025/12/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    /**
     * 数据库操作类型：INSERT、UPDATE
     */
    OperationType value();
}
