package com.example.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DbExecutionTimeAspect {
    // 기준값 (DB는 보통 API보다 훨씬 타이트하게 잡음)
    private static final long SLOW_DB_THRESHOLD_MS = 200;

    /**
     * repository 패키지 이하의 모든 메소드 실행 시간 측정
     */
    @Around("execution(* com.example.repository..*(..))")
    public Object measureRepositoryTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        try {
            // 실제 Repository 메소드 실행
            return joinPoint.proceed();

        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;

            if (elapsedTime >= SLOW_DB_THRESHOLD_MS) {
                log.warn(
                        "[SLOW-DB] {}.{} took {}ms",
                        joinPoint.getSignature().getDeclaringTypeName(), // 클래스
                        joinPoint.getSignature().getName(),              // 메소드
                        elapsedTime
                );
            } else {
                log.debug(
                        "[DB] {}.{} took {}ms",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        elapsedTime
                );
            }
        }
    }
}
