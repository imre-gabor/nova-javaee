package demo;

import javax.ejb.Remote;

@Remote
public interface CalculatorRemote {

	int add(int a, int b);

}
