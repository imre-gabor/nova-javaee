package bank.service;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class LoggerInterceptor {

	@AroundInvoke
	public Object log(InvocationContext ctx) throws Exception {
		
		String methodName = ctx.getTarget().getClass() + "." + ctx.getMethod().getName();
		System.out.println("Calling " + methodName);
		Object returnValue = ctx.proceed();
		
		System.out.println("Returning from " + methodName + " with " + returnValue);
		return returnValue;
	}
	
}
