package com.star.core.util;

import com.alibaba.fastjson.JSON;
import com.star.common.annotation.OptLog;
import com.star.common.tool.IpUtil;
import com.star.core.entity.OperationLog;
import com.star.core.mapper.OperationLogMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 06-25-2021 22:20
 */
@Aspect
@Component
public class OptLogAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.star.common.annotation.OptLog)")
    public void optLogPointcut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "optLogPointcut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Objects keys) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        // 获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 模块名
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        // 方法操作具体内容
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        // 操作类型
        OptLog optLog = method.getAnnotation(OptLog.class);

        // 为实体类填充内容
        operationLog.setOptModule(api.tags()[0]);
        operationLog.setOptType(optLog.optType());
        operationLog.setOptDesc(apiOperation.value());
        // 请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方式
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        operationLog.setOptMethod(methodName);
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        // 结果
        operationLog.setResponseData(JSON.toJSONString(keys));
        operationLog.setUserId(UserUtil.getLoginUser().getId());
        operationLog.setNickname(UserUtil.getLoginUser().getNickname());

        String ipAddr = IpUtil.getIpAddr(request);
        operationLog.setIpAddr(ipAddr);
        operationLog.setIpSource(IpUtil.getIpSource(ipAddr));

        operationLog.setOptUrl(request.getRequestURI());
        operationLog.setCreateTime(new Date());
        operationLogMapper.insert(operationLog);
    }


}
