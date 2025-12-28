package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.aspect
 * @Description: 自定义切面，实现公共字段自动填充逻辑
 * @Author: Simon
 * @CreateDate: 2025/12/28
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 定义切入点，匹配所有需要自动填充公共字段的方法。
     * 织入的方法上面必须要有@AutoFill注解，才会触发自动填充公共字段的逻辑。
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {}

     /**
      * 前置通知：在执行方法之前，完成公共字段的自动填充
      * */
     @Before("autoFillPointcut()")
     public void autoFillBefore(JoinPoint joinPoint) {
        log.info("开始进行自动填充公共字段");

        //1.获取被拦截方法上的数据库操作类型
         MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
         AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
         OperationType operationType = autoFill.value();//获取其对应属性值，即数据库操作类型
         //2.获取被拦截方法的实体对象，对公共字段进行赋值
         Object[] args = joinPoint.getArgs();//获取该方法的所有参数
         //约定实体对象都放在第一个参数上
         if (args == null || args.length == 0) {
             return;
         }
         Object entity = args[0];//获取实体对象

         //3.准备赋值的数据，并通过反射进行赋值
         LocalDateTime now = LocalDateTime.now();
         Long currentId = BaseContext.getCurrentId();
         if (operationType == OperationType.INSERT) {
             //插入操作，为公共字段赋值
             try {
                 entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
                 entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                 entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(entity, currentId);
                 entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
             } catch (Exception e) {
                 log.error("自动填充公共字段失败", e);
             }
         } else if (operationType == OperationType.UPDATE) {
             //更新操作，为公共字段赋值
             try {
                 entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                 entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
             } catch (Exception e) {
                 log.error("自动填充公共字段失败", e);
             }
         }
     }


}
