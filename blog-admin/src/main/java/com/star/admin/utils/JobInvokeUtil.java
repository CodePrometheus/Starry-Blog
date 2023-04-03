package com.star.admin.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.star.admin.entity.ScheduleJob;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Starry
 * @Date: 02-16-2023
 */
public class JobInvokeUtil {

    /**
     * 调用对应方法
     *
     * @param job
     * @throws Exception
     */
    public static void invokeMethod(ScheduleJob job) throws Exception {
        String invokeTarget = job.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);

        // 不是全限定类名 则从 Spring 中取出对象
        if (!isValidClassName(beanName)) {
            Object bean = SpringUtil.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        } else {
            // 利用反射调用无参构造方法创建对象
            Object bean = Class.forName(beanName).newInstance();
            invokeMethod(bean, methodName, methodParams);
        }
    }

    /**
     * 反射执行方法
     *
     * @param bean
     * @param methodName
     * @param methodParams
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (methodParams != null && methodParams.size() > 0) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            System.out.println("method = " + method + " methodName = " + methodName);
            method.invoke(bean);
        }
    }

    public static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }

    /**
     * 从第一个"("前的字符串中获取最后一个"."前的字符串
     * 如 java.lang.Object.method(values...) -> java.lang.Object
     *
     * @param invokeTarget
     * @return
     */
    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 从第一个"("前的字符串中获取最后一个"."后的字符串
     * 如 java.lang.Object.method(values...) -> method
     *
     * @param invokeTarget
     * @return
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 从目标字符串中提取第一个(和第一个)中间的字符串。并将其以,进行分割成字符串数据
     *
     * @param invokeTarget
     * @return
     */
    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return null;
        }
        String[] methodParams = methodStr.split(",");
        List<Object[]> classes = new LinkedList<>();
        // 仅仅支持以下数据类型：String，Boolean，Long，Double，Integer类型
        for (String methodParam : methodParams) {
            String str = StringUtils.trimToEmpty(methodParam);
            if (StringUtils.contains(str, "'")) {
                classes.add(new Object[]{StringUtils.replace(str, "'", ""), String.class});
            } else if (StringUtils.equals(str, "true") || StringUtils.equalsIgnoreCase(str, "false")) {
                classes.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            } else if (StringUtils.containsIgnoreCase(str, "L")) {
                classes.add(new Object[]{Long.valueOf(StringUtils.replaceIgnoreCase(str, "L", "")), Long.class});
            } else if (StringUtils.containsIgnoreCase(str, "D")) {
                classes.add(new Object[]{Double.valueOf(StringUtils.replaceIgnoreCase(str, "D", "")), Double.class});
            } else {
                classes.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classes;
    }

    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classes = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classes[index] = (Class<?>) os[1];
            index++;
        }
        return classes;
    }

    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classes = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classes[index] = os[0];
            index++;
        }
        return classes;
    }

}
