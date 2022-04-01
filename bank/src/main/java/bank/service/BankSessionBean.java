package bank.service;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import bank.dao.AbstractDao;
import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.dao.HistoryDao;
import bank.model.Account;
import bank.model.BankException;
import bank.model.Client;
import bank.model.History;
import bank.model.History.Status;

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
	
	@Resource
	TimerService timerService;

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
	public void scheduleTransfer(int fromAccountId, int toAccountId, double amount) {
    	timerService.createSingleActionTimer(5000, new TimerConfig(new TransferData(fromAccountId, toAccountId, amount), false));
    }
    
    @Timeout
    public void executeScheduledTransfer(Timer timer) {
    	TransferData transferData = (TransferData) timer.getInfo();
    	try {
			transfer(transferData.getFromAccountId(), transferData.getToAccountId(), transferData.getAmount());
		} catch (BankException e) {
			System.out.println("Scheduled transfer could not be executed.");
			e.printStackTrace();
		}
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    	History history = null;
    	Status status = null;
    	BankSessionBeanLocal self = ctx.getBusinessObject(BankSessionBeanLocal.class);
    	try {
    		//cél: akkor is le legyen naplózva a history táblába ez az esemény, ha végül a transfer sikertelen, és rollbackel
			history = self
    			.logHistory(String.format("Trying to transfer from account %d to account %d amount %f", fromAccountId, toAccountId, amount), amount);
    		
	    	Optional<Account> fromAccount = accountDao.findById(fromAccountId);
	    	Optional<Account> toAccount = accountDao.findById(toAccountId);
	    	if(!fromAccount.isPresent() || !toAccount.isPresent())
	    		throw new BankException("Non-existing account.");
	    	
	    	toAccount.get().increase(amount);
	    	fromAccount.get().decrease(amount);
	    	status = Status.SUCCESS;
    	} catch (Exception e) {
    		status = Status.FAILURE;
    		ctx.setRollbackOnly();
    		throw new BankException(e);
    	} finally {
    		self.updateHistoryStatus(history, status);
    	}
    }
    
    /*
     * csak akkor működik, más middleware szolgáltatásokhoz hasonlóan, ha a generált wrapperen keresztül hívjuk
     * ha lokálisan ebből az EJB-ből, akkor nem!
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
    public History logHistory(String message, Double amount) {
    	History history = new History(message);
    	history.setAmount(amount);
    	history.setStatus(Status.PENDING);
		return historyDao.create(history);
    }
    
    
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
    public void updateHistoryStatus(History history, Status status) {
    	history.setStatus(status);
    	historyDao.update(history);
    }
    
//    @Schedule(minute = "*/1", hour = "*")
    public void logTopTransfers() {
    	historyDao.findTop5Today().forEach(System.out::println);
    }
}
