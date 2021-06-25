package com.star.common.aspect;

import com.google.common.collect.ImmutableList;
import com.star.common.annotation.Limit;
import com.star.common.constant.LimitType;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 06-17-2021 20:49
 */
@Component
public class LimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.star.common.annotation.Limit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Method method = resolveMethod(point);
        Limit annotation = method.getAnnotation(Limit.class);
        LimitType limitType = annotation.limitType();
        String name = annotation.name();
        String key;
        String ipAddr = IpUtil.getIpAddr(request);

        int period = annotation.period();
        int limitCount = annotation.count();
        switch (limitType) {
            case IP:
                key = ipAddr;
                break;
            case CUSTOMER:
                key = annotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        // 将数组以符号或其他字符串为间隔组装成新的字符串
        String join = StringUtils.join(annotation.prefix() + "_", key, ipAddr);
        // ImmutableList是一个不可变、线程安全的列表集合，它只会获取传入对象的一个副本，而不会影响到原来的变量或者对象
        ImmutableList<String> keys = ImmutableList.of(join);

        String buildLuaScript = buildLuaScript();
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(buildLuaScript, Long.class);
        Long count = redisTemplate.execute(redisScript, keys, limitCount, period);
        // 当前次数和设定的最小次数比较
        if (count != null && count.intValue() <= limitCount) {
            logger.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ipAddr, count, keys, name);
            return point.proceed();
        } else {
            logger.error("key为 {}，描述为 [{}] 的接口访问超出频率限制", keys, name);
            throw new StarryException("接口访问超出频率限制");
        }
    }


    /**
     * 路由方法消息
     *
     * @param point
     * @return
     */
    public Method resolveMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = getDeclaredMethod(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("无法解析目标方法: " + signature.getMethod().getName());
        }
        return method;
    }

    private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethod(superClass, name, parameterTypes);
            }
        }
        return null;
    }

    /**
     * 限流脚本
     * 调用的时候不超过阈值，则直接返回并执行计算器自加。
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }


}
