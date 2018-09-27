package com.spring.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {
	//setup Logging
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	//setup pointcut declarations
	@Pointcut("execution(* com.spring.controller.*.*(..))")
	private void forControllerPackage() {}
	
	@Pointcut("execution(* com.spring.dao.*.*(..))")
	private void forDAOPackage() {}
	
	@Pointcut("execution(* com.spring.service.*.*(..))")
	private void forServicePackage() {}
	
	@Pointcut("forControllerPackage()||forDAOPackage()||forServicePackage()")
	private void forAppFlow() {}
	
	@Before("forAppFlow()")
	public void before(JoinPoint theJoinPoint) {
		//display method the calling
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====> in @Before: calling method"+theMethod);
		
		//display arguments to the method
		Object[] args = theJoinPoint.getArgs();
		
		for (Object tempArgs : args) {
			myLogger.info("=====> Atgument: "+tempArgs);
		}
			
	}
	
	@AfterReturning(pointcut="forAppFlow()", returning="theResult")
	public void afterReturning(JoinPoint theJoinPoint, Object theResult) {
		//display method the calling
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====> in @AfterReturn: calling method"+theMethod);
		
		//display data return
		myLogger.info("=====> result: "+theResult);
	}

}
