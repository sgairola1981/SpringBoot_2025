package com.vayam.ichr.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.vayam.ichr.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Before method: " + joinPoint.getSignature().getName());
    }

    @After("execution(* com.vayam.ichr.*.*(..))")
    public void logAfterMethod(JoinPoint joinPoint) {
        logger.info("After method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.vayam.ichr.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method " + joinPoint.getSignature().getName() + " returned: " + result);
    }
}

