package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段填充处理
 */

//自定义注解只能加在方法上
@Target(ElementType.METHOD)
//注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在（注解在运行时可用）
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型：Update、Insert
    OperationType value();
}
