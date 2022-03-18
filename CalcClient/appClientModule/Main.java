import javax.ejb.EJB;

import demo.CalculatorRemote;
import demo.StringStoreRemote;

public class Main {
	
	@EJB
	static CalculatorRemote calc;
	
	@EJB
	static StringStoreRemote stringStore1;
	

	@EJB
	static StringStoreRemote stringStore2;

	
	public static void main(String[] args) {
		System.out.format("3 + 4 = %d%n", calc.add(3, 4));
		
		stringStore1.add("1");
		stringStore1.add("3");
		stringStore1.add("5");
		
		stringStore2.add("2");
		stringStore2.add("4");
		stringStore2.add("6");
		
		stringStore1.getAll().forEach(System.out::println);
		stringStore2.getAll().forEach(System.out::println);
		
		stringStore1.release();
		stringStore2.release();
		
		//hibát dob, ha a @Remove metódus után próbáljuk használni
//		stringStore1.add("After release");
//		stringStore1.getAll().forEach(System.out::println);

	}

}