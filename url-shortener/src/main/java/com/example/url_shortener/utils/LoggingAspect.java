package com.example.url_shortener.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Component *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Pointcut("within(com.example.url_shortener.service.*)" +
            " || within(com.example.url_shortener.controller..*)")
    public void applicationPackagePointcut() {
    }


    @Pointcut("within(com.example.url_shortener.service.UrlService)" +
            " || within(com.example.url_shortener.service.UserService)")
    public void springBeanPointcutService() {
    }


    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement relevantElement = findRelevantStackTraceElement(stackTrace);

        if (relevantElement != null) {
            log.error("Exception in {}.{}() at {}:{} with message {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    relevantElement.getFileName(),
                    relevantElement.getLineNumber(),
                    e.getMessage() != null ? e.getMessage() : "NULL");
        } else {
            log.error("Exception in {}.{}() with cause = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage() != null ? e.getMessage() : "NULL");
        }
    }

    private StackTraceElement findRelevantStackTraceElement(StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("com.example.url_shortener")) {
                return element;
            }
        }
        return null;
    }


    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logOutputInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isInfoEnabled()) {
            log.info(append("action", joinPoint.getSignature().getName())
                            .and(append("step", "input"))
                            .and(append("payload", joinPoint.getArgs())),
                    "INCOMING_REQUEST_STARTED");
        }
        try {
            var startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            var endtime = System.currentTimeMillis();
            if (log.isInfoEnabled()) {
                log.info(append("action", joinPoint.getSignature().getName())
                                .and(append("step", "output"))
                                .and(append("exec_time", endtime - startTime)),
                        "INCOMING_REQUEST_SUCCESS");
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}

