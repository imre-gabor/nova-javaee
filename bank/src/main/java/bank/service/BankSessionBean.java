package bank.service;

import java.util.Date;
import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.model.Account;
import bank.model.Client;

/**
 * Session Bean implementation class BankSessionBean
 */
@Stateless
public class BankSessionBean implements BankSessionBeanLocal {
	
	@EJB
	ClientDao clientDao;
	
	@EJB
	AccountDao accountDao;

    @Override
	public void createClient(Client client) {
    	clientDao.create(client);
    }
    
    @Override
	public void createAccountForClient(Account account, int clientId) throws BankException {
    	Optional<Client> optionalClient = clientDao.findById(clientId);
    	if(optionalClient.isEmpty())
    		throw new BankException("Client with given id does not exist");

    	optionalClient.get().addAccount(account);
    	account.setCreatedate(new Date());
    	accountDao.create(account);
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    }

}
