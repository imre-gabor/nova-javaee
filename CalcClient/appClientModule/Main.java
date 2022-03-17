import javax.ejb.EJB;

import demo.CalculatorRemote;

public class Main {
	
	@EJB
	static CalculatorRemote calc;
	
	
	public static void main(String[] args) {
		System.out.format("3 + 4 = %d%n", calc.add(3, 4));
	}

}