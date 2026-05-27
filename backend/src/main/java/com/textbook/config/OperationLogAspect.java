package com.textbook.config;

import cn.hutool.json.JSONUtil;
import com.textbook.entity.OperationLog;
import com.textbook.mapper.OperationLogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogMapper logMapper;

    @Pointcut("execution(* com.textbook.controller.*Controller.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog log = new OperationLog();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            log.setIp(getClientIp(request));
            Object userId = request.getAttribute("userId");
            if (userId != null) log.setUserId((Long) userId);
            Object username = request.getAttribute("username");
            if (username != null) log.setUsername((String) username);
        }
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        log.setModule(className.replace("Controller", ""));
        log.setOperation(methodName);
        log.setMethod(className + "." + methodName);
        try {
            Object[] args = point.getArgs();
            if (args.length > 0) {
                String params = JSONUtil.toJsonStr(args[0]);
                log.setParams(params.length() > 2000 ? params.substring(0, 2000) : params);
            }
        } catch (Exception ignored) {}
        Object result;
        try {
            result = point.proceed();
            log.setStatus(1);
        } catch (Throwable e) {
            log.setStatus(0);
            log.setErrorMsg(e.getMessage() != null && e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage());
            throw e;
        } finally {
            log.setExecutionTime(System.currentTimeMillis() - startTime);
            log.setCreateTime(LocalDateTime.now());
            try { logMapper.insert(log); } catch (Exception ignored) {}
        }
        return result;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();
        return ip != null && ip.contains(",") ? ip.split(",")[0].trim() : ip;
    }
}
