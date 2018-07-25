package org.egov.tracer.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Configuration
public class PerformanceLogging {
    private Logger logger = LoggerFactory.getLogger(PerformanceLogging.class);

    @Pointcut("within(org.egov..*)")
    public void beansWithinEgov() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethods() {
    }

    @Pointcut("beansWithinEgov() && @annotation(org.springframework.web.bind.annotation.RequestMapping) && publicMethods()")
    public void egovControllerMethods() {
    }

    @Around("egovControllerMethods()")
    public Object measureEndpointExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object retVal = pjp.proceed();
        long end = System.nanoTime();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String path = Arrays.toString(methodSignature.getMethod().getAnnotation(RequestMapping.class).value());
        logger.info("Execution of " + path + " took " +
            TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return retVal;
    }
}