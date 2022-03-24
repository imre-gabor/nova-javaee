package bank.service;

import javax.ejb.Local;

import bank.model.Account;
import bank.model.Client;

@Local
public interface BankSessionBeanLocal {

	void transfer(int fromAccountId, int toAccountId, double amount) throws BankException;

	void createAccountForClient(Account account, int clientId) throws BankException;

	void createClient(Client client);

}
