package bank.service;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import bank.model.Account;
import bank.model.Client;
import bank.model.History;
import bank.model.History.Status;

@Local
public interface BankSessionBeanLocal {

	void transfer(int fromAccountId, int toAccountId, double amount) throws BankException;

	void createAccountForClient(Account account, int clientId) throws BankException;

	void createClient(Client client);

	History logHistory(String message, Double amount);

	void scheduleTransfer(int fromAccountId, int toAccountId, double amount);

	void updateHistoryStatus(History history, Status status);

}
