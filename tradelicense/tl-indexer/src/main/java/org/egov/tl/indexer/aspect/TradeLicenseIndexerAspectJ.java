package org.egov.tl.indexer.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * This is aspectj class for property indexer
 * 
 * @author:narendra
 * 
 */

@Aspect
@Component
public class TradeLicenseIndexerAspectJ {

	public static final Logger logger = LoggerFactory.getLogger("org.egov.propertyindexer.logging");

	/**
	 * This pointcut will execute for controller methods
	 */

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
	}

	/**
	 * This pointcut will execute for all methods
	 */

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
	}

	/**
	 * Description :This will log before any resource annotated with @Controller
	 * annotation
	 * 
	 * @param joinPoint
	 */

	@Before("controller() && allMethod()")
	public void logBefore(JoinPoint joinPoint) {
		logger.debug("Entering in Method :  " + joinPoint.getSignature().getName());
		logger.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
		logger.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
		logger.debug("Target class : " + joinPoint.getTarget().getClass().getName());

	}

	/**
	 * Description :This will log for all method within resource annotated
	 * with @Controller annotation and return a value
	 * 
	 * @param joinPoint
	 * @param result
	 */

	@AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {

		String returnValue = this.getValue(result);

		logger.debug("LEAVING: " + joinPoint.getSignature().getName() + " WITH: " + returnValue);
	}

	/**
	 * Description : This will log for any method within resource annotated
	 * with @Controller annotation if any exception occurs
	 * 
	 * @param joinPoint
	 * @param exception
	 */

	@AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		logger.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
		logger.error("Cause : " + exception.getCause());
		exception.printStackTrace();
	}

	/**
	 * Description : This will log for any method within resource annotated
	 * with @Controller annotation
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */

	@Around("controller() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		long start = System.currentTimeMillis();
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;
			logger.debug("Method " + className + "." + methodName + " ()" + " execution time : " + elapsedTime + " ms");

			return result;
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
					+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}

	/**
	 * Description : This method will return the string represntation of object
	 * 
	 * @param result
	 * @return
	 */

	private String getValue(Object result) {

		String returnValue = null;

		if (null != result) {
			returnValue = result.toString();
		}
		return returnValue;
	}

}