package bank.service;

import javax.ejb.ApplicationException;

//a dobása by default nem okoz rollbacket, csak ha RuntimeException-ből származik, vagy így:
//@ApplicationException(rollback = true)
public class BankException extends Exception {

	public BankException(String message) {
		super(message);
	}

	public BankException(Throwable cause) {
		super(cause);
	}
	
	
	
}
