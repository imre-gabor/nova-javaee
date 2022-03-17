package demo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class Calculator
 */
@Stateless
public class Calculator implements CalculatorRemote {

    public Calculator() {
    	System.out.println("Calcuulator constructor called.");
    }
    
    @PostConstruct
    public void init() {
    	System.out.println("Calculator init called.");    	
    }

    
    @PreDestroy
    public void destroy() {
    	System.out.println("Calculator destroy called.");    	
    }

    
    @Override
	public int add(int a, int b) {
    	return a+b;
    }
    
}
