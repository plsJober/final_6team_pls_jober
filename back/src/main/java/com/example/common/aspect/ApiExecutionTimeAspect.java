package com.example.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ApiExecutionTimeAspect {

    private static final long SLOW_API_THRESHOLD_MS = 500;

    @Around("execution(* com.example..controller..*(..))")
    public Object measureApiTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;

            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();

            if (duration >= SLOW_API_THRESHOLD_MS) {
                log.warn(
                        "[SLOW-API] {}.{} took {} ms",
                        className,
                        methodName,
                        duration
                );
            } else {
                log.info(
                        "[API] {}.{} took {} ms",
                        className,
                        methodName,
                        duration
                );
            }
        }
    }
}
