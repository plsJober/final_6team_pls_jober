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

    @Around("execution(* com.example..controller..*(..))")
    public Object measureApiExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;

            log.info(
                    "API 실행 시간 | {}.{}() | {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    duration
            );
        }
    }
}
