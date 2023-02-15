package com.star.core.hook;

import com.alibaba.fastjson2.JSON;
import com.star.admin.entity.ExceptionLog;
import com.star.admin.entity.ExceptionLogEvent;
import com.star.common.tool.IpUtils;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.star.common.exception.StarryException.getTrace;

/**
 * 异常监听钩子
 *
 * @Author: Starry
 * @Date: 02-02-2023
 */
@Aspect
@Component
public class ExceptionLogHooks {

    @Resource
    private ApplicationContext applicationContext;

    @Pointcut("execution(* com.star.*.rest..*.*(..))")
    public void exceptionLogPointCut() {
    }

    @AfterThrowing(value = "exceptionLogPointCut()", throwing = "ex")
    public void exceptionLogHooks(JoinPoint joinPoint, Exception ex) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes)
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method methodInfo = signature.getMethod();
        ApiOperation apiOperation = methodInfo.getAnnotation(ApiOperation.class);
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setOptUri(Objects.requireNonNull(request).getRequestURI());
        exceptionLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        exceptionLog.setOptMethod(joinPoint.getTarget().getClass().getName() + "." + methodInfo.getName());

        // 获取请求参数
        if (joinPoint.getArgs().length > 0) {
            if (joinPoint.getArgs()[0] instanceof MultipartFile) {
                exceptionLog.setRequestParam("file");
            } else {
                exceptionLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
            }
        }
        if (Objects.nonNull(apiOperation)) {
            exceptionLog.setOptDesc(apiOperation.value());
        } else {
            exceptionLog.setOptDesc("");
        }

        exceptionLog.setExceptionInfo(getTrace(ex));
        String ipAddr = IpUtils.getIpAddr(request);
        exceptionLog.setIpAddress(ipAddr);
        exceptionLog.setIpSource(IpUtils.getIpSource(ipAddr));
        applicationContext.publishEvent(new ExceptionLogEvent(exceptionLog));
    }

}
