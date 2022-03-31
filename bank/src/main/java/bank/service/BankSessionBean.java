package bank.service;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.jms.Session;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.dao.HistoryDao;
import bank.model.Account;
import bank.model.Client;
import bank.model.History;

/**
 * Session Bean implementation class BankSessionBean
 */
@Stateless
@Interceptors(LoggerInterceptor.class)
public class BankSessionBean implements BankSessionBeanLocal {
	
	@EJB
	ClientDao clientDao;
	
	@EJB
	AccountDao accountDao;
	
	@EJB
	HistoryDao historyDao;
	
	@Resource
	SessionContext ctx;

    @Override
	public void createClient(Client client) {
    	clientDao.create(client);
    }
    
    @Override
	public void createAccountForClient(Account account, int clientId) throws BankException {
    	Optional<Client> optionalClient = clientDao.findById(clientId);
    	if(!optionalClient.isPresent())
    		throw new BankException("Client with given id does not exist");

    	optionalClient.get().addAccount(account);
    	account.setCreatedate(new Date());
    	accountDao.create(account);
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    	try {
    		//cél: akkor is le legyen naplózva a history táblába ez az esemény, ha végül a transfer sikertelen, és rollbackel
    		ctx.getBusinessObject(BankSessionBeanLocal.class)
    			.logHistory(String.format("Trying to transfer from account %d to account %d amount %f", fromAccountId, toAccountId, amount));
    		
	    	Optional<Account> fromAccount = accountDao.findById(fromAccountId);
	    	Optional<Account> toAccount = accountDao.findById(toAccountId);
	    	if(!fromAccount.isPresent() || !toAccount.isPresent())
	    		throw new BankException("Non-existing account.");
	    	
	    	toAccount.get().increase(amount);
	    	fromAccount.get().decrease(amount);
	    	
    	} catch (Exception e) {
    		ctx.setRollbackOnly();
    		throw new BankException(e);
    	}
    }
    
    /*
     * csak akkor működik, más middleware szolgáltatásokhoz hasonlóan, ha a generált wrapperen keresztül hívjuk
     * ha lokálisan ebből az EJB-ből, akkor nem!
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
    public void logHistory(String message) {
    	historyDao.create(new History(message));
    }

}
