package com.disney.studios.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("execution(* com.disney.studios.api.challenge.controller.*.*(..))")
	public void controller() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
	}

	
	@Around("controller() && allMethod()")
	public Object dogAppAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
		LOGGER.info("Entering in Method :  " + proceedingJoinPoint.getSignature().getName());
		LOGGER.info("Class Name :  " + proceedingJoinPoint.getSignature().getDeclaringTypeName());
		Object[] args =  proceedingJoinPoint.getArgs();
		for(Object o : args)
		{
		LOGGER.info("args :  " + o);
		}
		
		Object value = null;
		try {
			value = proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			LOGGER.error(e.getMessage());
		}
		
		LOGGER.info("After invoking getName() method. Return value="+value);
		return value;
	}
	
}